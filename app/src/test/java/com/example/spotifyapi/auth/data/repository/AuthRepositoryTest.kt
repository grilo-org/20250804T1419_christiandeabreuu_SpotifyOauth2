import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.networking.AuthApiService
import com.example.spotifyapi.auth.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private val apiService: AuthApiService = mockk()

    @Before
    fun setup() {
        authRepository = AuthRepository(apiService)
    }

    @Test
    fun `getAccessToken should return SpotifyTokens when API call is successful`() = runBlocking {
        //Given
        val fakeTokens = SpotifyTokens("access123", "refresh456")
        coEvery {
            apiService.getAccessToken(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.success(fakeTokens)

        //When
        val result = authRepository.getAccessToken("authCode", "redirectUri")

        //Then - sucesso na obtenção do token
        assertTrue(result.isSuccess)
        assertEquals(fakeTokens, result.getOrNull())
        coVerify(exactly = 1) { apiService.getAccessToken(any(), any(), any(), any(), any()) }

    }

    @Test
    fun `getAccessToken should return failure when API call is unsuccessful`() = runBlocking {
        //Given
        coEvery {
            apiService.getAccessToken(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error(400, "Bad Request".toResponseBody(null))

        //When
        val result = authRepository.getAccessToken("authCode", "redirectUri")

        //Then - erro na API
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Erro: 400") == true)
    }

    @Test
    fun `getAccessToken should return failure when API call throws exception`() = runBlocking {
        //Given
        coEvery {
            apiService.getAccessToken(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception("Erro inesperado")

        //When
        val result = authRepository.getAccessToken("authCode", "redirectUri")

        //Then - erro ao chamar a API (Exceção lançada)
        assertTrue(result.isFailure)
        assertEquals("Erro inesperado", result.exceptionOrNull()?.message)
    }
}