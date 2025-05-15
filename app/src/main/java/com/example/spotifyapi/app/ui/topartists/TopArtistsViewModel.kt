package com.example.spotifyapi.app.ui.topartists

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.spotifyapi.app.data.SpotifyAuthHelper
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TopArtistsViewModel(
    private val useCase: GetTopArtistsUseCase,
    private val spotifyApiService: SpotifyApiService,
    private val spotifyAuthHelper: SpotifyAuthHelper,
    private val topArtistsUseCase: GetTopArtistsUseCase
) : ViewModel() {

    private val _artistsLiveData = MutableLiveData<List<Artist>>()
    val artistsLiveData: LiveData<List<Artist>> get() = _artistsLiveData

    init {
        getTopArtists(accessToken = "")
    }


    fun getTopArtists(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val artists = useCase.execute(accessToken)
            _artistsLiveData.postValue(artists)
        }
    }

    fun getUserProfile(accessToken: String) = liveData(Dispatchers.IO) {
        try {
            val userProfile = spotifyApiService.getUserProfile("Bearer $accessToken")
            emit(userProfile)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                emit(null) // Emitir nulo em caso de erro 401
            } else {
                emit(null)
            }
        } catch (e: IOException) {
            emit(null)
        } catch (e: Exception) {
            emit(null)
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


