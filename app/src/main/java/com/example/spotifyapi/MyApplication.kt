package com.example.spotifyapi

import android.app.Application
import com.example.spotifyapi.oauth2.di.authModules
import com.example.spotifyapi.app.di.networkModule
import com.example.spotifyapi.app.di.spotifyModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                authModules, spotifyModules, networkModule
            )
        }
    }
}