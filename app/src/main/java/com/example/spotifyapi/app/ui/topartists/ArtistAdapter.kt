package com.example.spotifyapi.app.ui.topartists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.databinding.ItemTopArtistsBinding

class ArtistAdapter(
    private val artists: List<Artist>, private val context: Context, private val accessToken: String
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding =
            ItemTopArtistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.binding.tvArtist.text = artist.name
        holder.binding.imageArtist.load(artist.images.firstOrNull()?.url) {

        }

    }

    override fun getItemCount(): Int = artists.size

    class ArtistViewHolder(val binding: ItemTopArtistsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
