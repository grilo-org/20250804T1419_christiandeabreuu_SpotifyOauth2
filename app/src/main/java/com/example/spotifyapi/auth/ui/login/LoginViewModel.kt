package com.example.spotifyapi.auth.ui.login

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import com.example.spotifyapi.auth.domain.usecase.AuthUseCase
import com.example.spotifyapi.auth.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase,
    private val extractTokensUseCase: ExtractTokensUseCase,
    private val resourcesPlugin: ResourcesPlugin
) : ViewModel() {

    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> get() = _connectionStatus

    private val _navigateToArtists = MutableLiveData<String>()
    val navigateToArtists: LiveData<String> get() = _navigateToArtists

    private val _authResult = MutableLiveData<Result<SpotifyTokens>>()
    val authResult: LiveData<Result<SpotifyTokens>> get() = _authResult

    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String> get() = _authError

    fun updateAccessToken(accessToken: String) {
        _navigateToArtists.postValue(accessToken)
    }

    fun processRedirect(uri: Uri) {
        viewModelScope.launch {
            val tokens = extractTokensUseCase.execute(uri)
            _authResult.postValue(Result.success(tokens))
        }
    }

    fun getAuthIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.SPOTIFY_AUTH_URL))
    }

    fun handleRedirect(uri: Uri, redirectUri: String): LiveData<Result<SpotifyTokens>> =
        liveData(Dispatchers.IO) {
            val authorizationCode = uri.getQueryParameter(Constants.CODE) ?: run {
                _authError.postValue(resourcesPlugin.handleDirectCodeNotFound())
                return@liveData
            }

            val result = authUseCase.authenticate(authorizationCode, redirectUri)
            result.onSuccess {
                _authResult.postValue(Result.success(it))
                updateAccessToken(it.accessToken)
            }.onFailure {
                _authError.postValue(it.message)
        }
    }
}
