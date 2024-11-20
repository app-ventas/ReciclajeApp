package com.kotlin.mobile.reciclajeapp.ui.puntoReciclaje

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.model.Transaccion
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase
import com.kotlin.mobile.reciclajeapp.services.TransaccionesServicioFirebase
import com.kotlin.mobile.reciclajeapp.ui.gallery.TransaccionesAdapter
import com.kotlin.mobile.reciclajeapp.ui.material.MaterialAdapter

class ListaPuntosReciclajeFragment : Fragment() {

    private lateinit var listaMateriales: List<Material?>
    private lateinit var recyclerViewTransaction: RecyclerView
    private lateinit var recyclerViewMaterial: RecyclerView
    private lateinit var adapterRecycle: MaterialAdapter

    private lateinit var transaccionesArrayList: List<Transaccion?>
    private lateinit var uidDonador: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val usuarioCache = UsuarioCache.obtenerUsuarioCache(requireContext())
        if(usuarioCache!=null) {

            uidDonador = usuarioCache.uid

            // Llama al servicio para obtener los materiales
            MaterialServicioFirebase.consultarMateriales(object : GenericCallback<List<Material?>> {
                override fun onSuccess(result: List<Material?>) {

                    listaMateriales = result

                    // Llama a consultarMateriales y maneja el resultado asíncrono
                    TransaccionesServicioFirebase.consultarTransaccionPorDonador(uidDonador, object : GenericCallback<List<Transaccion?>> {
                        override fun onSuccess(result: List<Transaccion?>) {
                            transaccionesArrayList = result as List<Transaccion>

                            // Crear un mapa para los materiales usando el UID como clave
                            val materialesMap = listaMateriales.filterNotNull().associateBy { it.uid }

                            // Asignar el material correspondiente a cada transacción
                            for (transaccion in transaccionesArrayList) {
                                if(transaccion != null){
                                    transaccion.material = materialesMap[transaccion.idTipoMaterial]

                                }
                            }

                            val view = requireView()
                            val layoutManager  = LinearLayoutManager(context)
                            recyclerViewTransaction = view.findViewById(R.id.recyclerViewPuntosReciclaje)
                            recyclerViewTransaction.layoutManager = layoutManager
                            recyclerViewTransaction.setHasFixedSize(true)
                            var adapter = TransaccionesAdapter(transaccionesArrayList as List<Transaccion>)
                            recyclerViewTransaction.adapter = adapter
                        }

                        override fun onError(error: String) {
                            // Manejo del error
                            println("Error al consultar la lista de transacciones: $error")
                        }
                    })

                }

                override fun onError(error: String) {
                    Toast.makeText(requireContext(), "Error al cargar materiales: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return inflater.inflate(R.layout.fragment_lista_puntos_reciclaje, container, false)
    }

}