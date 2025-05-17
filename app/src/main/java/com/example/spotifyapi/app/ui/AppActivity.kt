package com.example.spotifyapi.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.spotifyapi.R
import com.example.spotifyapi.databinding.ActivityAppBinding


class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        val navHostFragment = binding.navHostFragment.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        sendTokenToFragments(navController)
    }

    private fun sendTokenToFragments(navController: NavController) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.topArtistsFragment -> {
                    navController.navigate(R.id.topArtistsFragment) //  Não precisa token
                    true
                }
                R.id.profileFragment -> {
                    val bundle = Bundle().apply {
                        putString("ACCESS_TOKEN", accessToken)
                    }
                    navController.navigate(R.id.profileFragment, bundle) // token
                    true
                }
                R.id.navigation_playlists -> {
                    val bundle = Bundle().apply {
                        putString("ACCESS_TOKEN", accessToken)
                    }
                    navController.navigate(R.id.playlistFragment, bundle) // token
                    true
                }
                else -> false
            }
        }
    }
}

