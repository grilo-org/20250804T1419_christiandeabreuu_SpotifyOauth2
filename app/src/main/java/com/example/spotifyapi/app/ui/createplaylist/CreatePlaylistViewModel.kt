package com.example.spotifyapi.app.ui.createplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreatePlaylistViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase
) : ViewModel() {

    private val _createPlaylistLiveData = MutableLiveData<Result<String>>()
    val createPlaylistLiveData: LiveData<Result<String>> get() = _createPlaylistLiveData

    fun createPlaylist(accessToken: String, playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (playlistName.isBlank()) {
                    _createPlaylistLiveData.postValue(Result.failure(Exception("Por favor, insira um nome para a playlist.")))
                    return@launch
                }
                val result = createPlaylistUseCase.execute(accessToken, playlistName)
                _createPlaylistLiveData.postValue(result)
            } catch (e: Exception) {
                _createPlaylistLiveData.postValue(Result.failure(e))
            }
        }
    }
}


