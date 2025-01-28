package com.elitecode.taskplan.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun signInWithGoogleCredential(credential: AuthCredential, home:()-> Unit)
    = viewModelScope.launch {
        try{
            auth.signInWithCredential(credential)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful) {
                        Log.d("TaskPlan", "Logueado con Google exitosamente")
                    home()
                    }
                }
                .addOnFailureListener {
                    Log.d("TaskPlan", "Falló el logueo con Google")
                }
        }catch(ex:Exception){
            Log.d("TaskPlan", "Excepción al loguear con Google: " +
            "${ex.localizedMessage}")
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
                    Log.d("TaskPlan", "Sesión cerrada exitosamente")
                    navigateHome()
                }else{
                    Log.d("TaskPlan", "Error al cerrar sesión de Google.")
                }
            }
        }catch (ex:Exception){
            Log.d("TaskPlan", "Error al cerrar sesión")
        }
    }
}