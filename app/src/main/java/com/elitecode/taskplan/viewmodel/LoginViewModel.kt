package com.elitecode.taskplan.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.elitecode.taskplan.R
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class LoginViewModel: ViewModel() {
    private val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    var tareas by mutableStateOf<List<Tarea>>(emptyList())
        private set

    var usuario by mutableStateOf<User?>(null)
        private set

    var imageUrl: String = "" // Asegúrate de que sea una propiedad mutable
        private set

    private val _showUsuarioCreado = mutableStateOf(false)
    val showUsuarioCreado: State<Boolean> get() = _showUsuarioCreado
    fun setShowUsuarioCreado(value: Boolean) {
        _showUsuarioCreado.value = value
    }

    private val _showCredencialesIncorrectas = mutableStateOf(false)
    val showCredencialesIncorrectas: State<Boolean> get() = _showCredencialesIncorrectas
    fun setShowCredencialesIncorrectas(value: Boolean) {
        _showCredencialesIncorrectas.value = value
    }
    private val _showCorreoNoRegistrado = mutableStateOf(false)
    val showCorreoNoRegistrado: State<Boolean> get() = _showCorreoNoRegistrado
    fun setShowCorreoNoRegistrado(value: Boolean) {
        _showCorreoNoRegistrado.value = value
    }


    //Login con Google
    @RequiresApi(Build.VERSION_CODES.O)
    fun signInWithGoogleCredential(credential: AuthCredential, home: () -> Unit) = viewModelScope.launch {
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TaskPlanLogs", "Logueado con Google exitosamente")
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            Log.d("TaskPlanLogs", "UID del usuario: $userId")
                        } else {
                            Log.e("TaskPlanLogs", "El UID del usuario es nulo")
                        }
                        if (userId != null) {
                            // Verificar si el usuario existe en Firestore
                            db.collection("users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (!document.exists()) {
                                        // Crear el usuario si no existe
                                        val user = User(
                                            user_id = userId,
                                            nombre = auth.currentUser?.displayName ?: "Usuario Google",
                                            email = auth.currentUser?.email ?: "",
                                            fecha_registro = LocalDate.now().toString(),
                                            foto_perfil = "https://res.cloudinary.com/dgrvrwdk9/image/upload/v1741750072/usuario_igam2y.png", // 🔹 URL de imagen por defecto
                                            color_portada = "#808080" //
                                        ).toMap()

                                        db.collection("users")
                                            .document(userId)
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("TaskPlanLogs", "Usuario de Google creado en Firestore")
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("TaskPlanLogs", "Error al crear usuario en Firestore: ${exception.message}")
                                            }
                                    } else {
                                        Log.d("TaskPlanLogs", "El usuario ya existe en Firestore")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("TaskPlanLogs", "Error al verificar usuario en Firestore: ${exception.message}")
                                }
                        }
                        home()
                    } else {
                        Log.d("TaskPlanLogs", "Falló el logueo con Google: ${task.exception?.message}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TaskPlanLogs", "Falló el logueo con Google: ${exception.message}")
                }
        } catch (ex: Exception) {
            Log.d("TaskPlanLogs", "Excepción al loguear con Google: ${ex.localizedMessage}")
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
            Log.d("TaskPlan", "Error al loguearse con correo y password")
            when(ex){
                is FirebaseAuthInvalidCredentialsException -> {
                    Log.d("TaskPlan", "Credenciales incorrectas")
                    setShowCredencialesIncorrectas(true)
                }
                is FirebaseAuthInvalidUserException -> {
                    Log. d("TaskPlan", "Correo no encontrado.")
                    setShowCorreoNoRegistrado(true)
                }
            }
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

   // open fun createUser(nombre: String, email: String, password: String, onResult: (Boolean) -> Unit){
    @RequiresApi(Build.VERSION_CODES.O)
    fun createUser(nombre: String, email: String, password: String, onResult: (Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val userId = auth.currentUser?.uid
                    if (userId != null){
                        val user = User(
                            user_id = userId.toString(),
                            nombre = nombre.toString(),
                            email = email,
                            fecha_registro = LocalDate.now().toString(),
                            foto_perfil = "https://res.cloudinary.com/dgrvrwdk9/image/upload/v1741750072/usuario_igam2y.png", // 🔹 URL de imagen por defecto
                            color_portada = "#808080" //
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
                    }
                }else {
                    Log.e("TaskPlanLogs", "Error al registrar usuario ${task.exception?.message}")
                    onResult(false)
                }
            }
    }
    fun cargarTareas() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("tareas")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                tareas = snapshot.documents.mapNotNull { it.toObject(Tarea::class.java) }
            }
    }

    // Cargar información del usuario
    fun cargarUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("PerfilScreen", "Usuario no autenticado")
            return
        }

        val userId = user.uid
        Log.d("PerfilScreen", "UID del usuario: $userId") // Log para depuración

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("PerfilScreen", "Usuario encontrado en Firestore: ${document.data}") // Log para depuración
                    usuario = document.toObject(User::class.java) // Asignar el usuario
                    if (!usuario?.foto_perfil.isNullOrEmpty()) {  // Verifica que 'foto_perfil' no sea null ni vacío
                        val identificador = usuario?.foto_perfil?.substringAfter("/upload/")?.substringBeforeLast(".")
                        if (identificador != null) {
                            imageUrl = "https://res.cloudinary.com/dgrvrwdk9/image/upload/$identificador"  // URL directa
                            Log.d("PerfilScreen", "Imagen URL: $imageUrl")
                        } else {
                            Log.e("PerfilScreen", "No se pudo extraer el identificador de la imagen")
                        }
                    } else {
                        Log.e("PerfilScreen", "Foto de perfil no disponible o vacía")
                    }
                    Log.d("PerfilScreen", "Usuario encontrado en Firestore: ${usuario}")
                } else {
                    Log.e("PerfilScreen", "El usuario no existe en Firestore. UID: $userId")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PerfilScreen", "Error al cargar el usuario: ${exception.message}")
            }
    }


    fun obtenerUsuario(id: String, onResult: (User?) -> Unit) {
        Log.d("EditarPerfil", "Buscando usuario con ID: $id")

        db.collection("users").document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val usuario = document.toObject(User::class.java)
                    Log.d("EditarPerfil", "Usuario encontrado: ${usuario}")
                    if (!usuario?.foto_perfil.isNullOrEmpty()) {  // Verifica que 'foto_perfil' no sea null ni vacío
                        val identificador = usuario?.foto_perfil?.substringAfter("/upload/")?.substringBeforeLast(".")
                        if (identificador != null) {
                            imageUrl = "https://res.cloudinary.com/dgrvrwdk9/image/upload/$identificador"  // URL directa
                            Log.d("PerfilScreen", "Imagen URL: $imageUrl")
                        } else {
                            Log.e("PerfilScreen", "No se pudo extraer el identificador de la imagen")
                        }
                    } else {
                        Log.e("PerfilScreen", "Foto de perfil no disponible o vacía")
                    }
                    onResult(usuario)


                } else {
                    Log.e("EditarPerfil", "Usuario no encontrado en Firestore. ID: $id")
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("EditarPerfil", "Error al obtener el documento: ", e)
            }
    }

    fun actualizarPerfil(user: User) {
        db.collection("users").document(user.user_id)
            .set(user)
            .addOnSuccessListener {
                Log.d("EditarPerfil", "Perfil actualizado correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("EditarPerfil", "Error al actualizar Firestore: ${e.message}")
            }
    }


}