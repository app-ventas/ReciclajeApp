package com.kotlin.mobile.reciclajeapp.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.model.Usuario

object MaterialServicioFirebase {

    fun consultarMateriales(callback: GenericCallback<List<Material?>>) {
        val database = FirebaseDatabase.getInstance().reference
        val materialesRef = database.child("Materiales")

        materialesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val materialesList = mutableListOf<Material?>()

                // Iterar sobre cada hijo de "Materiales" y convertirlo a Material
                for (materialSnapshot in snapshot.children) {
                    val material = materialSnapshot.getValue(Material::class.java)
                    materialesList.add(material)
                }

                if (materialesList.isNotEmpty()) {
                    callback.onSuccess(materialesList)
                } else {
                    callback.onError("No se encuentra la lista de materiales")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        })
    }

    fun crearMaterial(material: Material ,  callback: GenericCallback<Material?>) {

        // Obtener la referencia de la base de datos
        val database = FirebaseDatabase.getInstance().reference

        // Definir la referencia al nodo donde guardarás el material
        val usuarioRef = database.child("Materiales").push() // Usar push para generar un ID único

        // Asignar el ID generado al material
        material.uid = usuarioRef.key.toString() // Asignar el ID generado al objeto material

        // Guardar el objeto Usuario en la base de datos
        usuarioRef.setValue(material).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Material guardado correctamente.")
                callback.onSuccess(material)
            } else {
                task.exception?.message?.let { callback.onError(it) }
            }
        }
    }

}
