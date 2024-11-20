package com.kotlin.mobile.reciclajeapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.databinding.FragmentGalleryBinding
import com.kotlin.mobile.reciclajeapp.model.GenericCallback
import com.kotlin.mobile.reciclajeapp.model.Material
import com.kotlin.mobile.reciclajeapp.model.Transaccion
import com.kotlin.mobile.reciclajeapp.model.Usuario
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase
import com.kotlin.mobile.reciclajeapp.services.TransaccionesServicioFirebase

class GalleryFragment : Fragment() {

    private lateinit var adapter: TransaccionesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var transaccionesArrayList: List<Transaccion>
    private lateinit var listaMateriales: List<Material?>

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery

        // Obtener usuario de la caché
        val usuarioCache = UsuarioCache.obtenerUsuarioCache(requireContext())

        if(usuarioCache !== null) {
            cargarTransacciones(root,usuarioCache)
        }


        // Observa el LiveData y actualiza el TextView
        galleryViewModel.text.observe(viewLifecycleOwner) { newText ->
            usuarioCache?.let { // Solo procede si usuarioCache no es null
                textView.text = "Saldo: $ ${it.saldo} puntos"
            } ?: run {
                // Manejo en caso de que usuarioCache sea null
                textView.text = "Usuario no encontrado"
            }
        }

        return root
    }

    private fun cargarTransacciones(view: View, usuarioCache: Usuario) {
        val uidDonante = usuarioCache.uid

        // Llama a consultarMateriales y maneja el resultado asíncrono por medio de una funcion callback
        MaterialServicioFirebase.consultarMateriales(object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {

                //Obtiene la lista de materiales
                listaMateriales = result

                TransaccionesServicioFirebase.consultarTransaccionPorDonador(uidDonante,
                    object : GenericCallback<List<Transaccion?>> {
                        override fun onSuccess(result: List<Transaccion?>) {
                            transaccionesArrayList = result as List<Transaccion>

                            // Crear un mapa para los materiales usando el UID como clave
                            val materialesMap =
                                listaMateriales.filterNotNull().associateBy { it.uid }

                            // Asignar el material correspondiente a cada transacción
                            for (transaccion in transaccionesArrayList) {
                                if (transaccion != null) {
                                    transaccion.material = materialesMap[transaccion.idTipoMaterial]
                                }
                            }

                            val layoutManager = LinearLayoutManager(context)
                            recyclerView = view.findViewById(R.id.recyclerView)
                            recyclerView.layoutManager = layoutManager
                            recyclerView.setHasFixedSize(true)
                            adapter = TransaccionesAdapter(transaccionesArrayList)
                            recyclerView.adapter = adapter
                        }

                        override fun onError(error: String) {
                            // Aquí manejas lo que pasa cuando hay un error
                            println("Error al consultar transacciones para el usuario: $error")
                        }
                    })
            }
            override fun onError(error: String) {
                // Manejo del error
                println("Error al consultar la lista de materiales: $error")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}