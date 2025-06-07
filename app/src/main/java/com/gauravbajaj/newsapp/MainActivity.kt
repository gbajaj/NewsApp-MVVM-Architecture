package com.gauravbajaj.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gauravbajaj.newsapp.ui.country_sources.CountrySourcesActivity
import com.gauravbajaj.newsapp.ui.languages.LanguagesActivity
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesActivity
import com.gauravbajaj.newsapp.ui.search.SearchActivity
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    fun onTopHeadlinesClick(view: View) {
        startActivity(Intent(this, TopHeadlineActivity::class.java))
    }

    fun onNewsSourcesClick(view: View) {
        startActivity(Intent(this, NewsSourcesActivity::class.java))
    }
    
    fun onCountriesClick(view: View) {
        startActivity(Intent(this, CountrySourcesActivity::class.java))
    }
    
    fun onLanguagesClick(view: View) {
        startActivity(
            Intent(this, LanguagesActivity::class.java)
        )
    }
    
    fun onSearchClick(view: View) {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    companion object {
        private const val REQUEST_CODE_LANGUAGE_SELECTION = 1001
    }
}