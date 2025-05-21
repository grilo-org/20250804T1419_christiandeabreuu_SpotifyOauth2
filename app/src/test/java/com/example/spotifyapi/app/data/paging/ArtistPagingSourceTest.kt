package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse

class ArtistPagingSourceTest {

    private lateinit var pagingSource: ArtistPagingSource
    private val getTopArtistsUseCase: GetTopArtistsUseCase = mockk()

    @Before
    fun setup() {
        pagingSource = ArtistPagingSource(getTopArtistsUseCase, "token123")
    }

    // 🔹 Testando carregamento de página com sucesso
    @Test
    fun `load should return page when data is available`() = runBlocking {
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )

        coEvery { getTopArtistsUseCase.getFromApi(any(), any(), any()) } returns TopArtistsResponse(
            items = fakeArtists, total = 2, limit = 20, offset = 0, href = "href", next = "next", previous = "previous"
        )

        val result = pagingSource.load(LoadParams.Refresh(0, 20, false))

        assertTrue(result is LoadResult.Page)
        assertEquals(fakeArtists, (result as LoadResult.Page).data)

        coVerify(exactly = 1) { getTopArtistsUseCase.getFromApi("token123", 0, any()) }
    }

    // 🔹 Testando quando API falha e retorna erro
    @Test
    fun `load should return error when API fails`() = runBlocking {
        coEvery { getTopArtistsUseCase.getFromApi(any(), any(), any()) } throws Exception("Erro na API")

        val result = pagingSource.load(LoadParams.Refresh(0, 20, false))

        assertTrue(result is LoadResult.Error)
        coVerify(exactly = 1) { getTopArtistsUseCase.getFromApi("token123", 0, any()) }
    }

    // 🔹 Testando `getRefreshKey` quando não há estado inicial
    @Test
    fun `getRefreshKey should return null when state is empty`() {
        val refreshKey = pagingSource.getRefreshKey(mockk(relaxed = true))
        assertNull(refreshKey)
    }
}