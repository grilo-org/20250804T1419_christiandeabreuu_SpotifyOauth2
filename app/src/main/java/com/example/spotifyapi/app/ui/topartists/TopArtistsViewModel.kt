//package com.example.spotifyapi.app.ui.topartists
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import androidx.paging.cachedIn
//import com.example.spotifyapi.app.data.model.ArtistResponse
//import com.example.spotifyapi.app.data.model.UserProfile
//import com.example.spotifyapi.app.data.pagingtesteeeeee.ArtistPagingSource
//import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
//import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//class TopArtistsViewModel(
//    private val topArtistsUseCase: GetTopArtistsUseCase,
//    private val userProfileUseCase: GetUserProfileUseCase
//) : ViewModel() {
//
//    private val _artistsLiveData = MutableLiveData<List<ArtistResponse>>()
//    val artistsLiveData: LiveData<List<ArtistResponse>> get() = _artistsLiveData
//
//    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
//    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData
//
//    private val _errorLiveData = MutableLiveData<String>()
//    val errorLiveData: LiveData<String> get() = _errorLiveData
//
//
//    fun getArtistsPagingData(accessToken: String): Flow<PagingData<ArtistResponse>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { ArtistPagingSource(topArtistsUseCase, accessToken) }
//        ).flow.cachedIn(viewModelScope)
//    }
//
//
//    fun getTopArtists(accessToken: String, offset: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val artists = topArtistsUseCase.getTopArtisFromApi(accessToken, offset)
//                _artistsLiveData.postValue(artists)
//            } catch (e: Exception) {
//                _errorLiveData.postValue("Erro ao buscar artistas")
//            }
//        }
//    }
//
//    fun getUserProfile(accessToken: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val userProfile = userProfileUseCase.execute(accessToken)
//                _userProfileLiveData.postValue(userProfile)
//            } catch (e: Exception) {
//                _errorLiveData.postValue("Erro ao buscar perfil do usuário")
//            }
//        }
//    }
//}
//
//
