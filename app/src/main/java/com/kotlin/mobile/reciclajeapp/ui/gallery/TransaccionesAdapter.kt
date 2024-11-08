package com.kotlin.mobile.reciclajeapp.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.mobile.reciclajeapp.R
import com.kotlin.mobile.reciclajeapp.model.Transaccion

class TransaccionesAdapter(private val transaccionesList : ArrayList<Transaccion>): RecyclerView.Adapter<TransaccionesAdapter.TransaccionesHolder>() {

    class TransaccionesHolder(itemView: View)  : RecyclerView.ViewHolder(itemView){
        val fecha : TextView = itemView.findViewById(R.id.textView)
        val descripcion : TextView = itemView.findViewById(R.id.textView2)
        val imageView : ImageView = itemView.findViewById(R.id.image_TransaccionView);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionesHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_transacciones, parent, false)
        return TransaccionesHolder(itemView)

    }

    override fun getItemCount(): Int {
        return transaccionesList.size
    }

    override fun onBindViewHolder(holder: TransaccionesHolder, position: Int) {
        val currentItem = transaccionesList[position]
        holder.fecha.text = currentItem.fecha
        holder.descripcion.text = currentItem.descripcion

        // Cargar la imagen desde una URL usando Glide
        Glide.with(holder.itemView.context)
            .load("https://res.cloudinary.com/dtzmxhp9p/image/upload/v1728263996/newProduct_mjhvym.png")
            .into(holder.imageView)

    }

}