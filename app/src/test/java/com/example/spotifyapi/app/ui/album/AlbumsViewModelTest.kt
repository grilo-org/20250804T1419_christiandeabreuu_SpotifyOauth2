package com.example.spotifyapi.app.ui.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import com.example.spotifyapi.app.data.model.Album
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // 🔹 Garante execução síncrona do LiveData

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AlbumsViewModel
    private val getAlbumsUseCase: GetAlbumsUseCase = mockk()

    private val albumsObserver: Observer<List<Album>> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // 🔹 Configura Dispatcher para testes
        viewModel = AlbumsViewModel(getAlbumsUseCase)
        viewModel.albumsLiveData.observeForever(albumsObserver)
        viewModel.errorLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 🔹 Testando busca bem-sucedida de álbuns
    @Test
    fun `getAlbums should update albumsLiveData when use case returns data`() = runTest {
        val fakeAlbums = listOf(
            Album("1", "Album 1", "release_date", emptyList(), "artist123"),
            Album("2", "Album 2", "release_date", emptyList(), "artist123")
        )

        coEvery { getAlbumsUseCase.execute(any(), any()) } returns fakeAlbums

        viewModel.getAlbums("token123", "artist123")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { albumsObserver.onChanged(fakeAlbums) }
        coVerify(exactly = 1) { getAlbumsUseCase.execute("token123", "artist123") }
    }

    // 🔹 Testando erro na busca de álbuns
    @Test
    fun `getAlbums should update errorLiveData when use case throws exception`() = runTest {
        coEvery { getAlbumsUseCase.execute(any(), any()) } throws Exception("Erro ao buscar álbuns")

        viewModel.getAlbums("token123", "artist123")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { errorObserver.onChanged("Erro ao buscar álbuns") }
        coVerify(exactly = 1) { getAlbumsUseCase.execute("token123", "artist123") }
    }
}