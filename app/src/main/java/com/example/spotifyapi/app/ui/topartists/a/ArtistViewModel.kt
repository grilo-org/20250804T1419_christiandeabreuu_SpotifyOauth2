package com.example.spotifyapi.app.ui.topartists.a

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.ui.topartists.c.ArtistC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArtistViewModel(
//    private val loadTokensUseCase: LoadTokensUseCase,
//    private val saveTokensUseCase: SaveTokensUseCase,
//    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
//    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val userProfileUseCase: GetUserProfileUseCase,
    private val getTopArtistsUseCase: GetTopArtistsUseCase,
    private val spotifyDAO: SpotifyDAO,
    private val context: Context
) : ViewModel() {

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    private val _artistsLiveData = MutableLiveData<List<ArtistC>>()
    val artistsLiveData: LiveData<List<ArtistC>> get() = _artistsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData


    fun getTopArtist(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val topArtists = getTopArtistsUseCase.getFromApi(accessToken)
            emit(Result.success(topArtists))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getArtistsPagingData(accessToken: String): Flow<PagingData<ArtistC>> {
        return Pager(config = PagingConfig(
            pageSize = 20, enablePlaceholders = false
        ),
            pagingSourceFactory = {
                ArtistPagingSource(
                    getTopArtistsUseCase,
                    accessToken,
                    context
                )
            }).flow.cachedIn(viewModelScope)
    }


    fun getUserProfile(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = userProfileUseCase.execute(accessToken)
                _userProfileLiveData.postValue(userProfile)
            } catch (e: Exception) {
                _errorLiveData.postValue("Erro ao buscar perfil do usuário")
            }
        }
    }

//    fun getUserProfileImage() = liveData(Dispatchers.IO) {
//        val profileImage = spotifyDAO.getUserProfile()?.imageUrl
//
//        emit(profileImage)
//    }
//    fun refreshToken(refreshToken: String) = liveData(Dispatchers.IO) {
//        try {
//            val tokens = refreshAccessTokenUseCase.execute(refreshToken)
//            emit(Result.success(tokens))
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }
//
//    fun loadTokens() = liveData(Dispatchers.IO) {
//        try {
//            val tokens = loadTokensUseCase.execute()
//            emit(Result.success(tokens))
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }
//
//    fun exchangeCodeForTokens(authorizationCode: String, redirectUri: String) = liveData(Dispatchers.IO) {
//        try {
//
//            val tokens: Tokens = getAccessTokenUseCase.execute(authorizationCode, redirectUri)
//            emit(Result.success(tokens))
//        } catch (e: Exception) {
//            emit(Result.failure<Tokens>(e))
//        }
//    }
//
//    fun saveAccessToken(accessToken: String, refreshToken: String) {
//        saveTokensUseCase.execute(accessToken, refreshToken)
//    }
}
