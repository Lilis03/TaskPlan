package com.elitecode.taskplan.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.elitecode.taskplan.components.UsuarioCreado
import com.elitecode.taskplan.navigation.Screens
import com.elitecode.taskplan.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController, viewModel: LoginViewModel) {

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showUsuarioCreado by remember { mutableStateOf(false) }
    //var showUsuarioCreado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crea tu cuenta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //navController.navigate(Screens.MyApp.route)
                            navController.navigate(Screens.HomeScreen.route)
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(com.elitecode.taskplan.R.drawable.arrow_back),
                            contentDescription = "Flecha regreso",
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    }
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF769AC4)
                )
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(26.dp),
               // verticalArrangement = Arrangement
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(300.dp),
                    painter = painterResource(com.elitecode.taskplan.R.drawable.registro),
                    contentDescription = "listado imagen"
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Nombre") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Usuario Icon",
                            tint = Color(0xFF0769AC4)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFF769AC4),
                        focusedBorderColor = Color(0xFF769AC4),
                        cursorColor = Color(0xFF769AC4),
                        //textColor = Color.Black,
                        //placeholderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(50.dp))
                        .testTag("Nombre")
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Correo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email Icon",
                            tint = Color(0xFF0769AC4)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFF769AC4),
                        focusedBorderColor = Color(0xFF769AC4),
                        cursorColor = Color(0xFF769AC4),
                        //textColor = Color.Black,
                        //placeholderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(50.dp))
                        .testTag("Correo")
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions =  KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Password Icon",
                            tint = Color(0xFF0769AC4)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFF769AC4),
                        focusedBorderColor = Color(0xFF769AC4),
                        cursorColor = Color(0xFF769AC4),
                        //textColor = Color.Black,
                        //placeholderColor = Color.Gray
                        ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(50.dp))
                        .testTag("Password")
                )
                Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier.size(width = 250.dp, height = 50.dp)
                            .testTag("Crearcuenta"),
                        onClick = {
                            viewModel.createUser(nombre, email, password){ success ->
                                if (success){
                                    showUsuarioCreado = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF769AC4), Color.White
                        ),
                        border = BorderStroke(1.dp, Color(0xFF769AC4))
                    ) {
                        Text(text = "Crear cuenta", fontSize = 20.sp)
                    }
                if(showUsuarioCreado){

                    UsuarioCreado( onDismiss = {
                        nombre= ""
                        email = ""
                        password = ""
                        showUsuarioCreado = false
                        },
                        navController = navController)
                }
            }
        }
    }
}