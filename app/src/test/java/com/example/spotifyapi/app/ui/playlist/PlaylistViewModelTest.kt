package com.example.spotifyapi.app.ui.playlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PlaylistViewModel
    private val getPlaylistsUseCase: GetPlaylistsUseCase = mockk()
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val playlistsObserver: Observer<List<Playlist>> = mockk(relaxed = true)
    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)
    private val resourcesPlugin: ResourcesPlugin = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PlaylistViewModel(getPlaylistsUseCase, getUserProfileUseCase, resourcesPlugin)
        viewModel.playlistsLiveData.observeForever(playlistsObserver)
        viewModel.userProfileLiveData.observeForever(userProfileObserver)
        viewModel.errorLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPlaylists should update playlistsLiveData when use case returns data`() = runTest {
        //Given
        val mockkListImages: List<Image> = listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val mockOwner : Owner = mockk(relaxed = true)
        val fakePlaylists = listOf(Playlist("id" , "name", "description", mockOwner, 0, mockkListImages))
        Playlist("id1" , "name1", "description1", mockOwner, 1, mockkListImages)
        coEvery { getPlaylistsUseCase.getPlaylists() } returns fakePlaylists

        //When
        viewModel.getPlaylists()
        advanceUntilIdle()

        //Then - Teste de busca bem-sucedida de playlists
        verify { playlistsObserver.onChanged(fakePlaylists) }
        coVerify(exactly = 1) { getPlaylistsUseCase.getPlaylists() }
    }

    @Test
    fun `getPlaylists should update errorLiveData when use case throws exception`() = runTest {
        //Given
        coEvery { getPlaylistsUseCase.getPlaylists() } throws Exception("Erro ao buscar playlists")

        //When
        viewModel.getPlaylists()
        advanceUntilIdle()

        //Then - Teste de erro na busca de playlists
        verify { errorObserver.onChanged("Erro ao buscar playlists") }
        coVerify(exactly = 1) { getPlaylistsUseCase.getPlaylists() }
    }

    @Test
    fun `getUserProfile should update userProfileLiveData when use case returns data`() = runTest {
        //Given
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val fakeUserProfile = UserProfile("1", "User Name", mockkListImages)
        coEvery { getUserProfileUseCase.getUserProfile() } returns fakeUserProfile

        //When
        viewModel.getUserProfile()
        advanceUntilIdle()

        //Then - Teste de busca bem-sucedida do perfil do usuário
        verify { userProfileObserver.onChanged(fakeUserProfile) }
        coVerify(exactly = 1) { getUserProfileUseCase.getUserProfile() }
    }

    @Test
    fun `getUserProfile should update errorLiveData when use case throws exception`() = runTest {
        //Given
        coEvery { getUserProfileUseCase.getUserProfile() } throws Exception("Erro ao buscar perfil do usuário")

        //When
        viewModel.getUserProfile()
        advanceUntilIdle()

        //Then - Teste de erro na busca do perfil do usuário
        verify { errorObserver.onChanged("Erro ao buscar perfil do usuário") }
        coVerify(exactly = 1) { getUserProfileUseCase.getUserProfile() }
    }
}