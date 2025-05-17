package com.example.spotifyapi.app.ui.topartists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.databinding.ItemTopArtistsBinding

class TopArtistsAdapter(private val onArtistClick: (Artist) -> Unit) :
    ListAdapter<Artist, TopArtistsAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

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
        private val onArtistClick: (Artist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            binding.tvArtist.text = artist.name
            binding.imageArtist.load(artist.images.firstOrNull()?.url) {
                transformations(CircleCropTransformation())
            }

            binding.root.setOnClickListener {
                onArtistClick(artist)
            }
        }
    }

    class ArtistDiffCallback : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
}


