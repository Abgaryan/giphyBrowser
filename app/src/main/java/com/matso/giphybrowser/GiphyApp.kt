package com.matso.giphybrowser

import android.app.Application
import com.matso.giphybrowser.di.networkingModule
import com.matso.giphybrowser.di.repositoryModule
import com.matso.giphybrowser.di.schedulerProviderModule
import com.matso.giphybrowser.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GiphyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GiphyApp)
            modules(
                listOf(
                    networkingModule,
                    repositoryModule,
                    schedulerProviderModule,
                    viewModelModule
                )
            )
        }
    }

}
