package com.example.spotifyapi.oauth2.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.oauth2.data.model.SpotifyTokens
import com.example.spotifyapi.oauth2.domain.usecase.AuthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> get() = _connectionStatus

    private val _navigateToArtists = MutableLiveData<String>()
    val navigateToArtists: LiveData<String> get() = _navigateToArtists

    private val _authResult = MutableLiveData<Result<SpotifyTokens>>()
    val authResult: LiveData<Result<SpotifyTokens>> get() = _authResult


    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String> get() = _authError

    fun notifyError(message: String) {
        _authError.postValue(message)
    }

    fun navigate(accessToken: String) {
        _navigateToArtists.postValue(accessToken)
    }

    fun checkInternet(context: Context) {
        _connectionStatus.postValue(isInternetAvailable(context))
    }

    fun processRedirect(uri: Uri) {
        viewModelScope.launch {
            val tokens = extractTokensFromUri(uri)
            _authResult.postValue(Result.success(tokens))
        }
    }

    private fun extractTokensFromUri(uri: Uri): SpotifyTokens {
        val accessToken = uri.getQueryParameter("access_token") ?: ""
        val refreshToken = uri.getQueryParameter("refresh_token") ?: ""
        return SpotifyTokens(accessToken, refreshToken)
    }

    fun getAuthIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.SPOTIFY_AUTH_URL))
    }

    fun handleRedirect(uri: Uri, redirectUri: String): LiveData<Result<SpotifyTokens>> =
        liveData(Dispatchers.IO) {
            val authorizationCode = uri.getQueryParameter("code") ?: run {
                _authError.postValue("Código de autorização não encontrado")
                return@liveData
            }

            val result = authUseCase.authenticate(authorizationCode, redirectUri)
            result.onSuccess {
                _authResult.postValue(Result.success(it))
                navigate(it.accessToken)
            }.onFailure {
                _authError.postValue(it.message)
            }
        }

    @SuppressLint("ServiceCast")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
