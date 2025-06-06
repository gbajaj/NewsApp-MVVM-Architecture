package com.gauravbajaj.newsapp.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gauravbajaj.newsapp.data.repository.NewsSourcesRepository
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.di.ActivityContext
import com.gauravbajaj.newsapp.ui.base.ViewModelProviderFactory
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesAdapter
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesViewModel
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlinesAdapter
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

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
    fun provideTopHeadlineAdapter() = TopHeadlinesAdapter(ArrayList())

    @Provides
    fun provideNewsSourcesAdapter() = NewsSourcesAdapter(ArrayList())

}