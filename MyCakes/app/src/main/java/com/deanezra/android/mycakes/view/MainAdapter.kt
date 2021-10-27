package com.deanezra.android.mycakes.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deanezra.android.mycakes.R
import com.deanezra.android.mycakes.databinding.AdapterCakeBinding
import com.deanezra.android.mycakes.models.Cake
import com.squareup.picasso.Picasso

class MainAdapter: RecyclerView.Adapter<MainViewHolder>() {

    var cakes = mutableListOf<Cake>()

    fun setCakeList(cakes: List<Cake>) {
        this.cakes = cakes.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterCakeBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cake = cakes[position]
        holder.binding.title.text = cake.title

        Picasso.get()
            .load(cake.image)
            .placeholder(R.drawable.ic_cake_slice)
            .error(R.drawable.ic_cake_slice)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return cakes.size
    }
}

class MainViewHolder(val binding: AdapterCakeBinding) : RecyclerView.ViewHolder(binding.root) {

}