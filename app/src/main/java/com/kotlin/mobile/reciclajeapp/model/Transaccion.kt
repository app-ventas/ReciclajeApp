package com.kotlin.mobile.reciclajeapp.model


data class Transaccion(
    var uid: String? = null,
    var uidDonante: String = "",
    var uidReciclador: String? = null,
    var fechaCreacion: String = "",
    var fechaEntrega: String? = null,
    var descripcion: String? = null,
    var cantidad: Int = 0,
    var unidadMedida: String = "",
    var idTipoMaterial: String = "",
    var puntosGanados: Int = 0,
    var material: Material? = null
) {
    // Constructor sin argumentos explícito
    constructor() : this(
        uid = null,
        uidDonante = "",
        uidReciclador = null,
        fechaCreacion = "",
        fechaEntrega = null,
        descripcion = null,
        cantidad = 0,
        unidadMedida = "",
        idTipoMaterial = "",
        puntosGanados = 0,
        material = null
    )
}
