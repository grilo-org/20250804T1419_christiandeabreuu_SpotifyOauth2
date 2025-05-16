package com.example.spotifyapi.app.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistActivity
import com.example.spotifyapi.app.ui.profile.ProfileActivity
import com.example.spotifyapi.app.ui.topartists.TopArtistsActivity
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
import com.example.spotifyapi.databinding.ActivityPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var accessToken: String
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAccessToken()
        setupRecyclerView()
        observePlaylists()
        observeUserProfile()
        viewModel.getPlaylists(accessToken)
        viewModel.getUserProfile(accessToken)
        setupUi()
        setupCreatePlaylistButton()

    }

    private fun setupUi() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
        setupBottomNavigationView()
    }

    private fun observePlaylists() {
        viewModel.playlistsLiveData.observe(this) { playlists ->
            playlistAdapter.submitList(playlists)
        }
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(this@PlaylistActivity) { profile ->
            profile?.let {
                Log.d(
                    "UserProfileActivity",
                    "✅ Nome: ${it.displayName}, Imagem: ${it.images.firstOrNull()?.url}"
                ) // 🔥 Teste no Logcat
                imageProfile(it.images.firstOrNull()?.url)
            } ?: Log.e("UserProfileActivity", "❌ Perfil do usuário não carregado!")
        }
    }

    private fun imageProfile(imageUrl: String?) {
        imageUrl?.let {
            binding.playlistsProfileImageView.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playlistsRecyclerView.adapter = playlistAdapter
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

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    navigateToActivity(TopArtistsActivity::class.java)
                    true
                }

                R.id.navigation_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true

                }

                R.id.navigation_playlists -> true
                else -> false

            }
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass).apply {
            putExtra("ACCESS_TOKEN", accessToken)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            val intent = Intent(this, CreatePlaylistActivity::class.java)
            intent.putExtra("ACCESS_TOKEN", accessToken)
            startActivity(intent)
        }
    }
}