package com.example.spotifyapi.app.ui.createplaylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
import com.example.spotifyapi.databinding.ActivityCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var accessToken: String
    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAccessToken()
        setupCreateButton()
        setupCloseButton()
        observeCreatePlaylist()
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

    private fun setupCreateButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString().trim()

            if (playlistName.isBlank()) {
                showError("Por favor, insira um nome para a playlist.")
                return@setOnClickListener
            }

            Log.d("CreatePlaylistActivity", "PlaylistName: $playlistName")
            viewModel.createPlaylist(accessToken, playlistName)
        }
    }

    private fun observeCreatePlaylist() {
        viewModel.createPlaylistLiveData.observe(this) { result ->
            result.onSuccess { message ->
                showSuccess(message)
            }.onFailure { exception ->
                showError(exception.message ?: "Erro desconhecido.")
            }
        }
    }

    private fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showSuccess(message: String) {
        Log.d("CreatePlaylistActivity", "Success: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showError(message: String) {
        Log.e("CreatePlaylistActivity", "Error: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
