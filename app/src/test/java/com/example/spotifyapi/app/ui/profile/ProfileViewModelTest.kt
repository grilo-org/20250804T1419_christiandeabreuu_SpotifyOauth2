import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.ui.profile.ProfileViewModel
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
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userProfileUseCase: GetUserProfileUseCase = mockk()
    private val resourcesPlugin: ResourcesPlugin = mockk()
    private lateinit var viewModel: ProfileViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(userProfileUseCase, resourcesPlugin)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserProfile posts user profile when use case succeeds`() = runTest {
        val userProfile = UserProfile(id = "1", displayName = "Chris", images = emptyList())
        coEvery { userProfileUseCase.getUserProfile() } returns userProfile

        val observer = mockk<Observer<UserProfile?>>(relaxed = true)
        viewModel.userProfileLiveData.observeForever(observer)

        viewModel.getUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        verify { observer.onChanged(userProfile) }
        viewModel.userProfileLiveData.removeObserver(observer)
    }

    @Test
    fun `getUserProfile posts error when use case throws`() = runTest {
        val errorMsg = "Erro ao buscar perfil"
        coEvery { userProfileUseCase.getUserProfile() } throws Exception("fail")
        every { resourcesPlugin.searchProfileErrorMessage() } returns errorMsg

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.errorLiveData.observeForever(observer)

        viewModel.getUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        verify { observer.onChanged(errorMsg) }
        viewModel.errorLiveData.removeObserver(observer)
    }
}