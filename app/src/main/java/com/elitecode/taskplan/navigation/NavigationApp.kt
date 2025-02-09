package com.elitecode.taskplan.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.elitecode.taskplan.view.CalendarScreen
import com.elitecode.taskplan.view.HomeScreen
import com.elitecode.taskplan.view.LoginScreen
import com.elitecode.taskplan.view.NuevaTareaScreen
import com.elitecode.taskplan.view.PageViewScreen
import com.elitecode.taskplan.view.PerfilScreen
import com.elitecode.taskplan.view.RegistroScreen
import com.elitecode.taskplan.viewmodel.LoginViewModel
import com.elitecode.taskplan.viewmodel.PageViewModel
import com.elitecode.taskplan.viewmodel.PerfilViewModel
import com.elitecode.taskplan.viewmodel.TaskViewModel


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
            val taskvm: TaskViewModel = viewModel()
            CalendarScreen(navController, taskvm)
        }
        composable(Screens.NuevaTareaScreen.route){
            NuevaTareaScreen(navController)
        }
        composable(Screens.PageFirstScreen.route){
            val pagevm: LoginViewModel = viewModel()
            PageViewScreen(navController, pagevm)
        }
        composable(Screens.PerfilScreen.route){
            val perfilvm: PerfilViewModel = viewModel()
            PerfilScreen(navController, perfilvm)
        }
    }
}
