package com.example.spotifyapi.app.ui.topartists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.ui.playlist.PlaylistActivity
import com.example.spotifyapi.app.ui.playlist.ProfileActivity
import com.example.spotifyapi.databinding.ActivityTopArtistsBinding
import com.example.spotifyapi.oauth2.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopArtistsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopArtistsBinding
    private val viewModel: TopArtistsViewModel by viewModel()
    private lateinit var topArtistsAdapter: TopArtistsAdapter
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

                    viewModel.getUserProfile(accessToken).observe(this@TopArtistsActivity) { profile ->
                profile?.let {
                    imageProfile(it.images.firstOrNull()?.url)
                } ?: run {
                    Log.e("ArtistActivity", "❌ Erro ao obter perfil do usuário, tentando refresh...")
                    viewModel.refreshToken(accessToken).observe(this@TopArtistsActivity) { newTokens ->
                        newTokens?.let {
                            saveAccessToken(it.accessToken, it.refreshToken)
                            viewModel.getUserProfile(it.accessToken)
                        } ?: navigateToLogin()
                    }
                }
            }


        // 🔹 Apenas observe `LiveData`, sem precisar de `lifecycleScope.launch`
        viewModel.artistsLiveData.observe(this) { artists ->
            artists?.let {
                Log.d("ArtistActivity", "🎨 Total de artistas recebidos: ${artists.size}")
                topArtistsAdapter = TopArtistsAdapter(it, this@TopArtistsActivity, accessToken)
                binding.artistasRecyclerView.adapter = topArtistsAdapter
            } ?: Log.e("ArtistActivity", "❌ Nenhum artista encontrado!")
        }

        // 🔹 Agora chame a função para carregar os artistas
        viewModel.getTopArtists(accessToken)
        bottomNavigationView()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

        private fun bottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    true
                }

                R.id.navigation_playlists -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(PlaylistActivity::class.java)
                    }
                    true
                }

                R.id.navigation_profile -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        navigateToActivity(ProfileActivity::class.java)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("ACCESS_TOKEN", accessToken)  // Passa o token de acesso
        startActivity(intent)
    }

    private fun imageProfile(imageUrl: String?) {
        imageUrl?.let {
            binding.profileImageView.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
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



}




