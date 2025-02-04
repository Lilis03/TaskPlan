package com.elitecode.taskplan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.elitecode.taskplan.view.CalendarScreen
import com.elitecode.taskplan.view.FormViewScreen
import com.elitecode.taskplan.view.HomeScreen
import com.elitecode.taskplan.view.LoginScreen
import com.elitecode.taskplan.view.RegistroScreen
import com.elitecode.taskplan.viewmodel.FormViewModel
import com.elitecode.taskplan.viewmodel.LoginViewModel


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
            RegistroScreen(navController)
        }
        composable(Screens.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(Screens.CalendarScreen.route){
            val loginvml: LoginViewModel = viewModel()
            CalendarScreen(navController, loginvml)
        }
        composable(Screens.FormScreen.route) {
            val formvm: FormViewModel = viewModel()
            FormViewScreen(navController, formvm)
        }
    }
}
