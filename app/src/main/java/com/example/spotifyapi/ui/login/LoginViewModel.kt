package com.example.spotifyapi.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotify.utils.Constants
import com.example.spotifyapi.data.repository.TokenRepository
import com.example.spotifyapi.domain.usecase.GetAccessTokenUseCase
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
        try {
            val tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
            val isSaved = tokenRepository.saveTokens(tokens.accessToken, tokens.refreshToken)
            if (isSaved) {
                emit(Result.success(TokenState(tokens, TokenStateEvent.GetToken)))
            } else {
                emit(Result.failure(Exception("Erro ao salvar tokens")))
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Erro ao trocar código pelos tokens: ${e.message}")
            emit(Result.failure(e))
        }
    }

    fun startAuthentication(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
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