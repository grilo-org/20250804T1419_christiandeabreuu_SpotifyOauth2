import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.UserProfileRepositoryImpl
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileRepositoryImplTest {

    private lateinit var apiService: SpotifyApiService
    private lateinit var spotifyDAO: SpotifyDAO
    private lateinit var tokenRepository: TokenRepository
    private lateinit var repository: UserProfileRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        spotifyDAO = mockk()
        tokenRepository = mockk()
        repository = UserProfileRepositoryImpl(apiService, spotifyDAO, tokenRepository)
    }

    @Test
    fun `getUserProfileFromApi returns UserProfile when successful`() = runTest {
        // Arrange
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val token = "token123"
        val userProfile = UserProfile("id", "name", mockkListImages)
        coEvery { tokenRepository.getAccessToken() } returns token
        coEvery { apiService.getUserProfile(token.addBearer()) } returns userProfile

        // Act
        val result = repository.getUserProfileFromApi()

        // Assert
        assertEquals(userProfile, result)
        coVerify { tokenRepository.getAccessToken() }
        coVerify { apiService.getUserProfile("Bearer $token") }
    }

    @Test
    fun `getUserProfileFromApi returns null when exception is thrown`() = runTest {
        // Arrange
        val token = "token123"
        coEvery { tokenRepository.getAccessToken() } returns token
        coEvery { apiService.getUserProfile(token.addBearer()) } throws RuntimeException("Network error")

        // Act
        val result = repository.getUserProfileFromApi()

        // Assert
        assertNull(result)
        coVerify { tokenRepository.getAccessToken() }
        coVerify { apiService.getUserProfile("Bearer $token") }
    }


    @Test
    fun `getLocalUserProfile returns local user profile from DAO`() = runTest {
        // Arrange
        val userProfileDB = UserProfileDB(0, "id", "name", "email")
        coEvery { spotifyDAO.getLocalUserProfile() } returns userProfileDB

        // Act
        val result = repository.getLocalUserProfile()

        // Assert
        assertEquals(userProfileDB, result)
        coVerify { spotifyDAO.getLocalUserProfile() }
    }
}
