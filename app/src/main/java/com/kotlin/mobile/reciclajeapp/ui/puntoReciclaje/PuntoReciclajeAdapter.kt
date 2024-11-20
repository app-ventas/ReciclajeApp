package com.kotlin.mobile.reciclajeapp.ui.puntoReciclaje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.Material

class PuntoReciclajeAdapter(private val materiales: List<Material?>) :
    RecyclerView.Adapter<PuntoReciclajeAdapter.MaterialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_material, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = materiales[position]
        holder.bind(material)
    }

    override fun getItemCount() = materiales.size

    class MaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.textMaterialNombre)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.textMaterialDescripcion)
        private val puntosTextView: TextView = itemView.findViewById(R.id.textMaterialPuntos)
        private val imageMaterialView: ImageView = itemView.findViewById(R.id.imageMaterialView)

        fun bind(material: Material?) {
            material?.let {
                nombreTextView.text = it.nombre ?: "Nombre no disponible"
                descripcionTextView.text = it.descripcion ?: "Descripción no disponible"
                puntosTextView.text = "Puntos: ${it.puntos ?: 0}"

                // Carga de la imagen usando Glide
                Glide.with(itemView.context)
                    .load(it.url) // URL de la imagen
                    .placeholder(R.drawable.materiales) // Imagen de carga por defecto
                    .into(imageMaterialView)
            }
        }
    }

}
