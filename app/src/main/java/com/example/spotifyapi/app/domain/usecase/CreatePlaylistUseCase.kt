package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository

class CreatePlaylistUseCase(private val repository: CreatePlaylistRepository) {

    suspend fun execute(playlistName: String): String {
        validatePlaylistName(playlistName)

        return createPlaylist(playlistName)
    }

    private fun validatePlaylistName(playlistName: String) {
        if (playlistName.isBlank()) {
            throw IllegalArgumentException("Por favor, insira um nome para a playlist.")
        }
    }

    private suspend fun createPlaylist(playlistName: String): String {
        return if (repository.createPlaylist(playlistName)) {
            "Playlist '${playlistName}' criada com sucesso!"
        } else {
            throw Exception("Erro ao criar playlist.")
        }
    }
}