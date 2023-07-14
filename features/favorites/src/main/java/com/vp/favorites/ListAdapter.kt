package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.vp.favorites.ListAdapter.ListViewHolder
import com.vp.favorites.database.entities.PosterEntity
import com.vp.favorites.databinding.ItemListBinding

class ListAdapter: Adapter<ListViewHolder>() {
    companion object {

        private const val NO_IMAGE = "N/A"
    }

    private var listItems: MutableList<PosterEntity> = mutableListOf()

    private lateinit var binding: ItemListBinding

    private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
        override fun onItemClick(imdbID: String?) {
            //DO NOTHING
        }
    }

    interface OnItemClickListener {

        fun onItemClick(imdbID: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        if (listItem.poster != null && listItem.poster != NO_IMAGE) {
            val density = holder.binding.poster.resources.displayMetrics.density
            Glide
                .with(holder.binding.poster)
                .load(listItem.poster)
                .override(300*density.toInt(), 600*density.toInt())
                .into(holder.binding.poster)
        } else {
            holder.binding.poster.setImageResource(R.drawable.placeholder)
        }
    }

    fun setItems(listItems: List<PosterEntity>?) {
        this.listItems = listItems?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }
    override fun getItemCount() = listItems.size

    inner class ListViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root){

    }
}