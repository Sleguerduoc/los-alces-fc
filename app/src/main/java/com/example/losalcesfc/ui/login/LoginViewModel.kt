package com.example.losalcesfc.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.losalcesfc.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Instancia del repositorio (recuerda inicializarlo en MainActivity con AuthRepository.init)
    private val authRepository = AuthRepository.instance

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(value: String) {
        _ui.value = _ui.value.copy(emailOrRut = value, emailError = null, authError = null)
    }

    fun onPasswordChange(value: String) {
        _ui.value = _ui.value.copy(password = value, pwdError = null, authError = null)
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _ui.value
        var emailErr: String? = null
        var pwdErr: String? = null

        if (s.emailOrRut.isBlank()) emailErr = "Campo requerido"
        if (s.password.length < 6)   pwdErr  = "Mínimo 6 caracteres"

        if (emailErr != null || pwdErr != null) {
            _ui.value = s.copy(emailError = emailErr, pwdError = pwdErr)
            return
        }

        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, authError = null)

            // Llamamos al repositorio DEMO (sin BD) para validar credenciales y guardar sesión
            val result = authRepository.login(s.emailOrRut, s.password)

            _ui.value = _ui.value.copy(isLoading = false)

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e ->
                    _ui.value = _ui.value.copy(authError = e.message ?: "Credenciales inválidas")
                }
            )
        }
    }

    fun sendResetEmail(email: String, onResult: (success: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.requestPasswordReset(email)
            if (result.isSuccess) {
                onResult(true, "Si el correo existe, te enviaremos un enlace a $email.")
            } else {
                onResult(false, result.exceptionOrNull()?.message ?: "Error al procesar la solicitud")
            }
        }
    }
}
