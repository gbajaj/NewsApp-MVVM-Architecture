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
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import javax.inject.Inject

/**
 * Adapter for displaying a list of top headlines in a RecyclerView.
 * Handles displaying article information and opening article URLs in Custom Tabs.
 *
 * @constructor Creates a new instance of the adapter with Dagger injection.
 */
class TopHeadlinesAdapter @Inject constructor() :
    RecyclerView.Adapter<TopHeadlinesAdapter.HeadlineViewHolder>() {

    private val articles = mutableListOf<Article>()
    private var onItemClickListener: ((Article) -> Unit)? = null

    /**
     * Updates the list of articles to be displayed.
     *
     * @param newArticles The new list of articles to display
     */
    fun submitList(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    /**
     * Sets a listener to be invoked when an article item is clicked.
     *
     * @param listener A lambda that will be called with the clicked [Article].
     *                 Pass `null` to clear the existing listener.
     */
    fun setOnItemClickListener(listener: ((Article) -> Unit)?) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_headline, parent, false)
        return HeadlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size

    inner class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.let { it(articles[position]) }
                }
            }
        }
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