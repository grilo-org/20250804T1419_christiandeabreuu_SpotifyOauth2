package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository

class CreatePlaylistUseCase(private val createPlaylistRepository: CreatePlaylistRepository) {
    suspend fun execute(accessToken: String, playlistName: String): Result<String> {
        return try {
            val response = createPlaylistRepository.createPlaylist(accessToken, playlistName)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}