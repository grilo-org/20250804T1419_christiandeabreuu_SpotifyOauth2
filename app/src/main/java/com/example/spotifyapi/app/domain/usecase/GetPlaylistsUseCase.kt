package com.example.spotifyapi.app.domain.usecase

import android.util.Log
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository

class GetPlaylistsUseCase(private val repository: PlaylistRepository) {

    suspend fun getPlaylists(accessToken: String): List<Playlist> {
        return try {
            val responseApi = repository.getPlaylistsFromApi(accessToken)
            if (responseApi.isNotEmpty()) {
                val playlistsDB = mapToPlaylistDB(responseApi)
                repository.insertPlaylistsIntoDB(playlistsDB)
                responseApi
            } else {
                throw Exception("Resposta da API está vazia")
            }
        } catch (e: Exception) {
            repository.getPlaylistsFromDB().map { it.toPlaylist() }
        }
    }

    private fun mapToPlaylistDB(playlists: List<Playlist>) : List<PlaylistDB> {
        return playlists.map { playlist ->
            PlaylistDB(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                ownerName = playlist.owner.name,
                tracksCount = playlist.tracksCount,
                imageUrl = playlist.images?.firstOrNull()?.url ?: ""
            )
        }
    }

    private fun PlaylistDB.toPlaylist(): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            owner = Owner(id = "", name = this.ownerName),
            tracksCount = this.tracksCount,
            images = if (this.imageUrl?.isBlank() == true) {
                Log.w("GetPlaylistsUseCase", "⚠️ Playlist sem imagem: ${this.name}")
                emptyList()
            } else {
                listOf(Image(url = this.imageUrl ?: ""))
            }
        )
    }
}


//    suspend fun getOfflinePlaylists(): List<Playlist> {
//        Log.w("GetPlaylistsUseCase", "⚠️ Sem internet! Chamando getPlaylistsFromDB().")
//        val playlistsDB = repository.getPlaylistsFromDB()
//
//        Log.d("GetPlaylistsUseCase", "📀 Total de playlists recuperadas do banco: ${playlistsDB.size}")
//        Log.d("GetPlaylistsUseCase", "🖼️ URLs das imagens carregadas offline: ${playlistsDB.map { it.imageUrl }}")
//
//        return playlistsDB.map { it.toPlaylist() }
//    }