package com.gauravbajaj.newsapp.ui.country_sources

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.databinding.ActivityCountrySourcesBinding
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity

/**
 * Activity to show the list of countries
 *
 * @author Gaurav Bajaj
 */
class CountrySourcesActivity : AppCompatActivity() {
    private val viewModel by viewModels<CountrySourcesViewModel>()

    lateinit var adapter: CountryAdapter

    private lateinit var binding: ActivityCountrySourcesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountrySourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        adapter = CountryAdapter()
        binding.rvCountries.apply {
            layoutManager = LinearLayoutManager(this@CountrySourcesActivity)
            setHasFixedSize(true)
            this.adapter = this@CountrySourcesActivity.adapter
        }

        adapter.onItemClick = { country ->
            // Open NewsListActivity with the selected country
            NewsListActivity.start(
                this@CountrySourcesActivity,
                country = country.code.toLowerCase()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.countries.observe(this) { state ->
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    state.data?.let { countries ->
                        adapter.submitList(countries)
                    }
                }

                is UiState.Error -> {
                    showLoading(false)
                    // Handle error
                }

                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_COUNTRY_CODE = "extra_country_code"
    }
}
