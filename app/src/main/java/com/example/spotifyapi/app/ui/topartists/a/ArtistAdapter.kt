package com.example.spotifyapi.app.ui.topartists.a

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotifyapi.R
import com.example.spotifyapi.app.ui.topartists.c.ArtistC
import com.example.spotifyapi.databinding.ItemTopArtistsBinding

class ArtistAdapter(
    private val accessToken: String, private val onClick: (ArtistC) -> Unit
) : PagingDataAdapter<ArtistC, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding =
            ItemTopArtistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)

        artist?.let {
            holder.binding.tvArtist.text = it.name
            holder.binding.imageArtist.load(artist.images.firstOrNull()?.url) {
                transformations(coil.transform.CircleCropTransformation())
                placeholder(R.drawable.ic_spotify_full)
                error(R.drawable.ic_spotify_full)
            }
            holder.binding.root.setOnClickListener {
                onClick(artist)
            }
        }
    }

    class ArtistViewHolder(val binding: ItemTopArtistsBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistC>() {
        override fun areItemsTheSame(oldItem: ArtistC, newItem: ArtistC): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArtistC, newItem: ArtistC): Boolean {
            return oldItem == newItem
        }
    }
}