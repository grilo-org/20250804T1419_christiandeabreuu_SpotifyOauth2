package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylist
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylistDB

class GetPlaylistsUseCase(private val repository: PlaylistRepository) {

    suspend fun getPlaylists(): List<Playlist> {
        val localPlaylists = getPlaylistsFromDB()
        val onlinePlaylists = fetchPlaylistsFromApi()

        return if (onlinePlaylists.isNotEmpty() && onlinePlaylists != localPlaylists) {
            savePlaylistsToDB(onlinePlaylists)
            onlinePlaylists
        } else {
            localPlaylists
        }
    }

    private suspend fun fetchPlaylistsFromApi(): List<Playlist> {
        val responseApi = repository.getPlaylistsFromApi()

        if (responseApi.isEmpty()) {
            return emptyList()
        }

        savePlaylistsToDB(responseApi)
        return responseApi
    }

    private suspend fun savePlaylistsToDB(playlists: List<Playlist>) {
        val existingPlaylists = repository.getPlaylistsFromDB().map { it.id }.toSet()
        val filteredPlaylists = playlists.filter { it.id !in existingPlaylists }
        repository.insertPlaylistsIntoDB(filteredPlaylists.map { it.toPlaylistDB() })
    }

    private suspend fun getPlaylistsFromDB(): List<Playlist> {
        return repository.getPlaylistsFromDB().map { it.toPlaylist() }
    }
}