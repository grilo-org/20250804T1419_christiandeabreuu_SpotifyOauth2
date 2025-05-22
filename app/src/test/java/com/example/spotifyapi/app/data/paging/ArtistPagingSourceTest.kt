package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
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

    @Test
    fun `load should return page when API returns data`() = runBlocking {
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )

        coEvery { getTopArtistsUseCase.getFromApi(any(), any(), any()) } returns
                TopArtistsResponse(fakeArtists, 2, 20, 0, "href", "next", "previous")

        val result = pagingSource.load(LoadParams.Refresh(0, 20, false))

        assertTrue(result is LoadResult.Page)
        assertEquals(fakeArtists, (result as LoadResult.Page).data)
        assertEquals(null, result.prevKey) // 🔹 Como é primeira página, prevKey deve ser null
        assertEquals(20, result.nextKey)   // 🔹 Próxima página começa em 20

        coVerify(exactly = 1) { getTopArtistsUseCase.getFromApi("token123", 0, any()) }
    }

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
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)

        every { mockState.anchorPosition } returns null // 🔹 Garante que retorna null corretamente

        val refreshKey = pagingSource.getRefreshKey(mockState)
        assertNull(refreshKey) // 🔹 Agora o teste deve rodar corretamente!
    }

    @Test
    fun `getRefreshKey should return null when state has no anchorPosition`() {
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)
        every { mockState.anchorPosition } returns null

        val refreshKey = pagingSource.getRefreshKey(mockState)
        assertNull(refreshKey)
    }

    @Test
    fun `getRefreshKey should return correct prevKey`() {
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)
        every { mockState.anchorPosition } returns 10
        every { mockState.closestPageToPosition(10)?.prevKey } returns 20

        val refreshKey = pagingSource.getRefreshKey(mockState)
        assertEquals(40, refreshKey) //  20 + 20 = 40
    }

}

