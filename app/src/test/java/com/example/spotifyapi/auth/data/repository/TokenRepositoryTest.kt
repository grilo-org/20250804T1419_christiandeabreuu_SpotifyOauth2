import android.content.Context
import android.content.SharedPreferences
import com.example.spotifyapi.auth.data.repository.TokenRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repo: TokenRepositoryImpl

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        sharedPreferences = mockk(relaxed = true)
        editor = mockk(relaxed = true)

        // Mock EncryptedSharedPreferences.create to return our mock
        mockkStatic("androidx.security.crypto.EncryptedSharedPreferences")
        every {
            androidx.security.crypto.EncryptedSharedPreferences.create(
                any(), any(), any(), any(), any()
            )
        } returns sharedPreferences

        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.commit() } returns true

        repo = TokenRepositoryImpl(context)
    }

    @Test
    fun `saveTokens returns true on successful save`() {
        val result = repo.saveTokens("access", "refresh")

        assertTrue(result)
        verify { editor.putString("ACCESS_TOKEN", "access") }
        verify { editor.putString("REFRESH_TOKEN", "refresh") }
        verify { editor.commit() }
    }

    @Test
    fun `saveTokens returns false on exception`() {
        every { editor.commit() } throws RuntimeException("fail")

        val result = repo.saveTokens("access", "refresh")

        assertFalse(result)
    }

    @Test
    fun `getAccessToken returns token from shared preferences`() {
        every { sharedPreferences.getString("ACCESS_TOKEN", null) } returns "token123"

        val token = repo.getAccessToken()

        assertEquals("token123", token)
    }

    @Test
    fun `getAccessToken returns null if not set`() {
        every { sharedPreferences.getString("ACCESS_TOKEN", null) } returns null

        val token = repo.getAccessToken()

        assertNull(token)
    }
}