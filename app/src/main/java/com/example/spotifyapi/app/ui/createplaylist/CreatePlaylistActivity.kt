package com.example.spotifyapi.app.ui.createplaylist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.databinding.ActivityCreatePlaylistBinding
import com.example.spotifyapi.utils.NetworkUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var accessToken: String
    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""

        setupCreateButton()
        setupCloseButton()
        observeCreatePlaylist()
        observeError()
    }

    private fun observeError() {
        viewModel.errorLiveData.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCreatePlaylist() {
        viewModel.createPlaylistLiveData.observe(this) { result ->
            result.onSuccess {
                showSuccess()
            }.onFailure {
                showError(it.message ?: "Erro ao criar playlist")
            }
        }
    }

    private fun setupCreateButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString().trim()

            if (playlistName.isBlank()) {
                showError("Por favor, insira um nome para a playlist")
                return@setOnClickListener
            }

            if (!NetworkUtils.isInternetAvailable(this)) {
                showError("Modo offline: Não é possível criar playlists")
                return@setOnClickListener
            }

            viewModel.createPlaylist(accessToken, playlistName)
        }
    }

    private fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showSuccess() {
        Toast.makeText(this, "Playlist criada com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
