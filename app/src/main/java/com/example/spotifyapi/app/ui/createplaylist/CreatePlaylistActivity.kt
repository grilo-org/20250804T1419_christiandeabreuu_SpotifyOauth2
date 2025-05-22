package com.example.spotifyapi.app.ui.createplaylist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.R
import com.example.spotifyapi.databinding.ActivityCreatePlaylistBinding
import com.example.spotifyapi.utils.NetworkUtils
import com.example.spotifyapi.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeCreatePlaylist()
        observeError()
        setupCreateButton()
        setupCloseButton()
    }

    private fun observeError() {
        viewModel.errorLiveData.observe(this) { errorMessage ->
            errorMessage?.let {
                toast(it)
            }
        }
    }

    private fun observeCreatePlaylist() {
        viewModel.createPlaylistLiveData.observe(this) { result ->
            result.onSuccess {
                toast(getString(R.string.playlist_created_success))
                finish()
            }.onFailure {
                toast(getString(R.string.error_create_playlist))
            }
        }
    }

    private fun setupCreateButton() {
        binding.createButton.setOnClickListener {
            val playlistName = binding.playlistNameEditText.text.toString().trim()
            if (playlistName.isBlank()) {
                toast(getString(R.string.insert_name_your_playlist_title))
                return@setOnClickListener
            }

            if (!NetworkUtils.isInternetAvailable(this)) {
                toast(getString(R.string.error_internet))
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
