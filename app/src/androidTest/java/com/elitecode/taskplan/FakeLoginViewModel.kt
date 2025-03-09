package com.elitecode.taskplan

import com.elitecode.taskplan.viewmodel.LoginViewModel

class FakeLoginViewModel: LoginViewModel() {
    var isCreateUserCalled = false

    override fun createUser(nombre: String, email: String, password: String, onSuccess: (Boolean) -> Unit) {
        isCreateUserCalled = true  // Marca que se llamó createUser
        onSuccess(true)  // Simula que la creación de usuario fue exitosa
    }
}