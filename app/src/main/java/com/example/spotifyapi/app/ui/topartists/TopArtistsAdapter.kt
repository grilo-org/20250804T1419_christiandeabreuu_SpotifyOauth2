//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import coil.load
//import coil.transform.CircleCropTransformation
//import com.example.spotifyapi.app.data.model.ArtistResponse
//import com.example.spotifyapi.databinding.ItemTopArtistsBinding
//
//class TopArtistsAdapter(private val onArtistClick: (ArtistResponse) -> Unit) :
//    PagingDataAdapter<ArtistResponse, TopArtistsAdapter.ArtistViewHolder>(ArtistDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
//        val binding =
//            ItemTopArtistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ArtistViewHolder(binding, onArtistClick)
//    }
//
//    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
//        val artist = getItem(position)
//        artist?.let {
//            Log.d("Adapter", "🔄 Artista exibido: ${artist.id} - ${artist.name}")
//            holder.bind(it)
//        }
//    }
//
//    class ArtistViewHolder(
//        private val binding: ItemTopArtistsBinding,
//        private val onArtistClick: (ArtistResponse) -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(artist: ArtistResponse) {
//            binding.tvArtist.text = artist.name
//            binding.imageArtist.load(artist.images.firstOrNull()?.url) {
//                transformations(CircleCropTransformation())
//            }
//
//            binding.root.setOnClickListener {
//                onArtistClick(artist)
//            }
//        }
//    }
//
//    class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistResponse>() {
//        override fun areItemsTheSame(oldItem: ArtistResponse, newItem: ArtistResponse): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ArtistResponse, newItem: ArtistResponse): Boolean {
//            return oldItem == newItem
//        }
//    }
//}