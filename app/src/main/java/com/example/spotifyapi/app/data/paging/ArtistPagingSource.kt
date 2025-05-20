package com.example.spotifyapi.app.data.paging

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.ImageArtistResponse
import com.example.spotifyapi.utils.NetworkUtils

class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase,
    private val accessToken: String,
) : PagingSource<Int, ArtistResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistResponse> {
        return try {
            val nextPageNumber = params.key ?: 0
            Log.d("ArtistPagingSource", "Carregando artistas com offset: $nextPageNumber")

            val response: List<ArtistResponse> = try {
                useCaseTopArtists.getFromApi(accessToken, nextPageNumber).items
            } catch (apiException: Exception) {
                Log.e("ArtistPagingSource", "Erro ao carregar da API: ${apiException.message}")
                emptyList()
            }

            val finalResponse = response.ifEmpty {
                Log.d("ArtistPagingSource", "Buscando artistas no banco de dados")
                useCaseTopArtists.getFromDBWithOffsetAndLimit(
                    20,
                    nextPageNumber
                ).artists.map { artistWithImages ->
                    ArtistResponse(
                        id = artistWithImages.artist.id,
                        name = artistWithImages.artist.name,
                        popularity = artistWithImages.artist.popularity,
                        images = artistWithImages.images.map { image ->
                            ImageArtistResponse(url = image.url)
                        }
                    )
                }
            }

            LoadResult.Page(
                data = finalResponse,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 20,
                nextKey = if (finalResponse.isEmpty()) null else nextPageNumber + 20
            )
        } catch (e: Exception) {
            Log.e("ArtistPagingSource", "Erro ao carregar artistas: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }
}