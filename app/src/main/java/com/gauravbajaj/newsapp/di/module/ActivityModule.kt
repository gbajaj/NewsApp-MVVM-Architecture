package com.gauravbajaj.newsapp.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gauravbajaj.newsapp.data.repository.NewsSourcesRepository
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.di.ActivityContext
import com.gauravbajaj.newsapp.di.ApplicationContext
import com.gauravbajaj.newsapp.ui.base.ViewModelProviderFactory
import com.gauravbajaj.newsapp.ui.country_sources.CountryAdapter
import com.gauravbajaj.newsapp.ui.country_sources.CountrySourcesViewModel
import com.gauravbajaj.newsapp.ui.languages.LanguageAdapter
import com.gauravbajaj.newsapp.ui.languages.LanguagesViewModel
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesAdapter
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesViewModel
import com.gauravbajaj.newsapp.ui.search.SearchResultsAdapter

import com.gauravbajaj.newsapp.ui.search.SearchViewModel
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineViewModel
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlinesAdapter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    /**
     * Provides the [TopHeadlineViewModel] to the [TopHeadlineActivity].
     *
     * @param topHeadlineRepository the [TopHeadlineRepository] to use for the view model.
     *
     * @return the [TopHeadlineViewModel].
     */
    @Provides
    fun provideNewsListViewModel(topHeadlineRepository: TopHeadlineRepository): TopHeadlineViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(TopHeadlineViewModel::class) {
                TopHeadlineViewModel(topHeadlineRepository)
            })[TopHeadlineViewModel::class.java]
    }

    @Provides
    fun provideNewsSourcesViewModel(newsSourcesRepository: NewsSourcesRepository): NewsSourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(NewsSourcesViewModel::class) {
                NewsSourcesViewModel(newsSourcesRepository)
            })[NewsSourcesViewModel::class.java]
    }

    @Provides
    fun provideTopHeadlinesAdapter() = TopHeadlinesAdapter()

    @Provides
    fun provideNewsSourcesAdapter() = NewsSourcesAdapter (ArrayList())


    @Provides
    fun provideCountrySourcesViewModel(): CountrySourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(CountrySourcesViewModel::class) {
                CountrySourcesViewModel()
            })[CountrySourcesViewModel::class.java]
    }
    
    @Provides
    fun provideCountryAdapter() = CountryAdapter()

    @Provides
    fun provideLanguagesViewModel(@ApplicationContext context: Context): LanguagesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(LanguagesViewModel::class) {
                LanguagesViewModel(context)
            })[LanguagesViewModel::class.java]
    }

    @Provides
    fun provideLanguageAdapter() = LanguageAdapter()
    
    @Provides
    fun provideSearchViewModel(searchRepository: SearchRepository): SearchViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(SearchViewModel::class) {
                SearchViewModel(searchRepository)
            })[SearchViewModel::class.java]
    }
    
    @Provides
    fun provideSearchResultsAdapter(): SearchResultsAdapter = SearchResultsAdapter()


}