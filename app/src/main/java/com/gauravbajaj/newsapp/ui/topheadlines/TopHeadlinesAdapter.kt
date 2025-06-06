package com.gauravbajaj.newsapp.ui.topheadlines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article

class TopHeadlinesAdapter(
    private val articleList: ArrayList<Article>
) : RecyclerView.Adapter<TopHeadlinesAdapter.HeadlineViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun submitList(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_headline, parent, false)
        return HeadlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { }
    }

    override fun getItemCount(): Int = articles.size

    inner class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvSource: TextView = itemView.findViewById(R.id.tvSource)

        fun bind(article: Article) {
            // Load image with Glide
            article.urlToImage?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_article_placeholder)
                    .error(R.drawable.ic_article_placeholder)
                    .into(ivArticleImage)
            } ?: run {
                ivArticleImage.setImageResource(R.drawable.ic_article_placeholder)
            }

            // Set text fields
            tvTitle.text = article.title
            tvDescription.text = article.description ?: ""
            tvSource.text = article.source.name
        }
    }
}