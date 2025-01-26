package com.elitecode.taskplan.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object HomeScreen : Screens("home_screen")
    object RegistroScreen : Screens("registro_screen")
    object LoginScreen : Screens("login")
    object CalendarScreen : Screens("calendar")
}