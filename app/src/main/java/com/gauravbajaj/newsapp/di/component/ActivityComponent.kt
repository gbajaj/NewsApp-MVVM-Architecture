package com.gauravbajaj.newsapp.di.component

import com.gauravbajaj.newsapp.di.ActivityScope
import com.gauravbajaj.newsapp.di.module.ActivityModule
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: TopHeadlineActivity)

}