package com.example.spotifyapi.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    fun getUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userProfile = userProfileUseCase.execute(accessToken)
            _userProfileLiveData.postValue(userProfile)
        }
    }
}