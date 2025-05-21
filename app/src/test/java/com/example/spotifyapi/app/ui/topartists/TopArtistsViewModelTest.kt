package com.example.spotifyapi.app.ui.topartists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.paging.map
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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TopArtistsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // 🔹 Garante execução síncrona do LiveData

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TopArtistsViewModel
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val getTopArtistsUseCase: GetTopArtistsUseCase = mockk()

    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // 🔹 Configura Dispatcher para testes
        viewModel = TopArtistsViewModel(getUserProfileUseCase, getTopArtistsUseCase)
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
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))

        val fakeUserProfile = UserProfile("1", "User Name", mockkListImages)


        coEvery { getUserProfileUseCase.execute(any()) } returns fakeUserProfile

        viewModel.getUserProfile("token123")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { userProfileObserver.onChanged(fakeUserProfile) }
        coVerify(exactly = 1) { getUserProfileUseCase.execute("token123") }
    }

    // 🔹 Testando erro na busca do perfil do usuário
    @Test
    fun `getUserProfile should update errorLiveData when use case throws exception`() = runTest {
        coEvery { getUserProfileUseCase.execute(any()) } throws Exception("Erro ao buscar perfil do usuário")

        viewModel.getUserProfile("token123")
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        verify { errorObserver.onChanged("Erro ao buscar perfil do usuário") }
        coVerify(exactly = 1) { getUserProfileUseCase.execute("token123") }
    }

    @Test
    fun `getArtistsPagingData should return valid PagingData`() = runTest {
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )


        coEvery { getTopArtistsUseCase.getFromApi(any(), any(), any()) } returns TopArtistsResponse(
            items = fakeArtists, total = 2, limit = 20, offset = 0, href = "href", next = "next", previous = "previous"
        )

        val result = viewModel.getArtistsPagingData("token123")

        assertNotNull(result) // verificamos se o fluxo não é nulo
    }
}