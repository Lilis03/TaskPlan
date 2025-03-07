package com.elitecode.taskplan.viewmodel

import android.app.Activity
import android.app.AlarmManager
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import android.provider.Settings
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitecode.taskplan.components.scheduleNotification
import com.elitecode.taskplan.model.Tarea
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import android.Manifest

class TareaViewModel : ViewModel(){
    private val db = Firebase.firestore
    private val _tarea = mutableStateOf(Tarea())
    var tarea: State<Tarea> = _tarea

    private val _showTareaCreada = mutableStateOf(false)
    val showTareaCreada: State<Boolean> get() = _showTareaCreada

    private var _tasks = MutableStateFlow<List<Tarea>>(emptyList())
    var tasks: StateFlow<List<Tarea>> = _tasks

    var isLoading by mutableStateOf(false)
        private set

    fun setShowTareaCreada(value: Boolean) {
        _showTareaCreada.value = value
    }

    private val _showTareaEditada = mutableStateOf(false)
    val showTareaEditada: State<Boolean> get() = _showTareaEditada
    fun setShowTareaEditada(value: Boolean) {
        _showTareaEditada.value = value
    }

    private val _listaTareas = MutableStateFlow<List<Tarea>>(emptyList())
    val listaTareas = _listaTareas.asStateFlow()

    init {
        //obtenerTareas()
    }
    fun obtenerTareas(context: Context){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resultado = db.collection("tareas")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val tareas = resultado.documents.mapNotNull {
                    it.toObject(Tarea::class.java)
                }

                withContext(Dispatchers.Main){
                    _listaTareas.value = tareas
                }

                tareas.forEach{ tarea ->
                    if (tarea.recordatorio){
                        realizarrecordatorio(context, tarea)
                    }
                }
            }catch (e: Exception){
                Log.d("FirestoreError", "Error obteniendo las tareas")
            }
        }
    }

    private fun realizarrecordatorio(context: Context, tarea: Tarea) {
        Log.d("RealizarRecordatorio", "Intentando programar recordatorio para: ${tarea.titulo}")

        if (tarea.recordatorio && tarea.fecha_recordatorio.isNotEmpty() && tarea.hora_recordatorio.isNotEmpty()) {
            val fechaHora = parseFechaHora(tarea.fecha_recordatorio, tarea.hora_recordatorio)
            if (fechaHora != null) {
                scheduleNotification(context, fechaHora.timeInMillis, tarea.titulo, tarea.descripcion)
                Log.d("RealizarRecordatorio", "Notificación programada para: ${fechaHora.time}")
            } else {
                Log.e("RealizarRecordatorio", "Error al convertir fecha y hora del recordatorio.")
            }
        } else {
            Log.w("RealizarRecordatorio", "Recordatorio no programado: fecha/hora vacías.")
        }
    }


    private fun parseFechaHora(fecha: String, hora: String): Calendar? {
        return try {
            Log.d("ParseFechaHora", "Recibiendo Fecha: $fecha - Hora: $hora")

            val (day, month, year) = fecha.split("/").map { it.toInt() }

            // Expresión regular mejorada para capturar AM/PM con o sin puntos
            val horaRegex = Regex("(\\d{1,2}):(\\d{2})\\s*(a\\.m\\.|p\\.m\\.|AM|PM)", RegexOption.IGNORE_CASE)
            val matchResult = horaRegex.find(hora.trim())

            if (matchResult == null) {
                Log.e("ParseFechaHora", "Formato de hora inválido: $hora")
                return null
            }

            val (hour, minute, amPm) = matchResult.destructured

            Log.d("ParseFechaHora", "Fecha dividida: $day/$month/$year - Hora: $hour:$minute $amPm")

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1) // Los meses en Calendar son 0-based
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.MINUTE, minute.toInt())
                set(Calendar.SECOND, 0)

                // Convertir AM/PM correctamente
                if (amPm.contains("p", ignoreCase = true)) {
                    set(Calendar.HOUR_OF_DAY, if (hour.toInt() == 12) 12 else hour.toInt() + 12) // PM
                } else {
                    set(Calendar.HOUR_OF_DAY, if (hour.toInt() == 12) 0 else hour.toInt()) // AM
                }
            }

            Log.d("ParseFechaHora", "Fecha convertida correctamente: ${calendar.time}")

            return calendar
        } catch (e: Exception) {
            Log.e("ParseFechaHora", "Error al parsear fecha/hora: ${e.message}")
            null
        }
    }




    fun eliminarTarea(id_tarea: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(id_tarea).delete()
                .addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        _listaTareas.value = listaTareas.value.filter {
                            it.id_tarea != id_tarea
                        }
                    }
                }
        }
    }

    fun obtenerTareaPorId(id_tarea: String, onTareaObtenida: (Tarea) -> Unit){
        db.collection("tareas").document(id_tarea).get()
            .addOnSuccessListener { task ->
                val tarea = task.toObject(Tarea::class.java)
                if(tarea != null){
                    _tarea.value = tarea
                    onTareaObtenida(tarea)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al obtener tarea ${exception.message}")
            }
    }

    fun editarTarea(id_tarea: String, context: Context){
        val tareaFinal = _tarea.value

        if(tareaFinal == null){
            Log.e("Error", "La tarea es nula")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(id_tarea)
                    //.set(tareaFinal)
                .update(
                    mapOf(
                        "titulo" to tareaFinal.titulo,
                        "descripcion" to tareaFinal.descripcion,
                        "hora" to tareaFinal.hora,
                        "fecha" to tareaFinal.fecha,
                        "categoria" to tareaFinal.categoria,
                        "prioridad" to tareaFinal.prioridad,
                        "recordatorio" to tareaFinal.recordatorio,
                        "color" to tareaFinal.color
                    )
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            setShowTareaEditada(true)
                            obtenerTareas(context)
                            Log.d("TareaEditada", "Tarea editada correctamente")
                        } else {
                            Log.d("Error", "Ocurrió un error al tratar de modificar los datos.")
                        }
                    }
            }
    }

    fun onTituloChange(newTitulo:String){
        _tarea.value = _tarea.value.copy(titulo = newTitulo)
    }
    fun onDescripcionChange(newDescripcion: String){
        _tarea.value = _tarea.value.copy(descripcion = newDescripcion)
    }
    fun onHoraChange(newHora: String){
        _tarea.value = _tarea.value.copy(hora = newHora)
    }
    fun onFechaChange(newFecha: String){
        _tarea.value = _tarea.value.copy(fecha = newFecha)
    }
    fun onCategoriaChange(newCat: String){
        _tarea.value = _tarea.value.copy(categoria = newCat)
    }
    fun onPrioridadChange(newPrio: String){
        _tarea.value = _tarea.value.copy(prioridad = newPrio)
    }
    fun onRecordChange(newRec: Boolean){
        _tarea.value = _tarea.value.copy(recordatorio = newRec)
    }
    fun onColorChange(newColor: String){
        _tarea.value = _tarea.value.copy(color = newColor)
    }
    fun onFechaRecordatorioChange(newFecha: String) {
        Log.d("RecordatorioDebug", "Fecha de recordatorio actualizada: $newFecha")
        _tarea.value = _tarea.value.copy(fecha_recordatorio = newFecha)
    }

    fun onHoraRecordatorioChange(newHora: String) {
        Log.d("RecordatorioDebug", "Hora de recordatorio actualizada: $newHora")
        _tarea.value = _tarea.value.copy(hora_recordatorio = newHora)
    }

    fun newTask(context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val tareaFinal = _tarea.value.copy(
            id_tarea = UUID.randomUUID().toString(),
            user_id = userId
        )

        Log.d("RegistroTarea", "Datos antes de registrar en Firebase:")
        Log.d("RegistroTarea", "Título: ${tareaFinal.titulo}")
        Log.d("RegistroTarea", "Descripción: ${tareaFinal.descripcion}")
        Log.d("RegistroTarea", "Fecha: ${tareaFinal.fecha}")
        Log.d("RegistroTarea", "Hora: ${tareaFinal.hora}")
        Log.d("RegistroTarea", "Recordatorio: ${tareaFinal.recordatorio}")
        Log.d("RegistroTarea", "Fecha Recordatorio: ${tareaFinal.fecha_recordatorio}")
        Log.d("RegistroTarea", "Hora Recordatorio: ${tareaFinal.hora_recordatorio}")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(tareaFinal.id_tarea).set(tareaFinal)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("RegistroTarea", "Tarea registrada correctamente en Firebase.")
                            setShowTareaCreada(true)

                            // Verificar permisos antes de programar la notificación
                            if (tareaFinal.recordatorio && tareaFinal.fecha_recordatorio.isNotEmpty() && tareaFinal.hora_recordatorio.isNotEmpty()) {
                                try {
                                    val fechaHora = parseFechaHora(tareaFinal.fecha_recordatorio, tareaFinal.hora_recordatorio)

                                    if (fechaHora != null) {
                                        // Verificar permisos en Android 12+
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                            if (!alarmManager.canScheduleExactAlarms()) {
                                                Log.e("RegistroTarea", "No se tiene permiso para programar alarmas exactas.")
                                                // Abrir la configuración de la app para que el usuario habilite el permiso
                                                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                                context.startActivity(intent)
                                                return@addOnCompleteListener
                                            }
                                        }

                                        // Verificar permisos en Android 13+
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            val permission = Manifest.permission.POST_NOTIFICATIONS
                                            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                                                // Solicitar el permiso
                                                ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), 1)
                                                return@addOnCompleteListener
                                            }
                                        }

                                        // Programar la notificación
                                        scheduleNotification(
                                            context,
                                            fechaHora.timeInMillis,
                                            tareaFinal.titulo,
                                            tareaFinal.descripcion
                                        )
                                        Log.d("RegistroTarea", "Notificación programada correctamente.")
                                    } else {
                                        Log.e("RegistroTarea", "Error al convertir fecha y hora del recordatorio.")
                                    }
                                } catch (e: Exception) {
                                    Log.e("RegistroTarea", "Error al programar notificación: ${e.message}")
                                }
                            } else {
                                Log.w("RegistroTarea", "Recordatorio no programado: fecha/hora vacías.")
                            }
                        } else {
                            val exception = task.exception
                            if (exception != null) {
                                Log.e("RegistroTarea", "Error al registrar la tarea: ${exception.message}")
                            } else {
                                Log.e("RegistroTarea", "Error al registrar la tarea: Error desconocido")
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e("RegistroTarea", "Excepción general en newTask(): ${e.message}")
            }
        }
    }




    fun loadTasks(userId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        isLoading = true
        Log.d("CalendarTask", "${userId}")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot = db.collection("tareas")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val tareasList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Tarea::class.java)
                }

                _tasks.value = tareasList
                isLoading = false
                Log.d("CalendarTask", "${_tasks.value}")
            } catch (e: Exception) {
                Log.e("CalendarTask", "Error cargando tareas: ", e)
                isLoading = false
            }
        }
    }
    fun observeTasks(userId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        isLoading = true

        db.collection("tareas")
            .whereEqualTo("user_id", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firebase", "Error en listener: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val tareasList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Tarea::class.java)
                    }
                    _tasks.value = tareasList
                }
            }
    }
}
