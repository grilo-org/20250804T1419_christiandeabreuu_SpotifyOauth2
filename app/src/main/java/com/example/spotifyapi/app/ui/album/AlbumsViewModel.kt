package com.example.spotifyapi.app.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumsViewModel(private val getAlbumsUseCase: GetAlbumsUseCase) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getAlbums(artistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val albums = getAlbumsUseCase.execute(artistId)
                _albumsLiveData.postValue(albums)
            } catch (e: Exception) {
                _errorLiveData.postValue(R.string.error_msg_search_albums.toString())
            }
        }
    }
}