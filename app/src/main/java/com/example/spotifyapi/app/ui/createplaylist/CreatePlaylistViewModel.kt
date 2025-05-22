package com.example.spotifyapi.app.ui.createplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.R
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase
) : ViewModel() {

    private val _createPlaylistLiveData = MutableLiveData<Result<String>>()
    val createPlaylistLiveData: LiveData<Result<String>> get() = _createPlaylistLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = createPlaylistUseCase.execute(playlistName)
                _createPlaylistLiveData.postValue(Result.success(result))
            } catch (e: Exception) {
                _errorLiveData.postValue(R.string.error_create_playlist.toString())
            }
        }
    }
}


