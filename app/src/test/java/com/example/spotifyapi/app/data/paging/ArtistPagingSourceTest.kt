package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ArtistPagingSourceTest {

    private lateinit var pagingSource: ArtistPagingSource
    private val getTopArtistsUseCase: GetTopArtistsUseCase = mockk()

    @Before
    fun setup() {
        pagingSource = ArtistPagingSource(getTopArtistsUseCase, "token123")
    }

    @Test
    fun `load should return page when API returns data`() = runBlocking {
        // Given
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )
        coEvery { getTopArtistsUseCase.getFromApi(any(), any(), any()) } returns TopArtistsResponse(
            fakeArtists,
            2,
            20,
            0,
            "href",
            "next",
            "previous"
        )

        // When
        val result = pagingSource.load(LoadParams.Refresh(0, 20, false))

        // Then
        assertTrue(result is LoadResult.Page)
        assertEquals(fakeArtists, (result as LoadResult.Page).data)
        assertEquals(null, result.prevKey)
        assertEquals(20, result.nextKey)

        coVerify(exactly = 1) { getTopArtistsUseCase.getFromApi("token123", 0, any()) }
    }

    @Test
    fun `load should return error when API fails`() = runBlocking {
        // Given
        coEvery {
            getTopArtistsUseCase.getFromApi(
                any(),
                any(),
                any()
            )
        } throws Exception("Erro na API")

        // When
        val result = pagingSource.load(LoadParams.Refresh(0, 20, false))

        // Then - Deve retornar um LoadResult.Error
        assertTrue(result is LoadResult.Error)
        coVerify(exactly = 1) { getTopArtistsUseCase.getFromApi("token123", 0, any()) }
    }

    @Test
    fun `getRefreshKey should return null when state is empty`() {
        // Given
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)
        every { mockState.anchorPosition } returns null

        // When
        val refreshKey = pagingSource.getRefreshKey(mockState)

        // Then - Deve retornar null
        assertNull(refreshKey)
    }

    @Test
    fun `getRefreshKey should return null when state has no anchorPosition`() {
        // Given
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)
        every { mockState.anchorPosition } returns null

        // When
        val refreshKey = pagingSource.getRefreshKey(mockState)

        // Then - Deve retornar null
        assertNull(refreshKey)
    }

    @Test
    fun `getRefreshKey should return correct prevKey`() {
        // Given
        val mockState = mockk<PagingState<Int, ArtistResponse>>(relaxed = true)
        every { mockState.anchorPosition } returns 10
        every { mockState.closestPageToPosition(10)?.prevKey } returns 20

        // When
        val refreshKey = pagingSource.getRefreshKey(mockState)

        // Then - Deve retornar prevKey + pageSize (20 + 20)
        assertEquals(40, refreshKey)
    }
}

