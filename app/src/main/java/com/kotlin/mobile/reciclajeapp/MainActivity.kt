package com.kotlin.mobile.reciclajeapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.kotlin.mobile.reciclajeapp.databinding.ActivityMainBinding
import com.kotlin.mobile.reciclajeapp.model.Usuario

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: Usuario
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuarioCache = UsuarioCache.obtenerUsuarioCache(this)

        if(usuarioCache == null) {

            // Recuperar el Usuario del Intent
            val usuarioLogeado = obtenerUsuario()
            if( usuarioLogeado == null ) {
                // Si no hay usuario autenticado, redireccionar al Login
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return;
            }
            else {
                this.usuario = usuarioLogeado;
                guardarUsuarioCache(baseContext,usuarioLogeado)
            }

        }
        else {
            this.usuario = usuarioCache
            // Cambiar el nombre en el encabezado de navegación
            val navView: NavigationView = binding.navView
            val headerView = navView.getHeaderView(0) // Obtener la vista del encabezado
            val bienvenidaUsuarioTextView: TextView = headerView.findViewById(R.id.tv_bienvenida_usuario)
            bienvenidaUsuarioTextView.text = "Bienvenido, ${usuario.nombre} !" // Establecer el texto con un saludo

        }

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Crea un nuevo material para recolectar", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show()

            val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                ?.findNavController()
            navController?.navigate(R.id.nav_nuevo_punto_reciclaje)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_lista_materiales
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Añadir el listener para manejar la selección de ítems del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_cerrar_sesion -> {
                    cerrarSesion()
                    true
                }
                else -> {
                    // Si selecciona otra opción, navegamos como siempre
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    handled
                }
            }
        }
    }

    // Recuperar el objeto Usuario del Intent
    private fun obtenerUsuario() : Usuario? {

        val usuarioSerializable = intent.getSerializableExtra("usuario") as? Usuario

        // Verifica si el usuario es nulo antes de asignarlo
        if (usuarioSerializable != null) {

            guardarUsuarioCache(baseContext,usuarioSerializable)
            this.usuario = usuarioSerializable

            // Configurar el NavigationView
            val navView: NavigationView = binding.navView
            val headerView = navView.getHeaderView(0) // Obtener la vista del encabezado

            // Cambiar el nombre en el encabezado de navegación
            val bienvenidaUsuarioTextView: TextView = headerView.findViewById(R.id.tv_bienvenida_usuario)
            bienvenidaUsuarioTextView.text = "Bienvenido, ${usuario.nombre} !" // Establecer el texto con un saludo

        }

        return usuarioSerializable;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun cerrarSesion() {
        // Crear un cuadro de diálogo de confirmación
        AlertDialog.Builder(this)
            .setTitle("¿Estás seguro de que deseas cerrar sesión?")
            .setMessage("Confirma para cerrar de sesión")
            .setPositiveButton("Sí") { dialog, _ ->
                // Cerrar sesión si el usuario confirma
                FirebaseAuth.getInstance().signOut()

                //borrar cache
                UsuarioCache.borrarUsuarioCache(this)

                // Redirigir a la pantalla de inicio de sesión
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Finaliza la actividad actual para que no se pueda volver con el botón de atrás
            }
            .setNegativeButton("No") { dialog, _ ->
                // Cierra el diálogo si el usuario cancela
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun guardarUsuarioCache(context: Context, usuario: Usuario) {
        val sharedPreferences = context.getSharedPreferences("mi_app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val usuarioJson = gson.toJson(usuario)
        editor.putString("usuario", usuarioJson)
        editor.apply() // o editor.commit()
    }

}