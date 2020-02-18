package com.matso.giphybrowser.di

import com.matso.giphybrowser.ui.giphyserach.GiphySearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { GiphySearchViewModel(get(), get()) }
}
