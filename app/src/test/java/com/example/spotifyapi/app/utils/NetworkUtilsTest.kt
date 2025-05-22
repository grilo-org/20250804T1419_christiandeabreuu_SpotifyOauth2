package com.example.spotifyapi.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.spotifyapi.utils.NetworkUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkUtilsTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var network: Network
    private lateinit var networkCapabilities: NetworkCapabilities

    @Before
    fun setup() {
        context = mockk()
        connectivityManager = mockk()
        network = mockk()
        networkCapabilities = mockk()

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
    }

    @Test
    fun `returns true when internet is available`() {
        // Given
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        // When
        val result = NetworkUtils.isInternetAvailable(context)

        // Then
        assertTrue(result)
    }

    @Test
    fun `returns false when activeNetwork is null`() {
        // Given
        every { connectivityManager.activeNetwork } returns null

        // When
        val result = NetworkUtils.isInternetAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun `returns false when networkCapabilities is null`() {
        // Given
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns null

        // When
        val result = NetworkUtils.isInternetAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun `returns false when internet capability is not present`() {
        // Given
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false

        // When
        val result = NetworkUtils.isInternetAvailable(context)

        // Then
        assertFalse(result)
    }
}