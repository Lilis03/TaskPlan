package com.elitecode.taskplan.view

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.elitecode.taskplan.R
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.components.uploadClouadinaryImage
import com.elitecode.taskplan.model.User
import com.elitecode.taskplan.viewmodel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController, id_usuario: String, viewModel: LoginViewModel) {
    var usuario by remember { mutableStateOf<User?>(null) }
    var imagenPerfil by remember { mutableStateOf<String?>(null) }
    var colorPortada by remember { mutableStateOf(Color.LightGray) }
    var showColorPicker by remember { mutableStateOf(false) }
    var colorPersonalizado by remember { mutableStateOf(Color.White) }

    val context = LocalContext.current
    val imageUrl = viewModel.imageUrl
    Log.d("EditarPerfil", "usuario para editar ${usuario}")


    LaunchedEffect(id_usuario) {
        viewModel.obtenerUsuario(id_usuario) { usuarioObtenido ->
            if (usuarioObtenido != null) {
                usuario = usuarioObtenido
                imagenPerfil = usuarioObtenido.foto_perfil
                colorPortada = Color(
                    android.graphics.Color.parseColor(
                        usuarioObtenido.color_portada ?: "#D3D3D3"
                    )
                )
                Log.d("EditarPerfil", "Se encontro el usuario ${usuario}")
            } else {
                Log.d("EditarPerfil", " Usuario sigue siendo null, mostrando mensaje...")

            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadClouadinaryImage(context, it) { url ->
                if (url != null) {
                    imagenPerfil = url
                    Log.d("EditarPerfil", "Imagen subida correctamente: $url")
                } else {
                    Log.e("EditarPerfil", "Error al subir imagen")
                }
            }
        }
    }

    MenuLateral(navController, viewModel) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (usuario == null) {
                androidx.compose.material3.CircularProgressIndicator()
                Log.d("EditarPerfil", "Aun no carga ${usuario}")
            } else {
                // Selección de Color de Portada
                Box(
                    contentAlignment = Alignment.Center, //  Centra el contenido en el Box
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(colorPortada)
                        .clickable { showColorPicker = true }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Cambiar color",
                            tint = Color.White, //  Color contrastante
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Presiona para cambiar color",
                            color = Color.White, //  Asegura contraste
                            fontSize = 16.sp, //  Aumenta la legibilidad
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (showColorPicker) {
                    AlertDialog(
                        onDismissRequest = { showColorPicker = false },
                        title = { Text("Selecciona un color de portada") },
                        text = {
                            Column {
                                //  Colores predefinidos
                                Row {
                                    listOf(Color.Red, Color.Blue, Color.Green, Color.Gray, Color.Yellow).forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .background(color)
                                                .clickable {
                                                    colorPortada = color //  Cambia el color
                                                    showColorPicker = false //  Cierra el diálogo
                                                }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                //  Sliders para colores personalizados
                                Text("Personalizar color", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                SliderColorPicker(
                                    color = colorPersonalizado,
                                    onColorChange = { nuevoColor -> colorPersonalizado = nuevoColor }
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                colorPortada = colorPersonalizado //  Guarda el color personalizado
                                showColorPicker = false
                            },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF769AC4))
                            ) {
                                Text("Seleccionar")
                            }
                        }
                    )
                }

                //  Imagen de perfil editable
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") }
                ) {
                    if (imagenPerfil != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.usuario),
                            contentDescription = "Imagen de perfil por defecto",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Cambiar color de portada",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Presiona la foto para camiarla",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                //  Campo de Nombre
                OutlinedTextField(
                    value = usuario!!.nombre,
                    onValueChange = { nuevoNombre ->
                        usuario = usuario!!.copy(nombre = nuevoNombre)
                    },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                //  Botón de Guardar Cambios
                Button(
                    onClick = {
                        usuario?.let { user ->
                            val usuarioActualizado = user.copy(
                                foto_perfil = imagenPerfil ?: user.foto_perfil,
                                color_portada = "#${Integer.toHexString(colorPortada.hashCode())}"
                            )

                            viewModel.actualizarPerfil(usuarioActualizado)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF769AC4))
                ) {
                    Text("Guardar cambios", color = Color.White)
                }
            }
        }
    }

}

@Composable
fun SliderColorPicker(color: Color, onColorChange: (Color) -> Unit) {
    var red by remember { mutableStateOf(color.red) }
    var green by remember { mutableStateOf(color.green) }
    var blue by remember { mutableStateOf(color.blue) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Rojo: ${red * 255}", fontSize = 12.sp)
        Slider(
            value = red,
            onValueChange = { red = it; onColorChange(Color(red, green, blue, 1f)) },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Red , activeTrackColor = Color.Red, inactiveTrackColor = Color.LightGray)
        )

        Text("Verde: ${green * 255}", fontSize = 12.sp)
        Slider(
            value = green,
            onValueChange = { green = it; onColorChange(Color(red, green, blue, 1f)) },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Green, activeTrackColor = Color.Green, inactiveTrackColor = Color.LightGray)
        )

        Text("Azul: ${blue * 255}", fontSize = 12.sp)
        Slider(
            value = blue,
            onValueChange = { blue = it; onColorChange(Color(red, green, blue, 1f)) },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Blue, activeTrackColor = Color.Blue, inactiveTrackColor = Color.LightGray)
        )

        //  Muestra el color seleccionado
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(red, green, blue))
        )
    }
}