import androidx.paging.PagingSource
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtistPagingSourceTest {

    private lateinit var pagingSource: ArtistPagingSource

    @MockK
    private lateinit var useCaseTopArtists: GetTopArtistsUseCase

    private val artistId = "test_artist_id"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        pagingSource = ArtistPagingSource(useCaseTopArtists)
    }

    @Test
    fun `load deve retornar erro quando a API falha`() = runTest {
        coEvery { useCaseTopArtists.fetchAndSaveTopArtists(0) } throws Exception("Erro na API")

        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assert(result is PagingSource.LoadResult.Error)
    }
}