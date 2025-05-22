package com.example.spotifyapi.app.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistActivity
import com.example.spotifyapi.databinding.FragmentPlaylistBinding
import com.example.spotifyapi.utils.NetworkUtils
import com.example.spotifyapi.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observePlaylists()
        observeError()
        observeUserProfile()

        viewModel.getPlaylists()
        viewModel.getUserProfile()
        setupRecyclerView()
        setupCreatePlaylistButton()
    }

    private fun observeError() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                toast(it)
            }
        }
    }

    private fun observePlaylists() {
        viewModel.playlistsLiveData.observe(viewLifecycleOwner) { playlists ->
            updatePlaylistsUI(playlists)
        }
    }

    private fun updatePlaylistsUI(playlists: List<Playlist>?) {
        playlists?.let {
            playlistAdapter.submitList(it)
        }
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                imageProfile(it.images.firstOrNull()?.url)
            }
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsRecyclerView.adapter = playlistAdapter
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            NetworkUtils.isInternetAvailable(requireContext())
            val intent = Intent(requireContext(), CreatePlaylistActivity::class.java)
            startActivity(intent)
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
}