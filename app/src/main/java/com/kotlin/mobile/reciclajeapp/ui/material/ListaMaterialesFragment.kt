package com.kotlin.mobile.reciclajeapp.ui.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase

class ListaMaterialesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MaterialAdapter
    private lateinit var materiales: List<Material>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_lista_materiales, container, false)

        // Inicializa el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewListaMateriales)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Configura el botón para navegar a otro fragmento
        //val btnNavegar = view.findViewById<Button>(R.id.btn_nuevoMaterialClk)
        //btnNavegar.setOnClickListener {
        //    findNavController().navigate(R.id.nav_crear_nuevo_material)
        //}

        // Obtener el indicador de desplazamiento al final desde los argumentos
        val scrollToEnd = arguments?.getBoolean("scrollToEnd", false) ?: false


        // Llama al servicio para obtener los materiales
        MaterialServicioFirebase.consultarMateriales(object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {
                // Configura el adapter con los datos obtenidos
                adapter = MaterialAdapter(result)
                recyclerView.adapter = adapter
                materiales = result.filterNotNull()

                // Desplazarse hasta el final solo si scrollToEnd es verdadero
                if (scrollToEnd) {
                    recyclerView.scrollToPosition(materiales.size - 1)
                }
            }

            override fun onError(error: String) {
                Toast.makeText(requireContext(), "Error al cargar materiales: $error", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}
