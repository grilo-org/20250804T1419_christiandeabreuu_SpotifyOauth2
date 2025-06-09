import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepositoryImpl
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistRepositoryImplTest {

    private lateinit var apiService: SpotifyApiService
    private lateinit var tokenRepository: TokenRepository
    private lateinit var repository: CreatePlaylistRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        tokenRepository = mockk()
        repository = CreatePlaylistRepositoryImpl(apiService, tokenRepository)
    }

    @Test
    fun `createPlaylist returns true when response is successful`() = runTest {
        // Arrange
        val playlistName = "My Playlist"
        val token = "token123"
        val bearerToken = token.addBearer()
        val request = CreatePlaylistRequest(name = playlistName, public = true)
        val response = Response.success(Unit)

        coEvery { tokenRepository.getAccessToken() } returns token
        coEvery { apiService.createPlaylist(bearerToken, request) } returns response

        // Act
        val result = repository.createPlaylist(playlistName)

        // Assert
        assertTrue(result)
        coVerify { tokenRepository.getAccessToken() }
        coVerify { apiService.createPlaylist(bearerToken, request) }
    }

    @Test
    fun `createPlaylist returns false when response is not successful`() = runTest {
        // Arrange
        val playlistName = "My Playlist"
        val token = "token123"
        val bearerToken = token.addBearer()
        val request = CreatePlaylistRequest(name = playlistName, public = true)
        val errorResponse = Response.error<Unit>(400, "error".toResponseBody(null))

        coEvery { tokenRepository.getAccessToken() } returns token
        coEvery { apiService.createPlaylist(bearerToken, request) } returns errorResponse

        // Act
        val result = repository.createPlaylist(playlistName)

        // Assert
        assertFalse(result)
        coVerify { tokenRepository.getAccessToken() }
        coVerify { apiService.createPlaylist(bearerToken, request) }
    }
}