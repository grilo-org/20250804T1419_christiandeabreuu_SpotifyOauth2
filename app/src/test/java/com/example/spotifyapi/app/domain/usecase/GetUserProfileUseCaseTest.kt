package com.example.spotifyapi.app.domain.usecase


import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfileDB
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetUserProfileUseCaseTest {

    private lateinit var useCase: GetUserProfileUseCase
    private val repository: UserProfileRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = GetUserProfileUseCase(repository)
    }

    @Test
    fun `execute should return user profile when API call is successful`() = runBlocking {
        // Given
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val fakeProfile = UserProfile("1", "User Name", mockkListImages)

        coEvery { repository.getUserProfileFromApi(any()) } returns fakeProfile
        coEvery { repository.insertLocalUserProfile(any()) } just Runs

        // When
        val result = useCase.execute("token123")

        // Then - retorna o user profile qdo a api é sucesso
        assertEquals(fakeProfile, result)
        coVerify(exactly = 1) { repository.getUserProfileFromApi("token123") }
        coVerify(exactly = 1) { repository.insertLocalUserProfile(fakeProfile.toUserProfileDB()) }
    }

    @Test
    fun `execute should return null when both API and local storage fail`() = runBlocking {
        // Given
        coEvery { repository.getUserProfileFromApi(any()) } returns null
        coEvery { repository.getLocalUserProfile() } returns null

        // When
        val result = useCase.execute("token123")

        // Then - retorna null quando a api e o banco de dados falham
        assertNull(result)
        coVerify(exactly = 1) { repository.getUserProfileFromApi("token123") }
        coVerify(exactly = 1) { repository.getLocalUserProfile() }
    }
}
