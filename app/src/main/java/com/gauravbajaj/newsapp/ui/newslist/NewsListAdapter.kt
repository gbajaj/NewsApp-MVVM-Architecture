package com.gauravbajaj.newsapp.ui.newslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.utils.formatDate

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private val articles = mutableListOf<Article>()
    var onArticleClick: ((Article) -> Unit)? = null

    fun submitList(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_article_image)
        private val titleView: TextView = itemView.findViewById(R.id.tv_article_title)
        private val sourceView: TextView = itemView.findViewById(R.id.tv_article_source)
        private val dateView: TextView = itemView.findViewById(R.id.tv_article_date)
        private val descriptionView: TextView = itemView.findViewById(R.id.tv_article_description)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onArticleClick?.invoke(articles[adapterPosition])
                }
            }
        }

        fun bind(article: Article) {
            titleView.text = article.title
            sourceView.text = article.source?.name ?: ""
            dateView.text = article.publishedAt?.let { formatDate(it) } ?: ""
            descriptionView.text = article.description ?: ""
            
            article.urlToImage?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            } ?: run {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }
}
