package com.example.dacs.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs.Data.SearchData
import com.example.dacs.R
import java.util.*

class SearchAdapter (private val pmlist: List<SearchData>)  : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){
    private var listener: OnItemClickListener? = null
    private var searchListFiltered: List<SearchData> = pmlist

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    inner  class  ViewHolder(itemView: View, clickListener: OnItemClickListener?) : RecyclerView.ViewHolder (itemView) {
        init {
            itemView.setOnClickListener {
                clickListener?.onItemClick(adapterPosition)
            }
        }
        private val title: TextView = itemView.findViewById(R.id.titleTextView)
        private val poster: ImageView = itemView.findViewById(R.id.posterImageView)
        fun bind(searchData: SearchData) {
            val titleText = searchData.title ?: searchData.name
            title.text = titleText
            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500/${searchData.poster_path}").into(poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val search = pmlist[position]
        holder.bind(search)
    }

    override fun getItemCount(): Int =
        pmlist.size


}