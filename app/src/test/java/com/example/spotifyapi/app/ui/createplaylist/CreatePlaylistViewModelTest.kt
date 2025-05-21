package com.example.spotifyapi.app.ui.createplaylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // 🔹 Garante execução síncrona do LiveData

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CreatePlaylistViewModel
    private val createPlaylistUseCase: CreatePlaylistUseCase = mockk()

    private val successObserver: Observer<Result<String>> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // 🔹 Configura Dispatcher para testes
        viewModel = CreatePlaylistViewModel(createPlaylistUseCase)
        viewModel.createPlaylistLiveData.observeForever(successObserver)
        viewModel.errorLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 🔹 Testando criação bem-sucedida da playlist
    @Test
    fun `createPlaylist should update createPlaylistLiveData when use case returns success`() = runTest {
        val fakeResponse = "Playlist 'Minha Playlist' criada com sucesso!"

        coEvery { createPlaylistUseCase.execute(any(), any()) } returns fakeResponse

        viewModel.createPlaylist("token123", "Minha Playlist")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { successObserver.onChanged(Result.success(fakeResponse)) }
        coVerify(exactly = 1) { createPlaylistUseCase.execute("token123", "Minha Playlist") }
    }

    // 🔹 Testando erro na criação de playlist
    @Test
    fun `createPlaylist should update errorLiveData when use case throws exception`() = runTest {
        coEvery { createPlaylistUseCase.execute(any(), any()) } throws Exception("Erro ao criar playlist")

        viewModel.createPlaylist("token123", "Minha Playlist")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { errorObserver.onChanged("Erro ao criar playlist") }
        coVerify(exactly = 1) { createPlaylistUseCase.execute("token123", "Minha Playlist") }
    }
}