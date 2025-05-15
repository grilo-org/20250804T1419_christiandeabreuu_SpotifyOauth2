package com.example.spotifyapi.authenticate.data.repository

import android.content.Context
import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.domain.interfacies.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl(context: Context) : AuthRepository {

    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).build()

    private val sharedPreferences =
        context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)

    override suspend fun getAccessToken(
        authorizationCode: String,
        redirectUri: String
    ): Result<SpotifyTokens> {
        return try {
            val requestBody = FormBody.Builder().add("grant_type", "authorization_code")
                .add("code", authorizationCode).add("redirect_uri", redirectUri)
                .add("client_id", BuildConfig.SPOTIFY_CLIENT_ID)
                .add("client_secret", BuildConfig.SPOTIFY_CLIENT_SECRET).build()

            val request =
                Request.Builder().url("https://accounts.spotify.com/api/token").post(requestBody)
                    .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            if (!response.isSuccessful) {
                return Result.failure(Exception("Erro: ${response.code} - ${response.body?.string()}"))
            }

            val responseBody =
                response.body?.string() ?: return Result.failure(Exception("Erro: resposta vazia"))
            val jsonObject = JSONObject(responseBody)
            val accessToken = jsonObject.getString("access_token")
            val refreshToken = jsonObject.getString("refresh_token")

            Result.success(SpotifyTokens(accessToken, refreshToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): Result<SpotifyTokens> {
        return try {
            val requestBody = FormBody.Builder().add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken).add("client_id", BuildConfig.SPOTIFY_CLIENT_ID)
                .add("client_secret", BuildConfig.SPOTIFY_CLIENT_SECRET).build()

            val request =
                Request.Builder().url("https://accounts.spotify.com/api/token").post(requestBody)
                    .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            if (!response.isSuccessful) {
                return Result.failure(Exception("Erro: ${response.code} - ${response.body?.string()}"))
            }

            val responseBody =
                response.body?.string() ?: return Result.failure(Exception("Erro: resposta vazia"))
            val jsonObject = JSONObject(responseBody)
            val accessToken = jsonObject.getString("access_token")
            val newRefreshToken = jsonObject.optString("refresh_token", refreshToken)

            Result.success(SpotifyTokens(accessToken, newRefreshToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return sharedPreferences.edit().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("REFRESH_TOKEN", refreshToken)
        }.commit()
    }

    override fun loadTokens(): SpotifyTokens? {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)

        return if (accessToken != null && refreshToken != null) {
            SpotifyTokens(accessToken, refreshToken)
        } else {
            null
        }
    }
}