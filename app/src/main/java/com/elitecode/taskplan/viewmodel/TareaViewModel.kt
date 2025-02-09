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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
