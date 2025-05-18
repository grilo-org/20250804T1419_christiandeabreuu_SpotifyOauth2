package com.example.spotifyapi.app.ui.topartists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse
import com.example.spotifyapi.databinding.ItemTopArtistsBinding

class TopArtistsAdapter(private val onArtistClick: (TopArtistInfoResponse) -> Unit) :
    ListAdapter<TopArtistInfoResponse, TopArtistsAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding =
            ItemTopArtistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding, onArtistClick)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.bind(artist)
    }

    class ArtistViewHolder(
        private val binding: ItemTopArtistsBinding,
        private val onArtistClick: (TopArtistInfoResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: TopArtistInfoResponse) {
            binding.tvArtist.text = artist.name
            binding.imageArtist.load(artist.images.firstOrNull()?.url) {
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener {
                onArtistClick(artist)
            }
        }
    }

    class ArtistDiffCallback : DiffUtil.ItemCallback<TopArtistInfoResponse>() {
        override fun areItemsTheSame(oldItem: TopArtistInfoResponse, newItem: TopArtistInfoResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TopArtistInfoResponse, newItem: TopArtistInfoResponse): Boolean {
            return oldItem == newItem
        }
    }
}


