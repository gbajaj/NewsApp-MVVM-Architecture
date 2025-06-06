package com.gauravbajaj.newsapp.di.component

import com.gauravbajaj.newsapp.di.ActivityScope
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.country_sources.CountrySourcesActivity
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesActivity
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: TopHeadlineActivity)

    fun inject(activity: NewsSourcesActivity)

    fun inject(activity: CountrySourcesActivity)
}