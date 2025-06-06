package com.gauravbajaj.newsapp.ui.country_sources

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.databinding.ActivityCountrySourcesBinding
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.base.UiState
import javax.inject.Inject
/**
 * Activity to show the list of countries
 *
 * @author Gaurav Bajaj
 */
class CountrySourcesActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: CountrySourcesViewModel

    @Inject
    lateinit var adapter: CountryAdapter

    private lateinit var binding: ActivityCountrySourcesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
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
            // Handle item click - for example, open news from that country
            //TODO
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

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    companion object {
        const val EXTRA_COUNTRY_CODE = "extra_country_code"
    }
}
