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
                Log.d("GetPlaylistsUseCase", "🎵 Playlists carregadas da API: ${responseApi.size}")
                mapToPlaylistDB(responseApi) // Salva no banco
                responseApi
            } else {
                throw Exception("Resposta da API está vazia")
            }
        } catch (e: Exception) {
            Log.e("GetPlaylistsUseCase", "❌ Erro ao buscar playlists: ${e.message}")
            repository.getPlaylistsFromDB().map { it.toPlaylist() }
        }
    }

    suspend fun getOfflinePlaylists(): List<Playlist> {
        Log.w("GetPlaylistsUseCase", "⚠️ Sem internet! Chamando getPlaylistsFromDB().")
        val playlistsDB = repository.getPlaylistsFromDB()

        Log.d("GetPlaylistsUseCase", "📀 Total de playlists recuperadas do banco: ${playlistsDB.size}")
        Log.d("GetPlaylistsUseCase", "🖼️ URLs das imagens carregadas offline: ${playlistsDB.map { it.imageUrl }}")

        return playlistsDB.map { it.toPlaylist() }
    }


    private suspend fun mapToPlaylistDB(playlists: List<Playlist>) {
        val playlistsDB = playlists.map { playlist ->
            PlaylistDB(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                ownerName = playlist.owner.name,
                tracksCount = playlist.tracksCount,
                imageUrl = playlist.images?.firstOrNull()?.url ?: "" // Garante string não-nula
            )
        }

        Log.d("GetPlaylistsUseCase", "🗄️ Salvando ${playlistsDB.size} playlists no banco.")
        Log.d(
            "GetPlaylistsUseCase",
            "🖼️ URLs das imagens salvas: ${playlistsDB.map { it.imageUrl }}"
        )

        repository.insertPlaylistsIntoDB(playlistsDB)
    }

    private fun PlaylistDB.toPlaylist(): Playlist {
        Log.d("GetPlaylistsUseCase", "🔄 Convertendo PlaylistDB para Playlist: ${this.imageUrl}")
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            owner = Owner(id = "", name = this.ownerName),
            tracksCount = this.tracksCount,
            images = if (this.imageUrl?.isBlank() == true) { // Agora verifica de forma segura
                Log.w("GetPlaylistsUseCase", "⚠️ Playlist sem imagem: ${this.name}")
                emptyList()
            } else {
                listOf(Image(url = this.imageUrl ?: "")) // Se for nulo, usa string vazia
            }
        )
    }
}