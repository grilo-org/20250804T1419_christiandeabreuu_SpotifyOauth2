package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class AlbumsPagingSourceTest {

    private lateinit var pagingSource: AlbumsPagingSource

    @MockK
    private lateinit var repository: AlbumsRepository

    private val artistId = "test_artist_id"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        pagingSource = AlbumsPagingSource(repository, artistId)
    }

    @Test
    fun `load deve retornar dados corretamente`() = runTest {
        val fakeAlbums = List(20) {
            Album(
                id = it.toString(),
                name = "Album $it",
                "release_date",
                emptyList(),
                artistId
            )
        }

        // Mockando chamadas do repositório
        coEvery { repository.getAlbumsFromApi(artistId, 0) } returns mockk {
            every { items } returns fakeAlbums
        }
        coEvery { repository.insertLocalAlbums(any()) } just Runs
        coEvery { repository.getAlbumsFromDB(artistId) } returns emptyList()

        // Chamando o método da PagingSource
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        // Validando o retorno esperado
        assertEquals(
            PagingSource.LoadResult.Page(
                data = fakeAlbums, prevKey = null, nextKey = 20
            ), result
        )
    }

    @Test
    fun `load deve retornar erro quando a API falha`() = runTest {
        coEvery { repository.getAlbumsFromApi(artistId, 0) } throws Exception("Erro na API")

        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assert(result is PagingSource.LoadResult.Error)
    }
}