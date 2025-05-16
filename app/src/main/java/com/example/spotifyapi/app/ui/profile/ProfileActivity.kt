package com.example.spotifyapi.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.ui.playlist.PlaylistActivity
import com.example.spotifyapi.app.ui.topartists.TopArtistsActivity
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
import com.example.spotifyapi.databinding.ActivityProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModel { parametersOf(accessToken) }
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkAccessToken()
        viewModel.getUserProfile(accessToken)
        observeUserProfile()
        setupUI()
    }

    private fun checkAccessToken() {
        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        if (accessToken.isEmpty()) {
            navigateToLogin()
            return
        }
    }

    private fun setupUI() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
        setupCloseButton()
        setupBottomNavigationView()
    }

    private fun setupCloseButton() {
        binding.buttonClose.setOnClickListener {
            finishAffinity()
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_artistas -> {
                    navigateToActivity(TopArtistsActivity::class.java)
                    true
                }

                R.id.navigation_playlists -> {
                    navigateToActivity(PlaylistActivity::class.java)
                    true
                }

                R.id.navigation_profile -> true
                else -> false
            }

        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass).apply {
            putExtra("ACCESS_TOKEN", accessToken)
//            addFlags(FLAG_ACTIVITY_NO_ANIMATION)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            //evita recriar a activity
        }
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(this@ProfileActivity) { profile -> // ✅ Agora observamos corretamente!
            profile?.let {
                Log.d("UserProfileActivity", "👤 Perfil do usuário carregado!")
                imageProfile(it.images.firstOrNull()?.url)
                binding.profileTextView.text = it.displayName
            } ?: Log.e("UserProfileActivity", "❌ Erro ao carregar perfil do usuário!")
        }
    }

    private fun imageProfile(imageUrl: String?) {
        imageUrl?.let {
            binding.profileImageView.load(it) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }

    private fun updateProfileUI(userProfile: UserProfile) {
        Log.d(
            "ProfileActivity",
            "Atualizando UI com o nome: ${userProfile.displayName} e imagem: ${userProfile.images.firstOrNull()?.url}"
        )
        binding.profileTextView.text = userProfile.displayName
        userProfile.images.firstOrNull()?.let { image ->
            binding.profileImageView.load(image.url) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full_black)
            }
        }
    }
}