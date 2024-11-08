package com.kotlin.mobile.reciclajeapp.model

import java.io.Serializable

data class Usuario(
    val uid: String = "",      // UID del usuario (debe venir del autenticador Firebase)
    val nombre: String = "",   // Nombre del usuario
    val email: String = "",    // Email del usuario
    val ciudad: String = "",   // Ciudad del usuario
    val numeroDocumento: String = "",    // Número de documento del usuario
    val rol: String = "",      // Rol del usuario
    val saldo: Int = 0 // Saldo inicial será 0 por defecto
) : Serializable