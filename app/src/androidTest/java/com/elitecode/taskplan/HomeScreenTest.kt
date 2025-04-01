package com.elitecode.taskplan

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elitecode.taskplan.ui.theme.TaskPlanTheme
import com.elitecode.taskplan.view.HomeScreen
import com.elitecode.taskplan.view.LoginScreen
import com.elitecode.taskplan.view.RegistroScreen
import com.elitecode.taskplan.viewmodel.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeScreenTest {

    //Regla de prueba. Se encarga de lanzar la actividad principal antes de cada prueba
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        composeTestRule.activity.setContent {
            TaskPlanTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen"){ HomeScreen(navController, viewModel = LoginViewModel()) }
                    composable("registro_screen"){ RegistroScreen(navController, viewModel = LoginViewModel()) }
                    composable("login"){ LoginScreen(navController, viewModel = LoginViewModel()) }
                }
               // HomeScreen(navController = rememberNavController(), viewModel = LoginViewModel())
            }
        }

    }

    @Test
    fun test_navigateToLoginScreen_buttonEmail(){
        //Busca y encuentra el botón "Continuar con Correo" y haz click
        composeTestRule.onNodeWithText("Continuar con Correo").performClick()

        //Verifica si la navegación se realizó buscando un texto en la pantalla
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Accede a tu cuenta").assertIsDisplayed()

    }

    @Test
    fun test_botonGoogleLogin(){
        composeTestRule.waitForIdle()
        //Verifica que el botón "Continuar con Google exista en la pantalla"
        composeTestRule.onNodeWithText("Continuar con Google").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar con Google").performClick()
    }

    @Test
    fun test_crearCuenta(){
        composeTestRule.waitForIdle()
        // Verifica que el texto "Crear cuenta" con el testTag esté visible
        composeTestRule.onNodeWithTag("crearCuenta").assertIsDisplayed()
        // Simula el clic en "Crear cuenta"
        composeTestRule.onNodeWithTag("crearCuenta").performClick()
        // Verifica la nueva pantalla "Crea tu cuenta"
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Crea tu cuenta").assertIsDisplayed()
    }
}