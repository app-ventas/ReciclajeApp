package com.kotlin.mobile.reciclajeapp.ui.puntoReciclaje

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.model.Transaccion
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase
import com.kotlin.mobile.reciclajeapp.services.TransaccionesServicioFirebase
import java.text.SimpleDateFormat
import java.util.Date

class NuevoPuntoReciclajeFragment : Fragment() {

    companion object {
        fun newInstance() = NuevoPuntoReciclajeFragment()
    }

    private var idTipoMaterial: String = ""
    private lateinit var listaMateriales: List<Material?>
    private lateinit var spinnerMaterial: Spinner
    private lateinit var spinnerUnidadMedida: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_nuevo_punto_reciclaje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarListaMateriales(view)


        val btnRegistrar: Button = view.findViewById(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {

            // Obtener usuario de la caché
            val usuarioCache = UsuarioCache.obtenerUsuarioCache(requireContext())

            if(usuarioCache != null) {

                val spinnerUnidadMedida = view.findViewById<Spinner>(R.id.spinnerUnidadMedida)
                val cantidadEditText = view.findViewById<EditText>(R.id.etCantidad)
                val descripcionEditText = view.findViewById<EditText>(R.id.etDescripcion)

                // Obtener la fecha y hora actual
                val fechaActual = Date()
                val formato = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val fechaCreacion = formato.format(fechaActual)


                var cantidad = 0;
                if(cantidadEditText.text.length > 0) {
                    cantidad = cantidadEditText.text.toString().toInt()
                }

                val unidadMedida = spinnerUnidadMedida.selectedItem.toString()
                val descripcion = descripcionEditText.text.toString()

                // Crear el objeto Transaccion
                val transaccion = Transaccion(
                    uidDonante = usuarioCache.uid,
                    descripcion = descripcion,
                    fechaCreacion = fechaCreacion, // Fecha en formato correcto
                    cantidad = cantidad,
                    idTipoMaterial = idTipoMaterial,
                    puntosGanados = 0,
                    unidadMedida = unidadMedida
                )

                TransaccionesServicioFirebase.crearTransaccionDonante(transaccion,object : GenericCallback<Transaccion> {
                    override fun onSuccess(result: Transaccion) {
                        // Mostrar mensaje de éxito
                        Toast.makeText(requireContext(), "Punto de reciclaje guardado correctamente", Toast.LENGTH_SHORT).show()

                        // Crear un Bundle con un indicador de desplazamiento al final
                        val bundle = Bundle()
                        bundle.putBoolean("scrollToEnd", true)

                        // Navegar a ListaMaterialesFragment y pasar el parametro bundle
                        findNavController().navigate(R.id.nav_home, bundle)
                    }

                    override fun onError(error: String) {
                        // Manejo del error
                        println("Error al crear la trasacción: $error")
                    }
                })

            }
        }
    }

    private fun cargarListaMateriales(view: View) {
        spinnerMaterial = view.findViewById(R.id.spinnerMaterial)
        spinnerUnidadMedida = view.findViewById(R.id.spinnerUnidadMedida)

        // Lista de valores para el Spinner
        val medidas = listOf("GR", "KL", "TOMx", "ltr")

        val adaptador = ArrayAdapter(
            requireContext(), // Contexto
            android.R.layout.simple_spinner_item, // Diseño simple predefinido
            medidas // Lista de datos
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnidadMedida.adapter = adaptador

        // Llama a consultarMateriales y maneja el resultado asíncrono
        MaterialServicioFirebase.consultarMateriales(object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {

                listaMateriales = result
                // Procesa la lista de Material obtenida del resultado
                val nombresMateriales = result.filterNotNull().map { it.nombre } // Convierte a una lista de nombres

                // Crea un ArrayAdapter usando el layout simple_spinner_item y la lista de nombres
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresMateriales)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Asigna el adaptador al Spinner
                spinnerMaterial.adapter = adapter


                //Cargar lista en spinner
                val spinnerMaterial = view.findViewById<Spinner>(R.id.spinnerMaterial)
                spinnerMaterial.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Obtén el ID del material seleccionado
                        val materialSeleccionado = listaMateriales.getOrNull(position)

                        if (materialSeleccionado != null) {
                            idTipoMaterial = materialSeleccionado.uid.toString()
                            println("ID del material seleccionado: ${idTipoMaterial}")
                            println("Nombre del material seleccionado: ${materialSeleccionado.nombre}")
                        } else {
                            println("El material seleccionado es nulo o inválido.")
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Manejo si no se selecciona nada
                    }
                }

            }

            override fun onError(error: String) {
                // Manejo del error
                println("Error al consultar materiales: $error")
            }
        })
    }

}