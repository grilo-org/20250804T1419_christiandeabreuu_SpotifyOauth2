package com.example.spotifyapi.app.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.data.model.Image
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.data.model.UserProfile
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // 🔹 Garante execução síncrona do LiveData

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProfileViewModel
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()

    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // 🔹 Configura Dispatcher para testes
        viewModel = ProfileViewModel(getUserProfileUseCase)
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
}