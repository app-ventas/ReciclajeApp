package com.kotlin.mobile.reciclajeapp.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.model.Transaccion

object TransaccionesServicioFirebase {

    fun consultarTransaccionPorDonador(uidDonante: String, callback: GenericCallback<List<Transaccion?>>) {
        val database = FirebaseDatabase.getInstance().reference
        val transaccionesRef = database.child("Transacciones")

        // Realizamos la consulta por el campo uidDonante
        val query = transaccionesRef.orderByChild("uidDonante").equalTo(uidDonante)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transaccionesList = mutableListOf<Transaccion?>()

                // Iterar sobre cada hijo de "Materiales" y convertirlo a Material
                for (materialSnapshot in snapshot.children) {
                    val transaccion = materialSnapshot.getValue(Transaccion::class.java)
                    transaccionesList.add(transaccion)
                }

                if (transaccionesList.isNotEmpty()) {
                    callback.onSuccess(transaccionesList)
                } else {
                    callback.onError("No se encuentra la lista de transacciones")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        })


    }

    fun crearTransaccionDonante(transaccion: Transaccion, callback: GenericCallback<Transaccion>) {

        // Referencia a la base de datos de Firebase
        val database = FirebaseDatabase.getInstance().reference

        // Generar un ID único para la transacción
        val transaccionRef = database.child("Transacciones").push()

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
