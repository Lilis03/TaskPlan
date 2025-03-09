package com.elitecode.taskplan

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.elitecode.taskplan.view.RegistroScreen
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_registroFormulario(){
        val fakeViewModel = FakeLoginViewModel()

        composeTestRule.setContent {
            val navController = rememberNavController()
           RegistroScreen(navController = navController, viewModel = fakeViewModel)
        }

        composeTestRule.onNodeWithTag("Nombre").performTextInput("Andrea González")
        composeTestRule.onNodeWithTag("Correo").performTextInput("andreagonzalez@gmail.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("tamarindo1234")
        composeTestRule.onNodeWithTag("Crearcuenta").performClick()

        assert(fakeViewModel.isCreateUserCalled){ "El método createUser no fu ellamado"}
        composeTestRule.onNodeWithTag("alert_usuario_creado").assertIsDisplayed()
    }
}