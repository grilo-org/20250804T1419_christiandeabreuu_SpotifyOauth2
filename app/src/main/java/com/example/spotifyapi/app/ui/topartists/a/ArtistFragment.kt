//package com.example.spotifyapi.app.ui.topartists.a
//
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.lifecycleScope
//import androidx.paging.PagingData
//import androidx.recyclerview.widget.LinearLayoutManager
//import coil.load
//import coil.transform.CircleCropTransformation
//import com.example.spotifyapi.R
////import com.example.spotifyapi.app.ui.topartists.TopArtistsViewModel
//import com.example.spotifyapi.app.ui.topartists.b.Artist
//import com.example.spotifyapi.databinding.FragmentTopArtistsBinding
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import org.koin.androidx.viewmodel.ext.android.viewModel
//
//class ArtistFragment : Fragment() {
//
//    private lateinit var binding: FragmentTopArtistsBinding
//    private val viewModel: ArtistViewModel by viewModel()
//    private lateinit var topArtistsAdapter: ArtistAdapter
//    private var accessToken: String = ""
//    private val offset: Int = 0
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentTopArtistsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        checkAccessToken()
//        setupRecyclerView()
//        observeUserProfile()
////        observeArtists()
//        observeError()
//        observePagingData()
//
//
//        viewModel.getUserProfile(accessToken)
//        viewModel.getArtistsPagingData(accessToken)
//    }
//
//    private fun observePagingData() {
//        lifecycleScope.launch {
//            viewModel.getArtistsPagingData(accessToken)
//                .collectLatest { pagingData: PagingData<com.example.spotifyapi.app.ui.topartists.c.Artist> ->
//                    Log.d("Fragment", "🔄 Dados recebidos: $pagingData")
//                    topArtistsAdapter.submitData(pagingData)
//                }
//        }
//
//    }
//    }
//
//    private fun observeUserProfile() {
//        viewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
//            profile?.let {
//                Log.d(
//                    "TopArtistsFragment",
//                    "✅ Nome: ${it.displayName}, Imagem: ${it.images.firstOrNull()?.url}"
//                )
//                imageProfile(it.images.firstOrNull()?.url)
//            } ?: Log.e("TopArtistsFragment", "❌ Perfil do usuário não carregado!")
//        }
//    }
//
//    private fun setupRecyclerView() {
////        topArtistsAdapter = TopArtistsAdapter { artist ->
////            navigateToAlbumsFragment(artist)
////        }
//
//        binding.artistasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.artistasRecyclerView.adapter = topArtistsAdapter
//
//
//    }
//
//    private fun observeError() {
//        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
//            errorMessage?.let {
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
////    private fun observeArtists() {
////        viewModel.artistsLiveData.observe(viewLifecycleOwner) { artists ->
////            updateArtistsUI(artists)
////        }
////    }
//
//
////    private fun navigateToAlbumsFragment(artist: ArtistResponse) {
////        val bundle = Bundle().apply {
////            putString("ACCESS_TOKEN", accessToken)
////            putString("ARTIST_ID", artist.id)
////            putString("ARTIST", artist.name)
////            putString("IMAGE_URL", artist.images.firstOrNull()?.url)
////        }
////
////        findNavController().navigate(
////            R.id.albumsFragment, bundle
////        )
////    }
//
//
//    private fun checkAccessToken() {
//        accessToken = requireActivity().intent.getStringExtra("ACCESS_TOKEN") ?: ""
//        if (accessToken.isEmpty()) {
//            Log.d("TopArtistsFragment", "⚠️ Access token nulo ou vazio. Indo para LoginActivity.")
//            return
//        }
//    }
//
//    private fun imageProfile(imageUrl: String?) {
//        imageUrl?.let {
//            binding.profileImageView.load(it) {
//                transformations(CircleCropTransformation())
//                placeholder(R.drawable.ic_spotify_full)
//                error(R.drawable.ic_spotify_full_black)
//            }
//        }
//    }
//
//    //    private fun updateArtistsUI(artists: List<ArtistResponse>?) {
////        artists?.let {
////            Log.d("TopArtistsFragment", "🎨 Total de artistas recebidos: ${artists.size}")
////            topArtistsAdapter.submitData(it)
////        } ?: Log.e("TopArtistsFragment", "❌ Nenhum artista encontrado!")
////    }
//}