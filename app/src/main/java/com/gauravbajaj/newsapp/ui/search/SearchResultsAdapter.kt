package com.gauravbajaj.newsapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.databinding.ItemSearchResultBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A [RecyclerView.Adapter] implementation that displays a list of [Article] objects in a search results list.
 *
 * This adapter handles the display of news articles in a scrollable list, including:
 * - Article thumbnails (loaded asynchronously using Glide)
 * - Article titles and descriptions
 * - Source information and publication dates
 *
 * @constructor Creates a new instance of the adapter with Dagger injection.
 *
 * @see RecyclerView.Adapter
 * @see Article
 */
class SearchResultsAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    /**
     * Mutable list of articles to be displayed in the RecyclerView.
     */
    private val articles = mutableListOf<Article>()

    /**
     * Optional callback to be invoked when an article item is clicked.
     */
    private var onItemClickListener: ((Article) -> Unit)? = null

    /**
     * Sets a listener to be invoked when an article item is clicked.
     *
     * @param listener A lambda that will be called with the clicked [Article].
     *                 Pass `null` to clear the existing listener.
     */
    fun setOnItemClickListener(listener: ((Article) -> Unit)?) {
        onItemClickListener = listener
    }

    /**
     * Updates the list of articles to be displayed.
     *
     * This method replaces the current list of articles with the provided list and
     * notifies any registered observers that the data set has changed.
     *
     * @param newList The new list of articles to display. If empty, the adapter will show no items.
     */
    fun submitList(newList: List<Article>) {
        articles.clear()
        articles.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Called when RecyclerView needs a new [SearchResultViewHolder] of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new [SearchResultViewHolder] that holds a View with the item layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of articles in the adapter.
     */
    override fun getItemCount(): Int = articles.size

    /**
     * ViewHolder class for displaying a single article item in the RecyclerView.
     *
     * @property binding The ViewBinding instance for the item view.
     */
    inner class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.let { it(articles[position]) }
                }
            }
        }

        /**
         * Binds an [Article] to this ViewHolder's views.
         *
         * @param article The article data to bind to the views.
         */
        fun bind(article: Article) {
            with(binding) {
                // Set article title and description
                textViewTitle.text = article.title ?: ""
                textViewDescription.text = article.description ?: ""
                textViewSource.text = article.source?.name ?: ""

                // Format and display the published date
                article.publishedAt?.let { dateString ->
                    try {
                        val inputFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                        val date = inputFormat.parse(dateString)
                        textViewDate.text = date?.let { outputFormat.format(it) } ?: ""
                    } catch (e: Exception) {
                        textViewDate.text = ""
                    }
                } ?: run {
                    textViewDate.text = ""
                }

                // Load article image using Glide with error and placeholder handling
                article.urlToImage?.let { imageUrl ->
                    Glide.with(imageViewThumbnail)
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(imageViewThumbnail)
                } ?: imageViewThumbnail.setImageResource(R.drawable.ic_placeholder)
            }
        }
    }
}