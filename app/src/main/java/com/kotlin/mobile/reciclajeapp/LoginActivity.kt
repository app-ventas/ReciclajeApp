package com.kotlin.mobile.reciclajeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.kotlin.mobile.reciclajeapp.services.LoginRequest
import com.kotlin.mobile.reciclajeapp.services.RetrofitServicesFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val service = RetrofitServicesFactory.create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        // set on-click listener
        buttonLogin.setOnClickListener {
            val email = findViewById<TextView>(R.id.email).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email = email, pin = password)

                lifecycleScope.launch {
                    try {
                        // Llamada a la API de login
                        val loginResponse = service.login(loginRequest)

                        // Validar la respuesta del login

                        println("Token: ${loginResponse.token}")

                        if(loginResponse.state == "A"){

                            // Si el login es exitoso, ir a la siguiente actividad
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso
                        }

                    } catch (e: Exception) {
                        println("Error en la solicitud: ${e.message}")
                    }
                }
            }


        }
        val buttomNuevoUsuario = findViewById<Button>(R.id.buttomNuevoUsuario)
        buttomNuevoUsuario.setOnClickListener {
            val intent = Intent(this@LoginActivity, NewUserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}