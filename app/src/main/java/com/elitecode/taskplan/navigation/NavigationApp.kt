package com.elitecode.taskplan.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elitecode.taskplan.view.CalendarScreen
import com.elitecode.taskplan.view.EditarPerfilScreen
import com.elitecode.taskplan.view.EditarTareaScreen
import com.elitecode.taskplan.view.HomeScreen
import com.elitecode.taskplan.view.ListaTareasScreen
import com.elitecode.taskplan.view.LoginScreen
import com.elitecode.taskplan.view.NuevaTareaScreen
import com.elitecode.taskplan.view.PerfilScreen
import com.elitecode.taskplan.view.RegistroScreen
import com.elitecode.taskplan.viewmodel.LoginViewModel
import com.elitecode.taskplan.viewmodel.TareaViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
       /* composable(Screens.SplashScreen.route) {
            Screens.SplashScreen(navController)
        }*/
        composable(Screens.HomeScreen.route) {
            val loginvm: LoginViewModel = viewModel()
            HomeScreen(navController, loginvm)
        }
        composable(Screens.RegistroScreen.route){
            val lognmv: LoginViewModel = viewModel()
            RegistroScreen(navController, lognmv)
        }
        composable(Screens.LoginScreen.route){
            val logvm: LoginViewModel = viewModel()
            LoginScreen(navController, logvm)
        }
        composable(Screens.CalendarScreen.route){
            val taskvm: TareaViewModel = viewModel()
            CalendarScreen(navController, taskvm)
        }
        composable(Screens.NuevaTareaScreen.route){
            val tareamv: TareaViewModel = viewModel()
            NuevaTareaScreen(navController, tareamv)
        }
        composable(Screens.PerfilScreen.route){
            val perfilvm: LoginViewModel = viewModel()
            PerfilScreen(navController, perfilvm)
        }
        composable(Screens.ListaTareasScreen.route) {
            val tareavm: TareaViewModel = viewModel()
            ListaTareasScreen(navController, tareavm)
        }
        composable("editar_tarea/{id_tarea}", arguments = listOf(
            navArgument("id_tarea") { type = NavType.StringType}
        )) {
            val tareae: TareaViewModel = viewModel()
            val id_tarea = it.arguments?.getString("id_tarea")?: ""
            if(id_tarea.isNotEmpty()){
                EditarTareaScreen(navController, id_tarea = id_tarea, tareae)
            }else{
               Log.d("Id inválido", "El id es inválido")
            }
        }
        composable(
            "editarPerfil/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id_usuario = backStackEntry.arguments?.getString("id") ?: ""
            Log.d("PerfilEdit", "ID recibido en editarPerfil: $id_usuario") // Depuración

            if (id_usuario.isNotEmpty()) {
                EditarPerfilScreen(navController, id_usuario, viewModel = viewModel())
            } else {
                Log.e("PerfilEdit", "El id es inválido")
            }
        }

        /*composable("editar_tarea/{id_tarea}"){ task->
            val tareae: TareaViewModel = viewModel()
            val tareaId = task.arguments?.getString("id_tarea")
            EditarTareaScreen(navController, tareaId.toString(), tareae)
        }*/
    }
}
