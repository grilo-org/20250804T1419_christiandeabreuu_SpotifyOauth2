package com.example.spotifyapi.auth.data.plugin

import android.content.Context
import com.example.spotifyapi.R

interface ResourcesPlugin {

    fun getRequestTokenErrorMessage(code : String, message : String): String
    fun getAccessTokenErrorMessage(): String
    fun getSaveTokenErrorMessage(): String
    fun handleDirectCodeNotFound(): String
}

class ResourcesPluginImpl(private val context: Context) : ResourcesPlugin {

    override fun getRequestTokenErrorMessage(code: String, message: String): String = context.getString(R.string.error_message,code, message)
    override fun getAccessTokenErrorMessage(): String = context.getString(R.string.error_message_get_token)
    override fun getSaveTokenErrorMessage(): String = context.getString(R.string.error_message_save_token)
    override fun handleDirectCodeNotFound(): String = context.getString(R.string.error_message_direct_code_not_found)

}