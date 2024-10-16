package com.kotlin.mobile.reciclajeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kotlin.mobile.reciclajeapp.ui.RegistrarUsuario

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Firebase Auth
        auth = Firebase.auth

        //val service = RetrofitServicesFactory.create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        // set on-click listener
        buttonLogin.setOnClickListener {
            val errorMessage: TextView = findViewById(R.id.error_message)
            errorMessage.visibility = View.GONE

            val email = findViewById<TextView>(R.id.email).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if(password.length >= 6 ) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            // Si el login es exitoso, ir a la siguiente actividad
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT,).show()
                        }
                    }
                }
                else {
                    errorMessage.text = "Email o contraseña no pueden estar vacíos"
                    errorMessage.visibility = View.VISIBLE
                }
            }
            else {
                errorMessage.text = "La contraseña debe tener mínimo 6 caracteres"
                errorMessage.visibility = View.VISIBLE
            }


        }
        val buttomNuevoUsuario = findViewById<Button>(R.id.buttomNuevoUsuario)
        buttomNuevoUsuario.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrarUsuario::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun iraRegistrarUsuario(view: View) {

        // Si el login es exitoso, ir a la siguiente actividad
        val intent = Intent(this@LoginActivity, RegistrarUsuario::class.java)
        startActivity(intent)
        finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso

    }
}