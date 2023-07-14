package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.vp.list.ListAdapter.ListViewHolder
import com.vp.list.databinding.ItemListBinding
import com.vp.list.model.ListItem

class ListAdapter: Adapter<ListViewHolder>() {
    companion object {

        private const val NO_IMAGE = "N/A"
    }

    private var listItems: MutableList<ListItem> = mutableListOf()

    private lateinit var binding: ItemListBinding

    private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
        override fun onItemClick(imdbID: String?) {
            //DO NOTHING
        }
    }

    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

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
            GlideApp
                .with(holder.binding.poster)
                .load(listItem.poster)
                .override(300*density.toInt(), 600*density.toInt())
                .into(holder.binding.poster)
        } else {
            holder.binding.poster.setImageResource(R.drawable.placeholder)
        }
    }

    fun setItems(listItems: List<ListItem>?) {
        this.listItems = listItems?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    fun clearItems() {
        listItems.clear()
    }

    override fun getItemCount() = listItems.size

    inner class ListViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }
}