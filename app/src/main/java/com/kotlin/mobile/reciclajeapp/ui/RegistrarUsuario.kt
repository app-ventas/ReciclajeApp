package com.kotlin.mobile.reciclajeapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kotlin.mobile.reciclajeapp.LoginActivity
import com.kotlin.mobile.reciclajeapp.R

class RegistrarUsuario : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Obtener las vistas del layout
        val nameEditText = findViewById<EditText>(R.id.et_name)
        val emailEditText = findViewById<EditText>(R.id.et_email)
        val passwordEditText = findViewById<EditText>(R.id.et_password)
        val registerButton = findViewById<Button>(R.id.btn_register)

        // Acción del botón de registro
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validar los campos y mostrar un mensaje (esto puede ser personalizado)
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Lógica para registrar al usuario
                registrarUsuarioFirebase(name,password, email);
            }
        }
    }

    private fun registrarUsuarioFirebase(name: String, password: String, email: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    // Si el login es exitoso, ir a la siguiente actividad
                    val intent = Intent(this@RegistrarUsuario, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT,).show()
                }
            }
    }
}
