package com.example.losalcesfc.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
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
        if (s.password.length < 6) pwdErr = "Mínimo 6 caracteres"

        if (emailErr != null || pwdErr != null) {
            _ui.value = s.copy(emailError = emailErr, pwdError = pwdErr)
            return
        }

        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, authError = null)
            // Simula autenticación (reemplaza por llamada a tu backend)
            delay(1000)
            val ok = s.emailOrRut == "admin@alces.cl" && s.password == "alces123"

            if (ok) {
                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            } else {
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    authError = "Credenciales inválidas"
                )
            }
        }
    }
}