package com.example.spotifyapi.app.ui.topartists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopArtistsViewModel(
    private val topArtistsUseCase: GetTopArtistsUseCase,
    private val userProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _artistsLiveData = MutableLiveData<List<TopArtistInfoResponse>>()
    val artistsLiveData: LiveData<List<TopArtistInfoResponse>> get() = _artistsLiveData

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
            val userProfile = userProfileUseCase.execute(accessToken)
            _userProfileLiveData.postValue(userProfile)
        }
    }
}


