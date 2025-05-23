package com.example.spotifyapi.app.ui.createplaylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CreatePlaylistViewModel
    private val createPlaylistUseCase: CreatePlaylistUseCase = mockk()
    private val resourcesPlugin: ResourcesPlugin = mockk()
    private val successObserver: Observer<Result<String>> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreatePlaylistViewModel(createPlaylistUseCase, resourcesPlugin)
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
        // Given
        val fakeResponse = "Playlist 'Minha Playlist' criada com sucesso!"
        coEvery { createPlaylistUseCase.createPlaylist(any()) } returns fakeResponse

        // When
        viewModel.createPlaylist("Minha Playlist")
        advanceUntilIdle()

        // Then
        verify { successObserver.onChanged(Result.success(fakeResponse)) }
        coVerify(exactly = 1) { createPlaylistUseCase.createPlaylist( "Minha Playlist") }
    }

    @Test
    fun `createPlaylist should update errorLiveData when use case throws exception`() = runTest {
        // Given
        coEvery { createPlaylistUseCase.createPlaylist(any()) } throws Exception("Erro ao criar playlist")

        // When
        viewModel.createPlaylist( "Minha Playlist")
        advanceUntilIdle()

        // Then - Teste de erro na criação de playlist
        verify { errorObserver.onChanged("Erro ao criar playlist") }
        coVerify(exactly = 1) { createPlaylistUseCase.createPlaylist( "Minha Playlist") }
    }
}