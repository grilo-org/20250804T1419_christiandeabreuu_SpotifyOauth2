package com.example.spotifyapi.app.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistActivity
import com.example.spotifyapi.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistAdapter
    private var accessToken: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        accessToken = arguments?.getString("ACCESS_TOKEN") ?: ""


            viewModel.getPlaylists(accessToken, requireContext())
            viewModel.getUserProfile(accessToken)


        setupRecyclerView()
        observePlaylists()
        observeUserProfile()
        setupCreatePlaylistButton()

    }

    private fun observePlaylists() {
        viewModel.playlistsLiveData.observe(viewLifecycleOwner) { playlists ->
            updatePlaylistsUI(playlists)
        }
    }

    private fun updatePlaylistsUI(playlists: List<Playlist>?) {
        playlists?.let {
            Log.d("PlaylistFragment", "🎶 Total de playlists recebidas: ${playlists.size}")
            playlistAdapter.submitList(it)
        } ?: Log.e("PlaylistFragment", "❌ Nenhuma playlist encontrada!")
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                Log.d("PlaylistFragment", "✅ Nome: ${it.displayName}, Imagem: ${it.images.firstOrNull()?.url}")
                imageProfile(it.images.firstOrNull()?.url)
            } ?: Log.e("PlaylistFragment", "❌ Perfil do usuário não carregado!")
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsRecyclerView.adapter = playlistAdapter
    }

    private fun setupCreatePlaylistButton() {
        binding.buttonToGoCreatePlaylist.setOnClickListener {
            val intent = Intent(requireContext(), CreatePlaylistActivity::class.java).apply {
                putExtra("ACCESS_TOKEN", accessToken) //  Passando o token para a Activity
            }
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