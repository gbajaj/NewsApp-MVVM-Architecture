package com.gauravbajaj.newsapp.ui.topheadlines

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.databinding.ActivityTopHeadlinesBinding
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadlineActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: TopHeadlineViewModel

    private lateinit var adapter: TopHeadlinesAdapter

    private lateinit var binding: ActivityTopHeadlinesBinding
    private var errorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupUI()
        setupObserver()
        loadHeadlines()

        binding.retryButton.setOnClickListener {
            loadHeadlines()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI() {
        adapter = TopHeadlinesAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TopHeadlineActivity)
            setHasFixedSize(true)
            adapter = this@TopHeadlineActivity.adapter
        }
        adapter.setOnItemClickListener { article ->
            CustomTabsHelper.launchUrl(this, article.url)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { result ->
                    when (result) {
                        is UiState.Loading -> {
                            hideError()
                            showLoading(true)
                        }

                        is UiState.Success -> {
                            showLoading(false)
                            hideError()
                            result.data.let { articles ->
                                if (articles.isNotEmpty()) {
                                    adapter.submitList(articles)
                                } else {
                                    showError(getString(R.string.no_articles_found))
                                }
                            }
                        }

                        is UiState.Error -> {
                            showLoading(false)
                            val errorMsg = result.message ?: getString(R.string.error_loading_news)
                            showError(errorMsg)
                            showMessage(errorMsg)
                        }
                    }
                }
            }
        }
    }
    private fun loadHeadlines() {
        viewModel.loadTopHeadlines()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        errorMessage = message
        binding.errorView.visibility = View.VISIBLE
        binding.errorMessage.text = message
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideError() {
        binding.errorView.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }
}
