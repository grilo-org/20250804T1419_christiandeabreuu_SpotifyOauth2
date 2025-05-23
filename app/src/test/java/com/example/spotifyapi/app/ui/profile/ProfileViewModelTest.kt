package com.example.spotifyapi.app.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProfileViewModel
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val resourcesPlugin: ResourcesPlugin = mockk()
    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)
    private val errorObserver: Observer<String> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(getUserProfileUseCase, resourcesPlugin)
        viewModel.userProfileLiveData.observeForever(userProfileObserver)
        viewModel.errorLiveData.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

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
        advanceUntilIdle() // 🔹 Aguarda execução das corrotinas

        //Then
        verify { errorObserver.onChanged("Erro ao buscar perfil do usuário") }
        coVerify(exactly = 1) { getUserProfileUseCase.getUserProfile() }
    }
}