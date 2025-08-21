package com.homeworks.finalexam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homeworks.finalexam.databinding.ItemCastBinding

class CastAdapter(private val items: MutableList<Cast>) : RecyclerView.Adapter<CastAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submit(list: List<Cast>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }

    inner class VH(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(c: Cast) {
            binding.tvName.text = c.name
            val url = c.profilePath?.let { "https://image.tmdb.org/t/p/w185$it" }
            if (url != null) Glide.with(binding.ivProfile).load(url).into(binding.ivProfile)
        }
    }
}


