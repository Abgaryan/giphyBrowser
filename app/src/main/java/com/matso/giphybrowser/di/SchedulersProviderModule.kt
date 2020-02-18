package com.matso.giphybrowser.di

import com.matso.giphybrowser.schedulers.BaseSchedulerProvider
import com.matso.giphybrowser.schedulers.SchedulerProvider
import org.koin.dsl.module

val schedulerProviderModule = module {
    single { SchedulerProvider() as BaseSchedulerProvider }
}
