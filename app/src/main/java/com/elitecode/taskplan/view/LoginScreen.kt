package com.elitecode.taskplan.view

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.elitecode.taskplan.components.CamposVacios
import com.elitecode.taskplan.components.CorreoNoRegistrado
import com.elitecode.taskplan.components.CredencialesIncorrectas
import com.elitecode.taskplan.navigation.Screens
import com.elitecode.taskplan.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel){

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showCamposVacios by rememberSaveable { mutableStateOf(false) }
    val showCredencialesIncorrectas by viewModel.showCredencialesIncorrectas
    val showCorreoNoRegistrado by viewModel.showCorreoNoRegistrado

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Accede a tu cuenta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp) },
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
                    painter = painterResource(com.elitecode.taskplan.R.drawable.login2),
                    contentDescription = "listado imagen"
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    placeholder = { Text("Correo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email Icon",
                            tint = Color(0xFF0769AC4)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF769AC4),
                        unfocusedIndicatorColor = Color(0xFF769AC4),
                        cursorColor = Color(0xFF769AC4),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
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
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF769AC4),
                        unfocusedIndicatorColor = Color(0xFF769AC4),
                        cursorColor = Color(0xFF769AC4),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(50.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if(email.value.isBlank() || password.value.isBlank()){
                            showCamposVacios = true
                        }else {
                            viewModel.signInWithEmailAndPassword(email.value, password.value) {
                                navController.navigate("calendar") {
                                    popUpTo("home_screen") { inclusive = true }
                                }
                            }
                        }

                    },
                    modifier = Modifier.size(width = 250.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF769AC4), Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF769AC4))
                ) {
                    Text(text = "Iniciar sesión", fontSize = 20.sp)
                }
                if(showCamposVacios){
                    CamposVacios(onDismiss = { showCamposVacios = false })
                }else if(showCredencialesIncorrectas){
                    CredencialesIncorrectas(onDismiss = { viewModel.setShowCredencialesIncorrectas(false) })
                }else if (showCorreoNoRegistrado){
                    CorreoNoRegistrado(onDismiss = { viewModel.setShowCorreoNoRegistrado(false) })
                }
            }
        }
    }
}