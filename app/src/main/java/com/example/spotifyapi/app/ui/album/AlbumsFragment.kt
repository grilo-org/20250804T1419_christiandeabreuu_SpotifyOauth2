package com.example.spotifyapi.app.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.R
import com.example.spotifyapi.databinding.FragmentAlbumsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AlbumsFragment : Fragment() {
    private lateinit var binding: FragmentAlbumsBinding
    private val viewModel: AlbumsViewModel by viewModel()
    private lateinit var albumAdapter: AlbumAdapter
    private var accessToken: String = ""
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

        accessToken = arguments?.getString("ACCESS_TOKEN") ?: ""
        artistId = arguments?.getString("ARTIST_ID") ?: ""
        artistName = arguments?.getString("ARTIST") ?: ""
        imageUrl = arguments?.getString("IMAGE_URL") ?: ""

        if (accessToken.isEmpty() || artistId.isEmpty()) {
            handleError("❌ Token ou ID do artista não encontrado!")
            return
        }

        setupViews()
        observeAlbums()
        setupBackButton()
        viewModel.fetchAlbums(accessToken, artistId)
    }


    private fun handleError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        requireActivity().supportFragmentManager.popBackStack() // 🔥 Fechar o Fragment em caso de erro
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
            requireActivity().supportFragmentManager.popBackStack() // 🔥 Fecha o Fragment ao clicar no botão
        }
    }



    private fun observeAlbums() {
        viewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            albums?.let {
                albumAdapter.submitList(it) // 🔥 Agora a RecyclerView será atualizada automaticamente!
            } ?: Log.e("AlbumsFragment", "❌ Nenhum álbum recebido!")
        }
    }

}
//
//private fun observeViewModel() {
//    viewModel.fetchAlbums(accessToken, artistId).observe(viewLifecycleOwner) { result ->
//        result.onSuccess { albums ->
//            albums?.let {
//                if (it.isNotEmpty()) {
//                    albumAdapter.submitList(it)
//                } else {
//                    Toast.makeText(requireContext(), "Nenhum álbum encontrado.", Toast.LENGTH_SHORT).show()
//                }
//            } ?: Log.e("AlbumsFragment", "Resposta da API retornou nula.")
//        }.onFailure {
//            Toast.makeText(requireContext(), "Erro ao carregar álbuns.", Toast.LENGTH_SHORT).show()
//        }
//    }
//}