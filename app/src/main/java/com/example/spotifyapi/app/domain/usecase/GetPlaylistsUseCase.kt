package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository

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

    private fun Playlist.toPlaylistDB(): PlaylistDB {
        return PlaylistDB(
            id = this.id,
            name = this.name,
            description = this.description,
            ownerName = this.owner.name,
            tracksCount = this.tracksCount,

            imageUrl = this.images?.firstOrNull()?.url ?: "" 
        )
    }

    private fun PlaylistDB.toPlaylist(): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            owner = Owner(id = "", name = this.ownerName),
            tracksCount = this.tracksCount,
            images = this.imageUrl?.takeIf { it.isNotBlank() }
                ?.let { listOf(Image(url = it)) }
                ?: emptyList()
        )
    }
}