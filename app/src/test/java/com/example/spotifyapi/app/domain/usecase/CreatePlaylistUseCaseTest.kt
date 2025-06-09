import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistUseCaseTest {

    private lateinit var repository: CreatePlaylistRepository
    private lateinit var resources: ResourcesPlugin
    private lateinit var useCase: CreatePlaylistUseCase

    @Before
    fun setUp() {
        repository = mockk()
        resources = mockk()
        useCase = CreatePlaylistUseCase(repository, resources)
    }

    @Test
    fun `createPlaylist returns success message when repository returns true`() = runTest {
        // Arrange
        val playlistName = "My Playlist"
        coEvery { repository.createPlaylist(playlistName) } returns true
        every { resources.playlistCreatedSuccessMessage(playlistName) } returns "Playlist criada com sucesso: $playlistName"
        every { resources.validatePlaylistNameMessage() } returns "O nome da playlist não pode ser vazio"
        every { resources.createPlaylistErrorMessage() } returns "Erro ao criar playlist"

        // Act
        val result = useCase.createPlaylist(playlistName)

        // Assert
        assertEquals("Playlist criada com sucesso: $playlistName", result)
    }

    @Test(expected = Exception::class)
    fun `execute should throw exception when repository fails to create playlist`(): Unit =
        runBlocking {
            coEvery { repository.createPlaylist(any()) } returns false

            // Teste de falha ao criar playlist
            useCase.createPlaylist("token123")
        }

    @Test
    fun `createPlaylist throws Exception with error message when repository returns false`() =
        runTest {
            // Arrange
            val playlistName = "My Playlist"
            coEvery { repository.createPlaylist(playlistName) } returns false
            every { resources.playlistCreatedSuccessMessage(playlistName) } returns "Playlist criada com sucesso: $playlistName"
            every { resources.validatePlaylistNameMessage() } returns "O nome da playlist não pode ser vazio"
            every { resources.createPlaylistErrorMessage() } returns "Erro ao criar playlist"

            // Act & Assert
            try {
                useCase.createPlaylist(playlistName)
                fail("Exception was expected")
            } catch (e: Exception) {
                assertEquals("Erro ao criar playlist", e.message)
            }
        }

    @Test
    fun `createPlaylist throws IllegalArgumentException when playlist name is blank`() = runTest {
        // Arrange
        val playlistName = "  "
        every { resources.validatePlaylistNameMessage() } returns "O nome da playlist não pode ser vazio"

        // Act & Assert
        try {
            useCase.createPlaylist(playlistName)
            fail("IllegalArgumentException era esperada")
        } catch (e: IllegalArgumentException) {
            assertEquals("O nome da playlist não pode ser vazio", e.message)
        }
    }
}