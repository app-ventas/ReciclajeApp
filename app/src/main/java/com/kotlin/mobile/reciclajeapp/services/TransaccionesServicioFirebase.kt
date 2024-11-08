package com.kotlin.mobile.reciclajeapp.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Transaccion

object TransaccionesServicioFirebase {

    fun consultarTransaccionPorDonador(uidDonante: String, callback: GenericCallback<List<Transaccion>>) {
        val database = FirebaseDatabase.getInstance().reference
        val transaccionesRef = database.child("Transacciones")

        // Realizamos la consulta por el campo uidDonante
        val query = transaccionesRef.orderByChild("uidDonante").equalTo(uidDonante)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transaccionesList = mutableListOf<Transaccion>()

                // Iteramos sobre todos los hijos que coinciden con el uidDonante
                for (transaccionSnapshot in snapshot.children) {
                    val transaccion = transaccionSnapshot.getValue(Transaccion::class.java)
                    if (transaccion != null) {
                        transaccionesList.add(transaccion)
                    }
                }

                // Devolvemos la lista de transacciones al callback
                callback.onSuccess(transaccionesList)
            }

            override fun onCancelled(error: DatabaseError) {
                // En caso de error, devolvemos el mensaje de error
                callback.onError(error.message)
            }
        })
    }

    fun crearTransaccionDonante(uidDonante: String, fecha: String, hora: String, cantidad: Int, tipoMaterial: Int, callback: GenericCallback<Transaccion>) {

        // Referencia a la base de datos de Firebase
        val database = FirebaseDatabase.getInstance().reference

        // Generar un ID único para la transacción
        val transaccionRef = database.child("Transacciones").push()

        // Crear el objeto Transaccion con los datos proporcionados
        val transaccion = Transaccion(
            uidDonante = uidDonante,
            fecha = fecha,
            hora = hora,
            cantidad = cantidad,
            tipoMaterial = tipoMaterial
        )

        // Almacenar la transacción en la base de datos
        transaccionRef.setValue(transaccion).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Si se guarda exitosamente, llamamos al callback onSuccess con la transacción
                callback.onSuccess(transaccion)
            } else {
                // Si hay un error, llamamos al callback onError con el mensaje del error
                callback.onError(task.exception?.message ?: "Error desconocido al crear transacción")
            }
        }
    }
}
