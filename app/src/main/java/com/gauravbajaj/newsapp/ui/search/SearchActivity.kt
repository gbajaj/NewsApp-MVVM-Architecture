package com.gauravbajaj.newsapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.databinding.ActivitySearchBinding
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.base.UiSearchState
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The SearchActivity provides a search interface for finding news articles.
 *
 * This activity allows users to:
 * - Enter search queries in a search view
 * - View search results in a scrollable list
 * - See loading states and error messages
 * - Navigate back to the previous screen
 *
 * It observes the [SearchViewModel] for state changes and updates the UI accordingly.
 *
 * @constructor Creates a new instance of SearchActivity.
 * @property viewModel The ViewModel that manages the search functionality
 * @property adapter The adapter for displaying search results
 *
 * @see SearchViewModel
 * @see SearchResultsAdapter
 */
class SearchActivity : AppCompatActivity() {
    
    /**
     * The ViewModel that manages the search functionality and business logic.
     */
    @Inject
    lateinit var viewModel: SearchViewModel
    
    /**
     * The adapter that manages the display of search results in the RecyclerView.
     */
    @Inject
    lateinit var adapter: SearchResultsAdapter
    
    /**
     * View binding instance for the activity's layout.
     */
    private lateinit var binding: ActivitySearchBinding
    
    /**
     * Configures the UI components including the search view and RecyclerView.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }
    
    /**
     * Sets up the toolbar with a back button and title.
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.search_news)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    
    /**
     * Configures the RecyclerView with a LinearLayoutManager and sets the adapter.
     */
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            this.adapter = this@SearchActivity.adapter
        }
        adapter.setOnItemClickListener { article ->
            CustomTabsHelper.launchUrl(this, article.url)
        }
    }
    
    /**
     * Sets up the search view with a QueryTextListener to handle search queries.
     * Triggers a search when the user submits a query.
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchNews(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    
    /**
     * Initiates a search for news articles with the given query.
     *
     * @param query The search query string entered by the user
     */
    private fun searchNews(query: String) {
        if (query.isNotBlank()) {
            viewModel.searchNews(query)
        }
    }
    
    /**
     * Observes the ViewModel's state and updates the UI accordingly.
     * Handles different states like Loading, Success, Error, and Empty states.
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiSearchState.Loading -> showLoading(true)
                    is UiSearchState.Success -> {
                        showLoading(false)
                        state.data.let { articles ->
                            if (articles.isNotEmpty()) {
                                adapter.submitList(articles)
                            } else {
                                showMessage(getString(R.string.no_results_found))
                            }
                        }
                    }
                    is UiSearchState.Error -> {
                        showLoading(false)
                        showMessage(state.message ?: getString(R.string.error_loading_news))
                    }
                    is UiSearchState.Empty -> {
                        showLoading(false)
                    }
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Shows or hides the loading indicator.
     *
     * @param isLoading Boolean indicating whether to show the loading indicator
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
    /**
     * Displays a toast message to the user.
     *
     * @param message The message to be displayed
     */
    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
    
    /**
     * This method initializes the Dagger component and injects the activity.
     */
    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }
}
