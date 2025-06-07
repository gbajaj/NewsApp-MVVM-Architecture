package com.gauravbajaj.newsapp.di.component

import android.content.Context
import com.gauravbajaj.newsapp.NewsApplication
import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.repository.NewsSourcesRepository
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.di.ApplicationContext
import com.gauravbajaj.newsapp.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NewsApplication)

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getTopHeadlineRepository(): TopHeadlineRepository

    fun getNewsSourcesRepository(): NewsSourcesRepository
    
    fun getSearchRepository(): SearchRepository

}