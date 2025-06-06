package com.gauravbajaj.newsapp.ui.news_sources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source

class NewsSourcesAdapter(private val sourceList: ArrayList<Source>) : ListAdapter<Source, NewsSourcesAdapter.NewsSourceViewHolder>(NewsSourceDiffCallback()) {

    var onItemClick: ((Source) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_source, parent, false)
        return NewsSourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsSourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSourceName: TextView = itemView.findViewById(R.id.tvSourceName)
        private val tvSourceDescription: TextView = itemView.findViewById(R.id.tvSourceDescription)
        private val tvSourceCategory: TextView = itemView.findViewById(R.id.tvSourceCategory)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(adapterPosition))
                }
            }
        }

        fun bind(source: Source) {
            tvSourceName.text = source.name
            tvSourceDescription.text = source.description
            tvSourceCategory.text = source.category?.replaceFirstChar { it.uppercase() } ?: ""
        }
    }
}

class NewsSourceDiffCallback : DiffUtil.ItemCallback<Source>() {
    override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
        return oldItem == newItem
    }
}
