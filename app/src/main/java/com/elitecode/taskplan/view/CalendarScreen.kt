package com.elitecode.taskplan.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.viewmodel.LoginViewModel



@Composable
fun CalendarScreen(navController: NavHostController, viewModel: LoginViewModel){
    MenuLateral(navController) { paddingValues ->
        val context = LocalContext.current
        Column( modifier = Modifier.padding(paddingValues)) {
            Button(onClick = {
                viewModel.signOut(
                    context = context,
                    navigateHome = {
                        navController.navigate("home_screen") {
                            popUpTo("calendar"){ inclusive = true }
                        }
                    }
                )
            }) {
                Text("Cerrar sesión")
            }

            Icon(
                painter = painterResource(com.elitecode.taskplan.R.drawable.google_icon),
                contentDescription = "Ícono",
                modifier = Modifier.size(24.dp)
            )
        }
    }//Fin de menu Lateral
}