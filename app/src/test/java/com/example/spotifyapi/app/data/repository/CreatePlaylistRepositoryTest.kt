import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CreatePlaylistRepositoryTest {

    @RelaxedMockK
    private lateinit var apiService: SpotifyApiService

    private lateinit var repository: CreatePlaylistRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CreatePlaylistRepository(apiService)
    }

    @Test
    fun `createPlaylist should return true when API call is successful`() = runBlocking {
        // Given
        val fakeResponse = mockk<Response<Unit>> {
            every { isSuccessful } returns true
        }

        coEvery { apiService.createPlaylist(any(), any()) } returns fakeResponse

        // When
        val result = repository.createPlaylist("token123", "Minha Playlist")

        // Then - Verificando se o resultado é verdadeiro
        assertTrue(result)
        coVerify(exactly = 1) { apiService.createPlaylist("Bearer token123", any()) }
    }

    @Test
    fun `createPlaylist should return false when API call fails`() = runBlocking {
        // Given
        val fakeResponse = mockk<Response<Unit>> {
            every { isSuccessful } returns false
        }

        coEvery { apiService.createPlaylist(any(), any()) } returns fakeResponse

        // When
        val result = repository.createPlaylist("token123", "Minha Playlist")

        // Then - Verificando se o resultado é falso
        assertFalse(result)
        coVerify(exactly = 1) { apiService.createPlaylist("Bearer token123", any()) }
    }
}