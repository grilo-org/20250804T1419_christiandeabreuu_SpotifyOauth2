package com.example.spotifyapi.authenticate.domain.usecase

import android.net.Uri
import com.example.spotifyapi.authenticate.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import org.junit.Assert.*
import org.junit.Test
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Before

class ExtractTokensUseCaseTest {

    private val extractTokensUseCase = ExtractTokensUseCase()

    @Before
    fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } answers { mockk(relaxed = true) }
    }

    // 🔹 Testando caso os tokens não estejam presentes no Uri
    @Test
    fun `execute should return empty tokens when Uri has no parameters`() {
        val uri = Uri.parse("https://example.com/callback")

        val tokens = extractTokensUseCase.execute(uri)

        assertEquals("", tokens.accessToken)
        assertEquals("", tokens.refreshToken)
    }
}