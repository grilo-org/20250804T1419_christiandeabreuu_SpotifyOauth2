package com.example.spotifyapi.app.ui.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import kotlinx.coroutines.launch

class AlbumsViewModel(private val getAlbumsUseCase: GetAlbumsUseCase) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    fun fetchAlbums(accessToken: String, artistId: String) {
        viewModelScope.launch {
            try {
                val albums = getAlbumsUseCase.execute(accessToken, artistId)
                _albumsLiveData.postValue(albums)
            } catch (e: Exception) {
                Log.e("AlbumsViewModel", "❌ Erro ao buscar álbuns: ${e.message}")
            }
        }
    }
}