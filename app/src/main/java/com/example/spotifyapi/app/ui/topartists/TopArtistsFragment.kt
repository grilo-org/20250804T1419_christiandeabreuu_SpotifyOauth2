package com.example.spotifyapi.app.ui.topartists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.ui.album.AlbumsFragment
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
import com.example.spotifyapi.databinding.FragmentTopArtistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel



class TopArtistsFragment : Fragment() {

    private lateinit var binding: FragmentTopArtistsBinding
    private val viewModel: TopArtistsViewModel by viewModel()
    private lateinit var topArtistsAdapter: TopArtistsAdapter
    private var accessToken: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTopArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAccessToken()
        setupRecyclerView()
        observeUserProfile()
        observeArtists()
        viewModel.getUserProfile(accessToken)
        viewModel.getTopArtists(accessToken)
    }

    private fun observeArtists() {
        viewModel.artistsLiveData.observe(viewLifecycleOwner) { artists ->
            updateArtistsUI(artists)
        }
    }

    private fun updateArtistsUI(artists: List<Artist>?) {
        artists?.let {
            Log.d("TopArtistsFragment", "🎨 Total de artistas recebidos: ${artists.size}")
            topArtistsAdapter.submitList(it)
        } ?: Log.e("TopArtistsFragment", "❌ Nenhum artista encontrado!")
    }

    private fun observeUserProfile() {
        viewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                Log.d("TopArtistsFragment", "✅ Nome: ${it.displayName}, Imagem: ${it.images.firstOrNull()?.url}")
                imageProfile(it.images.firstOrNull()?.url)
            } ?: Log.e("TopArtistsFragment", "❌ Perfil do usuário não carregado!")
        }
    }

    private fun setupRecyclerView() {
        topArtistsAdapter = TopArtistsAdapter { artist ->
            navigateToAlbumsFragment(artist)
        }

        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.artistasRecyclerView.adapter = topArtistsAdapter
    }

    private fun navigateToAlbumsFragment(artist: Artist) {
        val bundle = Bundle().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("ARTIST_ID", artist.id)
            putString("ARTIST", artist.name)
            putString("IMAGE_URL", artist.images.firstOrNull()?.url)
        }

        findNavController().navigate(R.id.albumsFragment, bundle) // 🔥 Agora usa `NavController` corretamente!
    }
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, AlbumsFragment().apply { arguments = bundle })
//            .addToBackStack(null)
//            .commit()




    private fun checkAccessToken() {
        accessToken = requireActivity().intent.getStringExtra("ACCESS_TOKEN") ?: ""
        if (accessToken.isEmpty()) {
            navigateToLogin()
            return
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
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