import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistViewModel
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistViewModelTest {

    // Permite LiveData emitir valores instantaneamente no teste
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val createPlaylistUseCase: CreatePlaylistUseCase = mockk()
    private val resourcesPlugin: ResourcesPlugin = mockk()
    private lateinit var viewModel: CreatePlaylistViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreatePlaylistViewModel(createPlaylistUseCase, resourcesPlugin)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createPlaylist posts success when useCase returns successfully`() = runTest {
        // Arrange
        val playlistName = "New Playlist"
        val expectedMessage = "Playlist criada!"
        coEvery { createPlaylistUseCase.createPlaylist(playlistName) } returns expectedMessage

        val observer = mockk<Observer<Result<String>>>(relaxed = true)
        viewModel.createPlaylistLiveData.observeForever(observer)

        // Act
        viewModel.createPlaylist(playlistName)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify {
            observer.onChanged(Result.success(expectedMessage))
        }
        viewModel.createPlaylistLiveData.removeObserver(observer)
    }

    @Test
    fun `createPlaylist posts error message when useCase throws exception`() = runTest {
        // Arrange
        val playlistName = "Bad Playlist"
        val errorMsg = "Erro ao criar playlist"
        coEvery { createPlaylistUseCase.createPlaylist(playlistName) } throws Exception("erro")
        every { resourcesPlugin.createPlaylistErrorMessage() } returns errorMsg

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.errorLiveData.observeForever(observer)

        // Act
        viewModel.createPlaylist(playlistName)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify { observer.onChanged(errorMsg) }
        viewModel.errorLiveData.removeObserver(observer)
    }
}