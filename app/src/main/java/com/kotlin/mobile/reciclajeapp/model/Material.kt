package com.kotlin.mobile.reciclajeapp.model

data class Material(
    var uid: String? = "",             // UID del material
    val nombre: String? = "",  // Nombre del material
    val url: String? = "",     // Url de la imagen del material
    val descripcion: String = "",  // Descripcion del material
    val puntos: Int? = null    // Puntos ganados por la coleccion del material
)