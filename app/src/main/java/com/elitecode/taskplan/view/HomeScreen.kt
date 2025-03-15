package com.elitecode.taskplan.view

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.elitecode.taskplan.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: LoginViewModel){
    val maxFontSize = 6f // Tamaño máximo de la fuente en píxeles
    val minFontSize = 5f // Tamaño mínimo de la fuente en píxeles

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current.density
    val scaledFontSize = (screenWidth.value / 12) // Valor numérico sin convertir a sp aún
    val fontSize = (scaledFontSize.coerceIn(minFontSize, maxFontSize)) * density

    val token = "936938268954-c3tm4b73epmvpbm9otergnff1o0om72k.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try{
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential){
                navController.navigate("perfil")
            }
        }catch(ex: Exception){
            Log.d("TaskPlan", "GoogleSignIn falló")
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(85.dp))
        Text(text = "TaskPlan", fontSize = 70.sp, fontFamily = FontFamily.Default, color = Color(0xFF769AC4), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(15.dp))
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(com.elitecode.taskplan.R.drawable.listas),
            contentDescription = "listado imagen"
        )
        Spacer(Modifier.height(14.dp))
        Button(onClick = { navController.navigate("login") },
            modifier = Modifier
                .size(width = 250.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(
                Color(0xFF769AC4), Color.White
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(com.elitecode.taskplan.R.drawable.email),
                    contentDescription = "Ícono",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continuar con Correo",
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
            val opciones = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken(token)
                .requestEmail()
                .build()
            val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
            launcher.launch(googleSingInCliente.signInIntent)
        },
            modifier = Modifier
                   .size(width = 250.dp, height = 50.dp),
               // .size(width = 280.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent, Color(0xFF769AC4),
                //Color(0xFF769AC4), Color.White
            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(com.elitecode.taskplan.R.drawable.google_icon),
                    contentDescription = "Ícono",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continuar con Google",
                    fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(){
            Text(
                text = "¿No tienes una cuenta?",
                fontSize = 20.sp,
                color = Color.Gray
            )
            Text(
                text = " Crear cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier
                    .clickable {
                        navController.navigate("registro_screen")
                    }
                    .testTag("crearCuenta")
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }

}