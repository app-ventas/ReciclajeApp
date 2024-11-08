package com.kotlin.mobile.reciclajeapp.ui.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase

class CrearNuevoMaterialFragment : Fragment() {

    private lateinit var materiales : List<Material?>

    companion object {
        fun newInstance() = CrearNuevoMaterialFragment()
    }

    private lateinit var viewModel: CrearNuevoMaterialViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        MaterialServicioFirebase.consultarMateriales( object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {
                materiales = result
            }

            override fun onError(error: String) {
                // Aquí manejas lo que pasa cuando hay un error
                println("Error al consultar usuario: $error")
            }
        })

        return inflater.inflate(R.layout.fragment_crear_nuevo_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CrearNuevoMaterialViewModel::class.java)


        // Encuentra el botón y configura el click listener
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar)

        btnGuardar.setOnClickListener {
            // Aquí va la lógica al hacer clic en el botón
            guardarMaterial()
        }

    }

    // Implementa la lógica para guardar el material
    private fun guardarMaterial() {

        val nombre = view?.findViewById<EditText>(R.id.input_nombre)?.text.toString()
        val url = view?.findViewById<EditText>(R.id.input_url)?.text.toString()
        val descripcion = view?.findViewById<EditText>(R.id.input_descripcion)?.text.toString()
        val puntos = view?.findViewById<EditText>(R.id.input_puntos)?.text.toString().toIntOrNull() ?: 0

        // Crear un objeto Material o realizar alguna acción con los datos obtenidos

        val nuevoMaterial = Material(nombre = nombre, url = url, puntos = puntos, descripcion = descripcion)
        MaterialServicioFirebase.crearMaterial(nuevoMaterial, object : GenericCallback<Material?> {
            override fun onSuccess(result: Material?) {
                // Mostrar mensaje de éxito
                Toast.makeText(requireContext(), "Material guardado correctamente", Toast.LENGTH_SHORT).show()

                // Crear un Bundle con un indicador de desplazamiento al final
                val bundle = Bundle()
                bundle.putBoolean("scrollToEnd", true)

                // Navegar a ListaMaterialesFragment y pasar el parametro bundle
                findNavController().navigate(R.id.nav_lista_materiales, bundle)
            }

            override fun onError(error: String) {
                Toast.makeText(requireContext(), "Error al guardar material: $error", Toast.LENGTH_SHORT).show()
            }
        })



    }

}