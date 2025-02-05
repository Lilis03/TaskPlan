package com.elitecode.taskplan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun UsuarioCreado(onDismiss: () -> Unit, navController: NavController) {
    val openAlert = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            openAlert.value = false
            onDismiss()
        },
        title = { Text(text = "Cuenta creada") },
        text = { Text(text = "Inicia sesión con tus credenciales. ") },
        confirmButton = {
            TextButton(
                onClick = {
                    openAlert.value = false
                    onDismiss()
                    navController.navigate("login")
                }, modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x80508BBF))
            )
            { Text("Aceptar", color = Color(0xFF2B5F8C)) }
        }

    )
}

@Composable
fun CredencialesIncorrectas(onDismiss: () -> Unit) {
    val openAlert = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            openAlert.value = false
            onDismiss()
        },
        title = { Text(text = "Credenciales incorrectas") },
        text = { Text(text = "Correo o contraseña incorrectos. ") },
        confirmButton = {
            TextButton(
                onClick = {
                    openAlert.value = false
                    onDismiss()
                }, modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x80508BBF))
            )
            { Text("Aceptar", color = Color(0xFF2B5F8C)) }
        }

    )
}

@Composable
fun CamposVacios(onDismiss: () -> Unit){
    val openDialog = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
            onDismiss()
        },
        title = { Text(text = "Campos vacíos") },
        text = { Text(text = "Ingrese su correo y contraseña.") },
        confirmButton = {
            TextButton(onClick = { openDialog.value = false
                onDismiss()
            }, modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0x8085C1E9))
            )
            { Text("Aceptar", color = Color(0xFF2B5F8C)) }
        }
    )
}

@Composable
fun CorreoNoRegistrado(onDismiss: () -> Unit) {
    val openAlert = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            openAlert.value = false
            onDismiss()
        },
        title = { Text(text = "Correo no registrado") },
        text = { Text(text = "El correo proporcionado no se ha registrado.") },
        confirmButton = {
            TextButton(
                onClick = {
                    openAlert.value = false
                    onDismiss()
                }, modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x80508BBF))
            )
            { Text("Aceptar", color = Color(0xFF2B5F8C)) }
        }

    )
}