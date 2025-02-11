package com.elitecode.taskplan.viewmodel

import android.content.ClipDescription
import android.util.Log
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class TareaViewModel : ViewModel(){
    private val db = Firebase.firestore
    private val _tarea = mutableStateOf(Tarea())
    val tarea: State<Tarea> = _tarea

    private val _showTareaCreada = mutableStateOf(false)
    val showTareaCreada: State<Boolean> get() = _showTareaCreada
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
        obtenerTareas()
    }

    fun obtenerTareas(){
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
            }catch (e: Exception){
                Log.d("FirestoreError", "Error obteniendo las tareas")
            }
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

    fun editarTarea(id_tarea: String){
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
                            obtenerTareas()
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

    fun newTask(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val tareaFinal = _tarea.value.copy(
            id_tarea = UUID.randomUUID().toString(),
            user_id = userId)
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("tareas").document(tareaFinal.id_tarea).set(tareaFinal)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d("RegistroTarea", "Datos registrados correctamente.")
                            setShowTareaCreada(true)

                    }else {
                        Log.e("RegistroTarea", "Error al registrar la tarea")
                    }
                }
            }
        }
    }
