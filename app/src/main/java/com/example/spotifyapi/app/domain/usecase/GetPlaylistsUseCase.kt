package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylist
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylistDB

class GetPlaylistsUseCase(private val repository: PlaylistRepository) {

    suspend fun getPlaylists(accessToken: String): List<Playlist> {
        return fetchPlaylistsFromApi(accessToken).ifEmpty {
            getPlaylistsFromDB()
        }
    }

    private suspend fun fetchPlaylistsFromApi(accessToken: String): List<Playlist> {
        val responseApi = repository.getPlaylistsFromApi(accessToken)

        if (responseApi.isEmpty()) {
            return emptyList()
        }

        savePlaylistsToDB(responseApi)
        return responseApi
    }

    private suspend fun savePlaylistsToDB(playlists: List<Playlist>) {
        val existingPlaylists = repository.getPlaylistsFromDB().map { it.id }.toSet()

        val filteredPlaylists = playlists.filter { it.id !in existingPlaylists } // 🔹 Remove duplicatas antes de salvar

        repository.insertPlaylistsIntoDB(filteredPlaylists.map { it.toPlaylistDB() })
    }

    private suspend fun getPlaylistsFromDB(): List<Playlist> {
        return repository.getPlaylistsFromDB().map { it.toPlaylist() }
    }
}