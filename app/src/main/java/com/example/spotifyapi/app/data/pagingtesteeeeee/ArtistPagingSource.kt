//package com.example.spotifyapi.app.data.pagingtesteeeeee
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.spotifyapi.app.data.model.ArtistResponse
//import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
//import com.example.spotifyapi.app.ui.topartists.a.GetTopArtistsUseCase
//
//class ArtistPagingSource(
//    private val useCaseTopArtists: GetTopArtistsUseCase, private val accessToken: String
//) : PagingSource<Int, ArtistResponse>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistResponse> {
//        return try {
//            val offset = params.key ?: 1
//            Log.d("PagingSource", "🔄 Carregando artistas - Offset: $offset")
//
//            val response = useCaseTopArtists.getTopArtisFromApi(
//                accessToken,
//                offset
//            ) // 🔹 Apenas chama o `Use Case`
//            val uniqueArtists = response.distinctBy { it.id }
//            LoadResult.Page(
//                data = uniqueArtists,
//                prevKey = if (offset == 0) null else offset - 20, // 🔹 Garantindo que nunca seja negativo!
//                nextKey = if (response.isEmpty()) null else offset + 20 // 🔹 Impede que a paginação avance sem dados! // 🔹 Corrigindo para avançar corretamente
//            )
//        } catch (e: Exception) {
//            Log.e("PagingSource", "❌ Erro ao carregar artistas: ${e.message}")
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, ArtistResponse>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
//        }
//    }
//}