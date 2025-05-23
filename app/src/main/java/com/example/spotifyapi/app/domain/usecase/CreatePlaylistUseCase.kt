package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin

class CreatePlaylistUseCase(
    private val repository: CreatePlaylistRepository,
    private val resources: ResourcesPlugin
) {

    suspend fun createPlaylist(playlistName: String): String {
        validateName(playlistName)
        return performCreatePlaylist(playlistName)
    }

    private fun validateName(playlistName: String) {
        if (playlistName.isBlank()) {
            throw IllegalArgumentException(resources.validatePlaylistNameMessage())
        }
    }

    private suspend fun performCreatePlaylist(playlistName: String): String {
        return if (repository.createPlaylist(playlistName)) {
            resources.playlistCreatedSuccessMessage(playlistName)
        } else {
            throw Exception(resources.createPlaylistErrorMessage())
        }
    }
}