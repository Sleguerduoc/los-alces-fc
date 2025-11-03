package com.example.losalcesfc.data.model

data class Credentials(
    val identifier: String,
    val password: String,
    val remember: Boolean = false
)

data class CredentialsErrors(
    val identifierError: String? = null,
    val passwordError: String? = null
) {
    val hasErrors: Boolean get() = identifierError != null || passwordError != null
}

object CredentialsValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isValidEmail(value: String): Boolean =
        emailRegex.matches(value)

    fun isValidRut(value: String): Boolean {
        val cleaned = value.replace(".", "").replace("-", "").uppercase()
        if (cleaned.length < 2) return false
        val dv = cleaned.last()
        val num = cleaned.dropLast(1)
        if (num.any { !it.isDigit() }) return false

        var s = 1
        num.reversed().forEachIndexed { i, ch ->
            s = (s + (ch.code - 48) * (9 - (i % 6))) % 11
        }
        val dvCalc = if (s == 0) 'K' else (s + 47).toChar()
        return dv == dvCalc
    }

    fun isValidPassword(value: String): Boolean =
        value.length >= 6
}

fun Credentials.validate(): CredentialsErrors {
    val idOk = CredentialsValidator.isValidEmail(identifier) || CredentialsValidator.isValidRut(identifier)
    val idErr = if (!idOk) "Debe ser un Email o RUT válido" else null

    val pwdOk = CredentialsValidator.isValidPassword(password)
    val pwdErr = if (!pwdOk) "La contraseña debe tener al menos 6 caracteres" else null

    return CredentialsErrors(identifierError = idErr, passwordError = pwdErr)
}
