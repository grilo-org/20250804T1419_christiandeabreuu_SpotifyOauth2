package com.example.spotifyapi.auth.domain.usecase

import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExtractTokensUseCaseTest {

    private val extractTokensUseCase = ExtractTokensUseCase()

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } answers { mockk(relaxed = true) }
    }

    @Test
    fun `execute should return empty tokens when Uri has no parameters`() {
        // Given
        val uri = Uri.parse("https://example.com/callback")

        // When
        val tokens = extractTokensUseCase.execute(uri)

        // Then -  Testando caso os tokens não estejam presentes no Uri
        assertEquals("", tokens.accessToken)
        assertEquals("", tokens.refreshToken)
    }
}