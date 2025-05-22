package com.example.spotifyapi.auth.data.plugin

import android.content.Context
import com.example.spotifyapi.R

interface ResourcesPlugin {

    fun getRequestTokenErrorMessage(code : String, message : String): String
}

class ResourcesPluginImpl(private val context: Context) : ResourcesPlugin {
    override fun getRequestTokenErrorMessage(code: String, message: String): String = context.getString(R.string.error_message,code, message)
}