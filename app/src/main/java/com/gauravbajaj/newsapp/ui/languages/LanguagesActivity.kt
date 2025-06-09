package com.gauravbajaj.newsapp.ui.languages

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.di.component.DaggerActivityComponent
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import javax.inject.Inject


class LanguagesActivity : AppCompatActivity() {
    private var doneMenuItem: MenuItem? = null
    private var selectedLanguages = emptyList<String>()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_languages, menu)
        doneMenuItem = menu.findItem(R.id.action_done)
        updateDoneButtonState()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_done -> {
                startNewsListActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = getString(R.string.select_language)
    }

    private fun setupRecyclerView() {
        adapter = LanguageAdapter()
        adapter.onSelectionChanged = { languages ->
            selectedLanguages = languages
            val selectedText = if (languages.isNotEmpty()) {
                getString(R.string.selected_languages, languages.joinToString())
            } else {
                getString(R.string.select_up_to_two_languages)
            }
            supportActionBar?.subtitle = selectedText
            updateDoneButtonState()
        }
        
        adapter.onMaxSelectionReached = {
            android.widget.Toast.makeText(
                this@LanguagesActivity,
                getString(R.string.max_languages_selected),
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        val rvLanguages: RecyclerView = findViewById(R.id.rvLanguages)
        rvLanguages.adapter = adapter
    }

    private fun updateDoneButtonState() {
        doneMenuItem?.isEnabled = selectedLanguages.isNotEmpty()
        doneMenuItem?.isVisible = selectedLanguages.isNotEmpty()
    }

    private fun startNewsListActivity() {
        NewsListActivity.start(
            this,
            language = selectedLanguages.joinToString()
        )
        finish()
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
