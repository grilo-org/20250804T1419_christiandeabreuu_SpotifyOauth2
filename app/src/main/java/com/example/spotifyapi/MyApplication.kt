package com.example.spotifyapi

import android.app.Application
import com.example.spotifyapi.di.appModule
import com.example.spotifyapi.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule, networkModule
            )
        }
    }
}