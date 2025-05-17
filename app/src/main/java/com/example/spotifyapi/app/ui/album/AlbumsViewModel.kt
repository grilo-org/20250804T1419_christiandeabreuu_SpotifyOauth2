package com.example.spotifyapi.app.ui.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import kotlinx.coroutines.launch

class AlbumsViewModel(private val spotifyApiService: SpotifyApiService) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    fun fetchAlbums(accessToken: String, artistId: String) {
        viewModelScope.launch {
            try {
                val response = spotifyApiService.getAlbums("Bearer $accessToken", artistId)
                _albumsLiveData.postValue(response.items) // 🔥 Atualiza a UI automaticamente
            } catch (e: Exception) {
                Log.e("AlbumsViewModel", "❌ Erro ao buscar álbuns: ${e.message}")
            }
        }
    }
}