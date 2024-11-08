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
import com.kotlin.mobile.reciclajeapp.services.MaterialServicioFirebase

class GalleryFragment : Fragment() {

    private lateinit var adapter: TransaccionesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var transaccionesArrayList: ArrayList<Transaccion>

    private lateinit var materiales : List<Material?>
    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        MaterialServicioFirebase.consultarMateriales( object : GenericCallback<List<Material?>> {
            override fun onSuccess(result: List<Material?>) {
                materiales = result
            }

            override fun onError(error: String) {
                // Aquí manejas lo que pasa cuando hay un error
                println("Error al consultar usuario: $error")
            }
        })


        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery

        // Obtener usuario de la caché
        val usuarioCache = UsuarioCache.obtenerUsuarioCache(requireContext())

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inicializarLista() {
        transaccionesArrayList = arrayListOf<Transaccion>()

        for (i in 0 until 20) {
            val transaccion = Transaccion(
                "uidonante",
                "uidonante",
                "12 Jun 2014",
                "Material Plástico",
                "10:10am",
                i,
                1
                )
            transaccionesArrayList.add(transaccion)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarLista()

        val layoutManager  = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = TransaccionesAdapter(transaccionesArrayList)
        recyclerView.adapter = adapter
    }
}