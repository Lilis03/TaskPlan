package com.elitecode.taskplan.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.elitecode.taskplan.viewmodel.PageViewModel

@Composable
fun PageViewScreen(navController: NavHostController, viewModel: PageViewModel){
    Text("Pagina principal")
}