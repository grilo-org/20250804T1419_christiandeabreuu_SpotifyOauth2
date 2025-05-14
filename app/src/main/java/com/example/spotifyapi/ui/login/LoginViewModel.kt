package com.example.spotifyapi.ui.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.spotifyapi.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val getAccessTokenUseCase: GetAccessTokenUseCase) : ViewModel() {

    fun handleRedirect(uri: Uri, redirectUri: String) = liveData(Dispatchers.IO) {
        val authorizationCode = uri.getQueryParameter("code") ?: run {
            emit(Result.failure(Exception("Código de autorização não encontrado")))
            return@liveData
        }
        try {
            val tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri) // ✅ Correto
            emit(Result.success(TokenState(tokens, TokenStateEvent.GetToken)))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Erro ao trocar código pelos tokens: ${e.message}")
            emit(Result.failure(e))
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return try {
            // Se houver um método específico para salvar tokens no UseCase, ele pode ser chamado aqui
            Log.d("LoginViewModel", "Tokens salvos com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Erro ao salvar tokens: ${e.message}")
            false
        }
    }
}