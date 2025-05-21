import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.data.networking.AuthApiService
import com.example.spotifyapi.authenticate.data.repository.AuthRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
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

    // 🔹 Testando sucesso na obtenção do token
    @Test
    fun `getAccessToken should return SpotifyTokens when API call is successful`() = runBlocking {
        val fakeTokens = SpotifyTokens("access123", "refresh456")

        coEvery { apiService.getAccessToken(any(), any(), any(), any(), any()) } returns Response.success(fakeTokens)

        val result = authRepository.getAccessToken("authCode", "redirectUri")

        assertTrue(result.isSuccess)
        assertEquals(fakeTokens, result.getOrNull())

        coVerify(exactly = 1) { apiService.getAccessToken(any(), any(), any(), any(), any(),) }

    }

    // 🔹 Testando erro na API com resposta não bem-sucedida
    @Test
    fun `getAccessToken should return failure when API call is unsuccessful`() = runBlocking {
        coEvery { apiService.getAccessToken(any(), any(), any(), any(), any()) } returns Response.error(400, ResponseBody.create(null, "Bad Request"))

        val result = authRepository.getAccessToken("authCode", "redirectUri")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Erro: 400") == true)

        coVerify(exactly = 1) { apiService.getAccessToken(any(), any(), any(), any(), any(),) }
    }

    // 🔹 Testando erro ao chamar a API (Exceção lançada)
    @Test
    fun `getAccessToken should return failure when API call throws exception`() = runBlocking {
        coEvery { apiService.getAccessToken(any(), any(), any(), any(), any()) } throws Exception("Erro inesperado")

        val result = authRepository.getAccessToken("authCode", "redirectUri")

        assertTrue(result.isFailure)
        assertEquals("Erro inesperado", result.exceptionOrNull()?.message)

        coVerify(exactly = 1) { apiService.getAccessToken(any(), any(), any(), any(), any(),) }
    }
}