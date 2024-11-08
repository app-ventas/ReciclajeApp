package com.kotlin.mobile.reciclajeapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Usuario
import com.kotlin.mobile.reciclajeapp.services.UsuarioServicioFirebase


class RegistrarUsuario : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var spinnerRol: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        // Initialize Firebase Auth
//        auth = Firebase.auth

        // Configura el Spinner
        configurarSpinner()

        val registrarButton = findViewById<Button>(R.id.btn_register)
        registrarButton.setOnClickListener { v: View? -> onRegisterClick() }
}

fun onRegisterClick() {
    // Obtener las vistas del layout
    val nombreEditText = findViewById<EditText>(R.id.et_name)
    val emailEditText = findViewById<EditText>(R.id.et_email)
    val passwordEditText = findViewById<EditText>(R.id.et_password)


    val nombre: String = nombreEditText.getText().toString().trim()
    val email: String = emailEditText.getText().toString().trim()
    val password: String = passwordEditText.getText().toString()
    if (nombre.isEmpty()) {
        nombreEditText.setError("El nombre es obligatorio")
        nombreEditText.requestFocus()
        return
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        emailEditText.setError("Introduce un correo electrónico válido")
        emailEditText.requestFocus()
        return
    }
    if (password.length < 6) {
        passwordEditText.setError("La contraseña debe tener al menos 6 caracteres")
        passwordEditText.requestFocus()
        return
    }

    // Lógica para registrar al usuario en firebase
    registrarUsuarioFirebase(nombre,password, email);
}

private fun registrarUsuarioFirebase(nombre: String, password: String, email: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                val uid = user?.uid ?: ""

                UsuarioServicioFirebase.crearUsuario(uid, nombre, email, object : GenericCallback<Usuario?> {
                    override fun onSuccess(result: Usuario?) {
                        // Aquí manejas lo que pasa cuando se guarda exitosamente
                        println("Usuario guardado correctamente.")

                        // Si el login es exitoso, ir a la siguiente actividad
                        val intent = Intent(this@RegistrarUsuario, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso
                    }

                    override fun onError(error: String) {
                        // Aquí manejas lo que pasa cuando hay un error
                        println("Error al guardar usuario: $error")
                    }
                })



            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarSpinner() {
        spinnerRol = findViewById(R.id.spinnerRol)

        // Lista de opciones para el Spinner
        val roles = listOf("Rol","Donador", "Recolector")

        // Crear un ArrayAdapter con la lista de roles
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,  // Layout básico para los elementos del Spinner
            roles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Dropdown layout

        // Asigna el adaptador al Spinner
        spinnerRol.adapter = adapter
    }

}
