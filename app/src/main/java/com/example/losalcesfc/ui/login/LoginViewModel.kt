package com.example.losalcesfc.ui.login


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.losalcesfc.data.local.SessionPrefs
import com.example.losalcesfc.data.model.Usuario
import com.example.losalcesfc.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UsuarioRepository.instance(app)
    private val prefs = SessionPrefs.get(app)

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(v: String) {
        _ui.value = _ui.value.copy(emailOrRut = v, emailError = null, authError = null)
    }

    fun onPasswordChange(v: String) {
        _ui.value = _ui.value.copy(password = v, pwdError = null, authError = null)
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _ui.value
        var emailErr: String? = null
        var pwdErr: String? = null

        if (s.emailOrRut.isBlank()) emailErr = "Campo requerido"
        if (s.password.length < 4)    pwdErr  = "Mínimo 4 caracteres"

        if (emailErr != null || pwdErr != null) {
            _ui.value = s.copy(emailError = emailErr, pwdError = pwdErr)
            return
        }

        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, authError = null)

            // Validación con contraseña en TEXTO PLANO
            val user: Usuario? = repo.validarLogin(
                emailOrRut = s.emailOrRut.trim(),
                passwordPlain = s.password
            )

            if (user != null) {
                // Guarda la sesión (DataStore)
                prefs.setLoggedIn(user.email)

                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            } else {
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    authError = "Credenciales inválidas o usuario inactivo"
                )
            }
        }
    }

    // Demo de reset password (mock)
    fun sendResetEmail(email: String, cb: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            cb(true, "Si el correo existe, te enviaremos un enlace a $email.")
        }
    }
}

