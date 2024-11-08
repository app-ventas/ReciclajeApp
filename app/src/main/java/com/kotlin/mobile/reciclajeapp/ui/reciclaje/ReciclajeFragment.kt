package com.kotlin.mobile.reciclajeapp.ui.reciclaje

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase

class ReciclajeFragment : Fragment() {

    companion object {
        fun newInstance() = ReciclajeFragment()
    }

    private lateinit var viewModel: ReciclajeViewModel
    private lateinit var spinnerMaterial: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reciclaje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Encuentra el Spinner en el layout
        spinnerMaterial = view.findViewById(R.id.spinnerMaterial)

        viewModel = ViewModelProvider(this).get(ReciclajeViewModel::class.java)

        // Llama a consultarMateriales y maneja el resultado asíncrono
        MaterialServicioFirebase.consultarMateriales(object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {
                // Procesa la lista de Material obtenida del resultado
                val nombresMateriales = result.filterNotNull().map { it.nombre } // Convierte a una lista de nombres

                // Crea un ArrayAdapter usando el layout simple_spinner_item y la lista de nombres
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresMateriales)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Asigna el adaptador al Spinner
                spinnerMaterial.adapter = adapter
            }

            override fun onError(error: String) {
                // Manejo del error
                println("Error al consultar materiales: $error")
            }
        })

    }

}