package com.example.spotifyapi.oauth2.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.utils.Constants
import com.example.spotifyapi.oauth2.data.repository.TokenRepository
import com.example.spotifyapi.oauth2.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    fun handleRedirect(uri: Uri, redirectUri: String) = liveData(Dispatchers.IO) {
        val authorizationCode = uri.getQueryParameter("code") ?: run {
            emit(Result.failure(Exception("Código de autorização não encontrado")))
            return@liveData
        }

        Log.d("LoginViewModel", "✅ Código de autorização recebido: $authorizationCode")

        try {
            val tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
            Log.d("LoginViewModel", "✅ Token gerado: ${tokens.accessToken}")

            val isSaved = tokenRepository.saveTokens(tokens.accessToken, tokens.refreshToken)
            if (isSaved) {
                emit(Result.success(TokenState(tokens, TokenStateEvent.GetToken)))
            } else {
                Log.e("LoginViewModel", "❌ Erro ao salvar tokens")
                emit(Result.failure(Exception("Erro ao salvar tokens")))
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Erro ao trocar código pelos tokens: ${e.message}")
            emit(Result.failure(e))
        }
    }

//    fun startAuthentication(): Intent {
//        Log.d("AuthTest", "SPOTIFY_AUTH_URL: ${BuildConfig.SPOTIFY_AUTH_URL}")
//        val authUrl = BuildConfig.SPOTIFY_AUTH_URL.ifEmpty {
//            "https://accounts.spotify.com/authorize?client_id=SEU_CLIENT_ID&response_type=code&redirect_uri=meuapp://callback&scope=user-read-private%20user-read-email%20playlist-modify-public%20playlist-modify-private%20user-top-read"
//        }
//        return Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
//    }

    fun startAuthentication(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.SPOTIFY_AUTH_URL))
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