package com.kotlin.mobile.reciclajeapp.model

data class Transaccion(
    val uidDonante: String = "",      // UID del donante
    val uidReciclador: String? = "",  // UID del reciclador, opcional
    val fecha: String = "",           // Fecha de la transacción en formato string (YYYY/MM/DD)
    val descripcion: String = "",     // Descripcion de la transacción
    val hora: String = "",            // Hora de la transacción en formato string (HH24:MM:SS)
    val cantidad: Int,                // Cantidad de material reciclado
    val tipoMaterial: Int,            // Identificador del tipo de material (1=Plástico, 2=Vidrio, etc.)
    val puntosGanados: Int? = null    // Puntos ganados por la transacción, opcional
)