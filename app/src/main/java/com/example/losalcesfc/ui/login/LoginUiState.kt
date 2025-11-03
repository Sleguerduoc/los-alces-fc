package com.example.losalcesfc.ui.login

data class LoginUiState(
    val emailOrRut: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val pwdError: String? = null,
    val authError: String? = null
)