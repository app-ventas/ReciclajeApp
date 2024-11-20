package com.kotlin.mobile.reciclajeapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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
        auth = Firebase.auth

        // Configura el Spinner
        configurarSpinner()

        configurarValidaciones()


        val registrarButton = findViewById<Button>(R.id.btn_register)
        registrarButton.setOnClickListener { v: View? -> onRegisterClick() }
}

    private fun configurarValidaciones() {

        val editTextName = findViewById<EditText>(R.id.et_name)
        // Filtro que permite solo letras (incluye acentos y espacios si deseas)
        val textOnlyFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$") // Letras y espacios
            if (source.isEmpty() || source.matches(regex)) {
                source // Permitir la entrada
            } else {
                "" // Bloquear la entrada
            }
        }

        // Asignar el filtro al EditText
        editTextName.filters = arrayOf(textOnlyFilter)



        val editTextCiudad = findViewById<EditText>(R.id.et_ciudad)
        // Filtro que permite solo letras (incluye acentos y espacios si deseas)
        val textOnlyCiudadFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$") // Letras y espacios
            if (source.isEmpty() || source.matches(regex)) {
                source // Permitir la entrada
            } else {
                "" // Bloquear la entrada
            }
        }

        // Asignar el filtro al EditText
        editTextCiudad.filters = arrayOf(textOnlyCiudadFilter)

        val editTextnDocumento = findViewById<EditText>(R.id.et_nDocumento)

        editTextnDocumento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Validar longitud mínima
                if (s != null && s.length < 6) {
                    editTextnDocumento.error = "Debe tener al menos 6 dígitos"
                }
            }
        })
    }

    fun onRegisterClick() {
    // Obtener las vistas del layout
    val nombreEditText = findViewById<EditText>(R.id.et_name)
    val nDocumentoEditText = findViewById<EditText>(R.id.et_nDocumento)
    val ciudadEditText = findViewById<EditText>(R.id.et_ciudad)
    val spinnerRol: Spinner = findViewById(R.id.spinnerRol)
    val tvSpinnerError: TextView = findViewById(R.id.tvSpinnerError)
    val emailEditText = findViewById<EditText>(R.id.et_email)
    val passwordEditText = findViewById<EditText>(R.id.et_password)
    val passwordVerifyEditText = findViewById<EditText>(R.id.et_password_verify)


    val nombre: String = nombreEditText.getText().toString().trim()
    val nDocumento: String = nDocumentoEditText.getText().toString().trim()
    val ciudad: String = ciudadEditText.getText().toString().trim()
    val rolSelected = spinnerRol.selectedItem.toString()

    val email: String = emailEditText.getText().toString().trim()
    val password: String = passwordEditText.getText().toString()
    val passwordVerify: String = passwordVerifyEditText.getText().toString()

    if (nombre.isEmpty()) {
        nombreEditText.setError("El nombre es obligatorio")
        nombreEditText.requestFocus()
        return
    }
    if (nDocumento.isEmpty()) {
        nDocumentoEditText.setError("El número de documento es obligatorio")
        nDocumentoEditText.requestFocus()
        return
    }
    if (ciudad.isEmpty()) {
        ciudadEditText.setError("La ciudad es obligatoria")
        ciudadEditText.requestFocus()
        return
    }

    if (ciudad.isEmpty()) {
        ciudadEditText.setError("La ciudad es obligatoria")
        ciudadEditText.requestFocus()
        return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        emailEditText.setError("Introduce un correo electrónico válido")
        emailEditText.requestFocus()
        return
    }

    if (rolSelected == "Rol") { // Compara con el texto inicial
        tvSpinnerError.visibility = View.VISIBLE // Muestra el error
        spinnerRol.requestFocus() // Establece el foco en el Spinner
        return
    } else {
        tvSpinnerError.visibility = View.GONE // Oculta el error si es válido
    }

    if (password.isEmpty()) {
        passwordEditText.setError("La contraseña es obligatoria")
        passwordEditText.requestFocus()
        return
    }

    if (password.length < 6) {
        passwordEditText.setError("La contraseña debe tener mínimo 6 caracteres")
        passwordEditText.requestFocus()
        return
    }

    if (passwordVerify.isEmpty()) {
        passwordVerifyEditText.setError("La verificación contraseña es obligatoria")
        passwordVerifyEditText.requestFocus()
        return
    }

    if (password != passwordVerify) {
        passwordVerifyEditText.setError("Las contraseñas no coinciden")
        passwordVerifyEditText.requestFocus()
        return
    }

    // Lógica para registrar al usuario en firebase
    registrarUsuarioFirebase(nombre, ciudad, nDocumento, rolSelected, password, email);
}

private fun registrarUsuarioFirebase(nombre: String, ciudad: String, nDocumento: String, rol: String, password: String, email: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser

                val usuario = Usuario(
                    uid = user?.uid ?: "",
                    nombre = nombre,
                    email = email,
                    ciudad = ciudad,
                    numeroDocumento = nDocumento,
                    rol = rol,
                    saldo = 0
                )

                UsuarioServicioFirebase.crearUsuario(usuario, object : GenericCallback<Usuario?> {
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
        val tvSpinnerError: TextView = findViewById(R.id.tvSpinnerError)


        spinnerRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvSpinnerError.visibility = View.GONE // Oculta el error si es válido
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                tvSpinnerError.visibility = View.GONE // Oculta el error si es válido
            }
        }

    }

}
