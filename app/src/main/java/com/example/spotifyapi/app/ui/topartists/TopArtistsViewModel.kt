package com.example.spotifyapi.app.ui.topartists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopArtistsViewModel(
    private val userProfileUseCase: GetUserProfileUseCase,
    private val artistPagingSource: ArtistPagingSource,
) : ViewModel() {

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    private val _artistsLiveData = MutableLiveData<List<ArtistResponse>>()
    val artistsLiveData: LiveData<List<ArtistResponse>> get() = _artistsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getArtistsPagingData(): Flow<PagingData<ArtistResponse>> {
        return Pager(config = PagingConfig(
            pageSize = 20, enablePlaceholders = false
        ), pagingSourceFactory = {
            artistPagingSource
        }).flow.cachedIn(viewModelScope)
    }

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = userProfileUseCase.execute()
                _userProfileLiveData.postValue(userProfile)
            } catch (e: Exception) {
                _errorLiveData.postValue(R.string.error_message_search_profile.toString())
            }
        }
    }
}
