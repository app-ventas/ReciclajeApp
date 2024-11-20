package com.kotlin.mobile.reciclajeapp.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Usuario

object UsuarioServicioFirebase {

    fun consultarUsuario(uid: String, callback: GenericCallback<Usuario?>) {
        val database = FirebaseDatabase.getInstance().reference
        val usuarioRef = database.child("Usuarios").child(uid)

        usuarioRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    callback.onSuccess(usuario)
                } else {
                    callback.onError("No se encuentra el usuario")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        })
    }

    fun crearUsuario(usuario : Usuario, callback: GenericCallback<Usuario?>) {

        // Obtener la referencia de la base de datos
        val database = FirebaseDatabase.getInstance().reference

        // Definir la referencia al nodo donde guardarás el usuario
        val usuarioRef = database.child("Usuarios").child(usuario.uid)

        // Guardar el objeto Usuario en la base de datos
        usuarioRef.setValue(usuario).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Usuario guardado correctamente.")
                callback.onSuccess(usuario)
            } else {
                task.exception?.message?.let { callback.onError(it) }
            }
        }
    }

}
