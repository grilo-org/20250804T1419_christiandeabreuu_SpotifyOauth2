package com.example.spotifyapi

import android.app.Application
import com.example.spotifyapi.app.di.appModules
import com.example.spotifyapi.app.di.databaseModule
import com.example.spotifyapi.app.di.networkAppModule
import com.example.spotifyapi.auth.di.authModules
import com.example.spotifyapi.auth.di.networkAuthModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                authModules,
                networkAuthModule,
                appModules,
                networkAppModule,
                databaseModule
            )
        }
    }
}