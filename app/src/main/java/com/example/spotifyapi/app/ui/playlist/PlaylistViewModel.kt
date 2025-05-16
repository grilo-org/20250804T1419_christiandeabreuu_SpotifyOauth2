package com.example.spotifyapi.app.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfilePlaylistsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsUseCase: GetPlaylistsUseCase,
    private val userProfilePlaylistsUseCase: GetUserProfilePlaylistsUseCase
) : ViewModel() {

    private val _playlistsLiveData = MutableLiveData<List<Playlist>>()
    val playlistsLiveData: LiveData<List<Playlist>> get() = _playlistsLiveData

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    fun getPlaylists(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlists = playlistsUseCase.execute(accessToken)
            _playlistsLiveData.postValue(playlists)
        }
    }

    fun getUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userProfile = userProfilePlaylistsUseCase.execute(accessToken)
            _userProfileLiveData.postValue(userProfile)
        }
    }
}