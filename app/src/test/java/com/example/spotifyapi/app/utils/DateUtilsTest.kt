package com.example.spotifyapi.app.utils

import com.example.spotifyapi.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `format yyyy-MM-dd to dd-MM-yyyy`() {
        // Given
        val input = "2024-05-22"
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("22/05/2024", result)
    }

    @Test
    fun `format yyyy-MM to MM-yyyy`() {
        // Given
        val input = "2024-05"
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("05/2024", result)
    }

    @Test
    fun `format yyyy to yyyy`() {
        // Given
        val input = "2024"
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("2024", result)
    }

    @Test
    fun `format empty string returns empty`() {
        // Given
        val input = ""
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("", result)
    }

    @Test
    fun `format null returns empty`() {
        // Given
        val input: String? = null
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("", result)
    }

    @Test
    fun `format invalid date returns original`() {
        // Given
        val input = "invalid-date"
        // When
        val result = DateUtils.formatDateFromIsoToBr(input)
        // Then
        assertEquals("invalid-date", result)
    }
}