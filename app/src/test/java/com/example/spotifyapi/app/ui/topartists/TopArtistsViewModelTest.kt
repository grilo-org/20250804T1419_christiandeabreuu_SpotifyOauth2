package com.example.spotifyapi.app.ui.topartists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class TopArtistsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TopArtistsViewModel
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val artistPagingSource: ArtistPagingSource = mockk()
    private val getTopArtistsUseCase: GetTopArtistsUseCase = mockk()
    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TopArtistsViewModel(getUserProfileUseCase, artistPagingSource)
        viewModel.userProfileLiveData.observeForever(userProfileObserver)
        viewModel.errorLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 🔹 Testando busca bem-sucedida do perfil do usuário
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
        coVerify(exactly = 1) { getUserProfileUseCase.getUserProfile()}
    }

    @Test
    fun `getUserProfile should update errorLiveData when use case throws exception`() = runTest {
        //Given
        coEvery { getUserProfileUseCase.getUserProfile() } throws Exception("Erro ao buscar perfil do usuário")

        //When
        viewModel.getUserProfile()
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        //Then - teste de erro na busca do perfil do usuário
        verify { errorObserver.onChanged("Erro ao buscar perfil do usuário") }
        coVerify(exactly = 1) { getUserProfileUseCase.getUserProfile() }
    }

    @Test
    fun `getArtistsPagingData should return valid PagingData`() = runTest {
        //Given
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )

        coEvery { getTopArtistsUseCase.fetchAndSaveTopArtists(any(), any()) } returns TopArtistsResponse(
            items = fakeArtists, total = 2, limit = 20, offset = 0, href = "href", next = "next", previous = "previous"
        )
        //When

        val result = viewModel.getArtistsPagingData()

        //Then - Verifica se o valor nao é nulo
        assertNotNull(result)
    }
}