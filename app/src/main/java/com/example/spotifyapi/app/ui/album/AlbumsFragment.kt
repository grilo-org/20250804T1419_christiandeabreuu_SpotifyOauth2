package com.example.spotifyapi.app.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.databinding.FragmentAlbumsBinding
import com.example.spotifyapi.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment() {
    private lateinit var binding: FragmentAlbumsBinding
    private val viewModel: AlbumsViewModel by viewModel()
    private lateinit var albumAdapter: AlbumAdapter

    private var artistId: String = ""
    private var artistName: String = ""
    private var imageUrl: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistId = arguments?.getString("ARTIST_ID") ?: ""
        artistName = arguments?.getString("ARTIST") ?: ""
        imageUrl = arguments?.getString("IMAGE_URL") ?: ""

        setupViews()
        observeAlbums()
        observeError()
        setupBackButton()
        viewModel.getAlbums(artistId)
    }

    private fun observeError() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                toast(it)
            }
        }
    }

    private fun setupViews() {
        binding.albumTitleTextView.text = artistName
        binding.albumPostImageView.load(imageUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        albumAdapter = AlbumAdapter()
        binding.albumsRecyclerView.adapter = albumAdapter
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeAlbums() {
        viewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            albums?.let {
                albumAdapter.submitList(it)
            }
        }
    }
}
