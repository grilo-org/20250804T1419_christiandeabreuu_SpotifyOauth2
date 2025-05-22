package com.example.spotifyapi.auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class TokenRepositoryTest {

    private lateinit var tokenRepository: TokenRepository
    private val mockContext: Context = mockk()
    private val mockSharedPreferences: SharedPreferences = mockk()
    private val mockEditor: SharedPreferences.Editor = mockk()

    @Before
    fun setup() {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.commit() } returns true
        tokenRepository = TokenRepository(mockContext)
    }

    @Test
    fun `saveTokens should save access and refresh tokens correctly`() {
        val success = tokenRepository.saveTokens("access123", "refresh456")

        //Then -  sucesso ao salvar tokens
        assertTrue(success)
        verify { mockEditor.putString("ACCESS_TOKEN", "access123") }
        verify { mockEditor.putString("REFRESH_TOKEN", "refresh456") }
        verify { mockEditor.commit() }
    }

    @Test
    fun `saveTokens should return false when commit fails`() {
        //Given
        every { mockEditor.commit() } returns false

        //When
        val success = tokenRepository.saveTokens("access123", "refresh456")

        //Then -  falha ao salvar tokens
        assertFalse(success)
        verify { mockEditor.commit() }
    }
}