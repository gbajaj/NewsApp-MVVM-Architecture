package com.gauravbajaj.newsapp.ui.newslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.databinding.ActivityNewsListBinding
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: NewsListViewModel
    
    @Inject
    lateinit var adapter: NewsListAdapter
    
    private lateinit var binding: ActivityNewsListBinding

    companion object {
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_COUNTRY = "extra_country"
        private const val EXTRA_LANGUAGE = "extra_language"

        fun start(
            context: android.content.Context,
            source: String? = null,
            country: String? = null,
            language: String? = null
        ) {
            val intent = android.content.Intent(context, NewsListActivity::class.java).apply {
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_COUNTRY, country)
                putExtra(EXTRA_LANGUAGE, language)
            }
            context.startActivity(intent)
        }
    }

    private var selectedSource: String? = null
    private var selectedCountry: String? = null
    private var selectedLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize from intent extras
        selectedSource = intent.getStringExtra(EXTRA_SOURCE)
        selectedCountry = intent.getStringExtra(EXTRA_COUNTRY)
        selectedLanguage = intent.getStringExtra(EXTRA_LANGUAGE)


        // Setup adapter click listener
        adapter.onArticleClick = { article ->
            article.url.let { url ->
                CustomTabsHelper.launchUrl(this, url)
            }
        }

        setupToolbar()
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        // Initial load
        loadNews()
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupRecyclerView() {
        binding.rvNewsList.apply {
            layoutManager = LinearLayoutManager(this@NewsListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@NewsListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = this@NewsListActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadNews()
        }
    }



    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            if (!binding.swipeRefreshLayout.isRefreshing) {
                                showLoading(true)
                            }
                        }
                        is UiState.Success -> {
                            showLoading(false)
                            if (state.data.isEmpty()) {
                                showEmptyState(true)
                                showError(getString(R.string.no_results_found))
                            } else {
                                showEmptyState(false)
                                adapter.submitList(state.data)
                            }
                        }
                        is UiState.Error -> {
                            showLoading(false)
                            showError(state.message ?: getString(R.string.error_generic))
                        }
                    }
                }
            }
        }
    }

    private fun loadNews(
        source: String? = selectedSource,
        country: String? = selectedCountry,
        language: String? = selectedLanguage
    ) {
        viewModel.loadNews(source, country, language)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.emptyView.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvNewsList.visibility = if (isEmpty) View.GONE else View.VISIBLE
        
        if (isEmpty) {
            val emptyImageView = binding.emptyView.root.findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.ivEmptyState)
            val emptyTextView = binding.emptyView.root.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tvEmptyMessage)
            
            emptyImageView.setImageResource(R.drawable.ic_search)
            emptyTextView.setText(R.string.no_results_found)
        }
    }

    private fun showError(message: String) {
        if (!message.isNullOrEmpty()) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }
}
