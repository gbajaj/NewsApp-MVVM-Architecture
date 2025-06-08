package com.gauravbajaj.newsapp.ui.languages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.languages.LanguageAdapter
import com.gauravbajaj.newsapp.ui.languages.LanguagesViewModel
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesActivity
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import javax.inject.Inject

class LanguagesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: LanguagesViewModel

    @Inject
    lateinit var adapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        adapter = LanguageAdapter()
        adapter.onItemClick = { language ->
            NewsListActivity.start(
                this@LanguagesActivity,
                language = language.code,
            )
        }

        val rvLanguages: RecyclerView = findViewById(R.id.rvLanguages)
        rvLanguages.adapter = adapter
    }


    private fun observeViewModel() {
        viewModel.languages.observe(this) { languages ->
            adapter.submitList(languages)
        }
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }
}
