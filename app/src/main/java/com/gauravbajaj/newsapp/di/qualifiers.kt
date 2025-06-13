package com.gauravbajaj.newsapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackgroundContext

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainContext