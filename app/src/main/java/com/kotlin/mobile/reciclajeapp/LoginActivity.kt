package com.kotlin.mobile.reciclajeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Usuario
import com.kotlin.mobile.reciclajeapp.services.UsuarioServicioFirebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Firebase Auth
        auth = Firebase.auth

        //val service = RetrofitServicesFactory.create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvOlvidoContrasenia: TextView = findViewById(R.id.tv_olvido_contrasenia)
        val text = "Olvidaste contraseña"
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        tvOlvidoContrasenia.text = spannableString

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
                            val uid = user?.uid
                            if (uid != null) {

                                UsuarioServicioFirebase.consultarUsuario(uid, object : GenericCallback<Usuario?> {
                                    override fun onSuccess(usuarioFirebase: Usuario?) {

                                        // Si el login es exitoso, ir a la siguiente actividad
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)

                                        usuarioFirebase?.let {
                                            intent.putExtra("usuario", it) // Agrega el objeto Usuario al Intent
                                        }
                                        startActivity(intent)
                                        finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso
                                    }

                                    override fun onError(error: String) {
                                        // Aquí manejas lo que pasa cuando hay un error
                                        println("Error al consultar usuario: $error")
                                    }
                                })
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext,"Autenticación falló.",Toast.LENGTH_SHORT,).show()
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
        val buttomNuevoUsuario = findViewById<Button>(R.id.buttonNuevoUsuario)
        buttomNuevoUsuario.setOnClickListener {
            iraRegistrarUsuario()
        }
    }

    fun iraRegistrarUsuario() {
        // Si el login es exitoso, ir a la siguiente actividad
        val intent = Intent(this@LoginActivity, RegistrarUsuario::class.java)
        startActivity(intent)
        finish() // Opcional: Finaliza la actividad actual para que no se pueda volver con el botón de retroceso
    }
}