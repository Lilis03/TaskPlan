package com.elitecode.taskplan.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(navController: NavHostController, viewModel: PerfilViewModel){
    MenuLateral(navController) { paddingValues ->
        val context = LocalContext.current
        Column( modifier = Modifier.padding(paddingValues)) {

        }
    }//Fin de menu Lateral

}