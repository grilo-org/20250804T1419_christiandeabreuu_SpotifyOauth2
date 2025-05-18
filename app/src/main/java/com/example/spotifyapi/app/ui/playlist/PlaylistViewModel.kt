package com.example.spotifyapi.app.ui.playlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfilePlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsUseCase: GetPlaylistsUseCase,
    private val userProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _playlistsLiveData = MutableLiveData<List<Playlist>>()
    val playlistsLiveData: LiveData<List<Playlist>> get() = _playlistsLiveData

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

//    fun getPlaylists(accessToken: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val playlists = playlistsUseCase.getPlaylists(accessToken) ?: emptyList()
//            _playlistsLiveData.postValue(playlists)
//        }
//    }

//    fun getPlaylists(accessToken: String, context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val isConnected = NetworkUtils.isInternetAvailable(context) // Verifica conexão
//            val playlists = if (isConnected) {
//                Log.d("PlaylistViewModel", "🌐 Internet disponível! Carregando da API.")
//                playlistsUseCase.getPlaylists(accessToken) // Busca online
//            } else {
//                Log.w("PlaylistViewModel", "⚠️ Sem internet! Chamando getOfflinePlaylists().")
//                playlistsUseCase.getOfflinePlaylists() // Busca offline
//            }
//
//            Log.d("PlaylistViewModel", "📂 Playlists enviadas para o LiveData: ${playlists.size}")
//            _playlistsLiveData.postValue(playlists)
//        }
//    }

        fun getPlaylists(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlists = playlistsUseCase.getPlaylists(accessToken)
            _playlistsLiveData.postValue(playlists)
        }
    }


    fun getUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userProfile = userProfileUseCase.execute(accessToken)
            _userProfileLiveData.postValue(userProfile)
        }
    }
}