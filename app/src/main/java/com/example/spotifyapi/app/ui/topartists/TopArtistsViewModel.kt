package com.example.spotifyapi.app.ui.topartists

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.SpotifyAuthHelper
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileTopArtistsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopArtistsViewModel(
    private val topArtistsUseCase: GetTopArtistsUseCase,
    private val spotifyAuthHelper: SpotifyAuthHelper,
    private val userProfileTopArtistsUseCase: GetUserProfileTopArtistsUseCase
) : ViewModel() {

    private val _artistsLiveData = MutableLiveData<List<Artist>>()
    val artistsLiveData: LiveData<List<Artist>> get() = _artistsLiveData

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    init {
        getTopArtists(accessToken = "")
    }


    fun getTopArtists(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val artists = topArtistsUseCase.execute(accessToken)
            _artistsLiveData.postValue(artists)
        }
    }

    fun getUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userProfile = userProfileTopArtistsUseCase.execute(accessToken)
            _userProfileLiveData.postValue(userProfile)
        }
    }

    fun loadTokens(context: Context) = liveData(Dispatchers.IO) {
        val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "") ?: ""
        emit(Pair(accessToken, refreshToken))
    }

    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
        try {
            val tokens = spotifyAuthHelper.refreshAccessToken(refreshToken)
            emit(tokens)
        } catch (e: Exception) {
            emit(null)
        }
    }
}


