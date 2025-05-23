package com.example.spotifyapi.app.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsUseCase: GetPlaylistsUseCase,
    private val userProfileUseCase: GetUserProfileUseCase,
    private val resourcesPlugin: ResourcesPlugin
) : ViewModel() {

    private val _playlistsLiveData = MutableLiveData<List<Playlist>>()
    val playlistsLiveData: LiveData<List<Playlist>> get() = _playlistsLiveData

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = playlistsUseCase.getPlaylists()
                _playlistsLiveData.postValue(playlists)
            } catch (e: Exception) {
                _errorLiveData.postValue(resourcesPlugin.searchPlaylistsErrorMessage())
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = userProfileUseCase.getUserProfile()
                _userProfileLiveData.postValue(userProfile)
            } catch (e: Exception) {
                _errorLiveData.postValue(resourcesPlugin.searchProfileErrorMessage())
            }
        }
    }
}