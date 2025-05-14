package com.example.spotifyapi.ui.topartists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyapi.databinding.ActivityTopArtistsBinding
import com.example.spotifyapi.ui.login.LoginActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopArtistsBinding
    private val viewModel: ArtistViewModel by viewModel()
    private lateinit var artistAdapter: ArtistAdapter
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopArtistsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Recupera o token que foi passado pela LoginActivity
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        Log.d("ArtistActivity", "✅ Token recebido: $accessToken")

        if (accessToken.isEmpty()) {
            Log.e("ArtistActivity", "❌ Token não recebido, redirecionando para Login")
            navigateToLogin()
            return
        }

        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)



        lifecycleScope.launch {
            viewModel.getTopArtists(accessToken).observe(this@ArtistActivity) { artists ->
                artists?.let {
                    Log.d("ArtistActivity", "🎨 Total de artistas recebidos: ${artists.size}")
                    artistAdapter = ArtistAdapter(it, this@ArtistActivity, accessToken)
                    binding.artistasRecyclerView.adapter = artistAdapter
                } ?: Log.e("ArtistActivity", "❌ Nenhum artista encontrado!")
            }
        }
    }

    private fun saveAccessToken(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}

