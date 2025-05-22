package com.example.spotifyapi.auth.domain.usecase

import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.repository.TokenRepository

class AuthUseCase(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val tokenRepository: TokenRepository
) {
    suspend fun authenticate(
        authorizationCode: String,
        redirectUri: String
    ): Result<SpotifyTokens> {
        val tokensResult = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
        val tokens =
            tokensResult.getOrNull() ?: return Result.failure(Exception("Erro ao obter token"))

        return if (tokenRepository.saveTokens(tokens.accessToken, tokens.refreshToken)) {
            Result.success(tokens)
        } else {
            Result.failure(Exception("Erro ao salvar tokens"))
        }
    }
}