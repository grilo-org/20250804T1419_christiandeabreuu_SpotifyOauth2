package com.example.spotifyapi.app.ui.createplaylist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.R
import com.example.spotifyapi.databinding.ActivityCreatePlaylistBinding
import com.example.spotifyapi.utils.NetworkUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Toast.makeText(
                    this,
                    getString(R.string.playlist_created_success),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }.onFailure {
                Toast.makeText(this, getString(R.string.error_create_playlist), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupCreateButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString().trim()
            if (playlistName.isBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.insert_name_your_playlist_title),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!NetworkUtils.isInternetAvailable(this)) {
                Toast.makeText(this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.createPlaylist(playlistName)
        }
    }

    private fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}
