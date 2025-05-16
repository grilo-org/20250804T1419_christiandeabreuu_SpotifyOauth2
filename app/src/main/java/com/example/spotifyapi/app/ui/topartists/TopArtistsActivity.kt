package com.example.spotifyapi.app.ui.topartists

import TopArtistsAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.ui.playlist.PlaylistActivity
import com.example.spotifyapi.app.ui.profile.ProfileActivity
import com.example.spotifyapi.databinding.ActivityTopArtistsBinding
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
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

        checkAccessToken()
        setupRecyclerView()
        observeUserProfile()
        observeArtists()
//        setupObservers()
        viewModel.getTopArtists(accessToken)
        bottomNavigationView()
    }

//    private fun setupObservers() {
//        viewModel.accessToken.observe(this) { token ->
//            if (token.isEmpty()) navigateToLogin()
//        }
//    }

    private fun observeArtists() {
        viewModel.artistsLiveData.observe(this) { artists ->
            updateArtistsUI(artists)
        }
    }

    private fun updateArtistsUI(artists: List<Artist>?) {
        artists?.let {
            Log.d("ArtistActivity", "🎨 Total de artistas recebidos: ${artists.size}")
            topArtistsAdapter.submitList(it)
        } ?: Log.e("ArtistActivity", "❌ Nenhum artista encontrado!")
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(this@TopArtistsActivity) { profile -> // ✅ Agora observamos corretamente!
            profile?.let {
                Log.d("UserProfileActivity", "👤 Perfil do usuário carregado!")
                imageProfile(it.images.firstOrNull()?.url)
            } ?: Log.e("UserProfileActivity", "❌ Erro ao carregar perfil do usuário!")
        }
    }

    private fun refreshUserToken() {
        Log.e("ArtistActivity", "❌ Erro ao obter perfil do usuário, tentando refresh...")
        viewModel.refreshToken(accessToken).observe(this@TopArtistsActivity) { newTokens ->
            newTokens?.let {
                saveAccessToken(it.accessToken, it.refreshToken)
                viewModel.getUserProfile(it.accessToken)
            } ?: navigateToLogin()
        }
    }

    private fun setupRecyclerView() {
        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(this)
        topArtistsAdapter = TopArtistsAdapter()
        binding.artistasRecyclerView.adapter = topArtistsAdapter
    }

    private fun checkAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        if (accessToken.isEmpty()) {
            navigateToLogin()
            return
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun bottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> true
                R.id.navigation_playlists -> {
                    navigateToActivity(PlaylistActivity::class.java)
                    true
                }

                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
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


//    private fun setupObservers() {
//
//        viewModel.getUserProfile(accessToken).observe(this@TopArtistsActivity) { profile ->
//            profile?.let {
//                imageProfile(it.images.firstOrNull()?.url)
//            } ?: run {
//                Log.e("ArtistActivity", "❌ Erro ao obter perfil do usuário, tentando refresh...")
//                viewModel.refreshToken(accessToken).observe(this@TopArtistsActivity) { newTokens ->
//                    newTokens?.let {
//                        saveAccessToken(it.accessToken, it.refreshToken)
//                        viewModel.getUserProfile(it.accessToken)
//                    } ?: navigateToLogin()
//                }
//            }
//        }
//
//        viewModel.artistsLiveData.observe(this) { artists ->
//            artists?.let {
//                Log.d("ArtistActivity", "🎨 Total de artistas recebidos: ${artists.size}")
//                topArtistsAdapter = TopArtistsAdapter(it, this@TopArtistsActivity, accessToken)
//                binding.artistasRecyclerView.adapter = topArtistsAdapter
//            } ?: Log.e("ArtistActivity", "❌ Nenhum artista encontrado!")
//        }
//    }



