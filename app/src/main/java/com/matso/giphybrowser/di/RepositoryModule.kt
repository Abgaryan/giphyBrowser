package com.matso.giphybrowser.di

import com.matso.giphybrowser.data.repository.GiphyRepository
import com.matso.giphybrowser.data.repository.GiphyRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<GiphyRepository> { GiphyRepositoryImpl(get()) }
}
