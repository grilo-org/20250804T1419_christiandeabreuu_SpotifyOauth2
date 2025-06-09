import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfile
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfileDB
import org.junit.Assert.assertEquals
import org.junit.Test

class UserProfileMapperTest {

    @Test
    fun `toUserProfileDB maps UserProfile to UserProfileDB correctly`() {
        val userProfile = UserProfile(
            id = "123",
            displayName = "User Name",
            images = listOf(Image(url = "http://img.com/pic.jpg"))
        )

        val result = userProfile.toUserProfileDB()

        val expected = UserProfileDB(
            id = "123", name = "User Name", imageUrl = "http://img.com/pic.jpg"
        )

        assertEquals(expected, result)
    }

    @Test
    fun `toUserProfileDB handles empty images list`() {
        val userProfile = UserProfile(
            id = "321", displayName = "No Pic", images = emptyList()
        )

        val result = userProfile.toUserProfileDB()

        val expected = UserProfileDB(
            id = "321", name = "No Pic", imageUrl = ""
        )

        assertEquals(expected, result)
    }

    @Test
    fun `toUserProfile maps UserProfileDB to UserProfile correctly`() {
        val userProfileDB = UserProfileDB(
            id = "456", name = "DB Name", imageUrl = "http://img.com/dbpic.jpg"
        )

        val result = userProfileDB.toUserProfile()

        val expected = UserProfile(
            id = "456",
            displayName = "DB Name",
            images = listOf(Image(url = "http://img.com/dbpic.jpg"))
        )

        assertEquals(expected, result)
    }

    @Test
    fun `toUserProfile handles null imageUrl`() {
        val userProfileDB = UserProfileDB(
            id = "789", name = "Null Image", imageUrl = null
        )

        val result = userProfileDB.toUserProfile()

        val expected = UserProfile(
            id = "789", displayName = "Null Image", images = listOf(Image(url = ""))
        )

        assertEquals(expected, result)
    }
}