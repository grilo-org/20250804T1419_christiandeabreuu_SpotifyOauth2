package com.example.spotifyapi.app.ui.topartists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.databinding.FragmentTopArtistsBinding
import com.example.spotifyapi.utils.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopArtistsFragment : Fragment() {

    private lateinit var binding: FragmentTopArtistsBinding
    private val viewModel: TopArtistsViewModel by viewModel()
    private lateinit var artistsAdapter: TopArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        artistsAdapter = TopArtistsAdapter { artist ->
            navigateToAlbumsFragment(artist)
        }
        observeUserProfile()
        observeArtists()
        observeError()
        observePagingData()
        setupRecyclerView()
        viewModel.getUserProfile()
    }

    private fun observePagingData() {

        lifecycleScope.launch {
            viewModel.getArtistsPagingData().collectLatest { pagingData ->
                artistsAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeArtists() {
        viewModel.artistsLiveData.observe(viewLifecycleOwner) { artists ->
            artists?.let {}
        }
    }

    private fun observeError() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                toast(it)
            }
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
        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.artistasRecyclerView.adapter = artistsAdapter
    }

    private fun navigateToAlbumsFragment(artist: ArtistResponse) {
        val bundle = Bundle().apply {
            putString("ARTIST_ID", artist.id)
            putString("ARTIST", artist.name)
            putString("IMAGE_URL", artist.images.firstOrNull()?.url)
        }

        findNavController().navigate(R.id.albumsFragment, bundle)
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
}
