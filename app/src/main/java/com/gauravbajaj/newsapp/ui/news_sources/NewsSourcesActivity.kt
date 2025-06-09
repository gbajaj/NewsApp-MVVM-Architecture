package com.gauravbajaj.newsapp.ui.news_sources

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.databinding.ActivityNewsSourcesBinding
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class NewsSourcesActivity : AppCompatActivity() {

    private val viewModel by viewModels<NewsSourcesViewModel>()
    lateinit var adapter: NewsSourcesAdapter

    private lateinit var binding: ActivityNewsSourcesBinding
    private var errorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        loadNewsSources()

        binding.retryButton.setOnClickListener {
            loadNewsSources()
        }
    }

    /**
     * Sets up the toolbar with a back navigation button.
     * The toolbar is configured as the app's action bar.
     * A click listener is set on the navigation button to handle back navigation.
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun setupRecyclerView() {
        adapter = NewsSourcesAdapter(ArrayList())
        binding.rvNewsSources.apply {
            layoutManager = LinearLayoutManager(this@NewsSourcesActivity)
            setHasFixedSize(true)
            adapter = this@NewsSourcesActivity.adapter
        }

        adapter.onItemClick = { source ->
            NewsListActivity.start(
                this@NewsSourcesActivity,
                source = source.id,
            )
        }
    }

    private fun observeViewModel() {
        viewModel.newsSources.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    hideError()
                    showLoading(true)
                }

                is UiState.Success -> {
                    showLoading(false)
                    hideError()
                    state.data?.let { sources ->
                        if (sources.isNotEmpty()) {
                            adapter.submitList(sources)
                        } else {
                            showError(getString(R.string.no_sources_found))
                        }
                    }
                }

                is UiState.Error -> {
                    showLoading(false)
                    val errorMsg = state.message ?: getString(R.string.error_loading_sources)
                    showError(errorMsg)
                    showMessage(errorMsg)
                }

                else -> {}
            }
        }
    }

    private fun loadNewsSources() {
        viewModel.loadNewsSources()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        errorMessage = message
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = message
        binding.rvNewsSources.visibility = View.GONE
    }

    private fun hideError() {
        binding.errorView.visibility = View.GONE
        binding.rvNewsSources.visibility = View.VISIBLE
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
