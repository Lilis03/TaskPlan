package com.elitecode.taskplan.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.elitecode.taskplan.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    private val _showUsuarioCreado = mutableStateOf(false)
    val showUsuarioCreado: State<Boolean> get() = _showUsuarioCreado

    // Función para actualizar el estado
    fun setShowUsuarioCreado(value: Boolean) {
        _showUsuarioCreado.value = value
    }

    //Login con Google
    fun signInWithGoogleCredential(credential: AuthCredential, home:()-> Unit)
    = viewModelScope.launch {
        try{
            auth.signInWithCredential(credential)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful) {
                        Log.d("TaskPlanLogs", "Logueado con Google exitosamente")
                    home()
                    }
                }
                .addOnFailureListener {
                    Log.d("TaskPlanLogs", "Falló el logueo con Google")
                }
        }catch(ex:Exception){
            Log.d("TaskPlanLogs", "Excepción al loguear con Google: " +
            "${ex.localizedMessage}")
        }
    }

    //Login con correo
    fun signInWithEmailAndPassword(email: String, password: String, home: ()-> Unit)
    = viewModelScope.launch {
        try{
            val authResult= auth.signInWithEmailAndPassword(email, password).await()
            Log.d("TaskPlanLogs", "Logueado con correo y contraseña.")
            home()
        }catch (ex:Exception){
            Log.d("TaskPlanLogs", "Error al loguearse con correo y password")
        }
    }

    fun signOut(navigateHome: ()-> Unit, context: Context){
        try{
            Firebase.auth.signOut()
            val googleSignInClient = GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            )

            googleSignInClient.signOut().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("TaskPlanLogs", "Sesión cerrada exitosamente")
                    navigateHome()
                }else{
                    Log.d("TaskPlanLogs", "Error al cerrar sesión de Google.")
                }
            }
        }catch (ex:Exception){
            Log.d("TaskPlanLogs", "Error al cerrar sesión")
        }
    }

    fun createUser(nombre: String, email: String,password: String, onResult: (Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val userId = auth.currentUser?.uid
                    if (userId != null){
                        val user = User(
                            id = null.toString(),
                            userId = userId.toString(),
                            nombre = nombre.toString()
                        ).toMap()

                        FirebaseFirestore.getInstance().collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("TaskPlanLogs", "Usuario registrado y guardado en Firestore")
                                onResult(true)
                                setShowUsuarioCreado(true)
                            }
                            .addOnFailureListener {
                                Log.d("TaskPlanLogs", "Error al guardar en Firestores ${it.message}")
                                onResult(false)
                            }
                       /* FirebaseFirestore.getInstance().collection("users")
                            .add(user)
                            .addOnSuccessListener {
                                Log.d("TaskPlanLogs", "Creado ${it.id}")
                                setShowCredencialesIncorrectas(true)
                            }.addOnFailureListener {
                                Log.d("TaskPlanLogs", "Ocurrió un error ${it}")
                            }*/
                    }
                }else {
                    Log.e("TaskPlanLogs", "Error al registrar usuario ${task.exception?.message}")
                    onResult(false)
                }
            }
    }
}