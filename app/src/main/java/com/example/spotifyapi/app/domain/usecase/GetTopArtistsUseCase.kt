package com.example.spotifyapi.app.domain.usecase

import android.util.Log
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtistDB
import com.example.spotifyapi.app.data.model.ImageArtist
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse
import com.example.spotifyapi.app.data.repository.TopArtistsRepository

class GetTopArtistsUseCase(private val repository: TopArtistsRepository) {

    suspend fun execute(accessToken: String): List<TopArtistInfoResponse> {
        return try {
            val responseApi = repository.getTopArtists(accessToken)
            if (responseApi.isNotEmpty()) {
                Log.d("GetTopArtistsUseCase", "🎵 Artistas carregados da API: ${responseApi.size}")

                val (artistsDB, imagesDB) = mapToArtistDB(responseApi)

                repository.insertLocalTopArtists(artistsDB)

                // Aguarde a inserção dos artistas
                val insertedArtists = repository.getLocalTopArtists()

                if (insertedArtists.isNotEmpty()) {
                    Log.d("Database", "📸 Agora inserindo ${imagesDB.size} imagens no banco")

                    // Verifique se os IDs dos artistas estão corretos
                    imagesDB.forEach { image ->
                        val exists = insertedArtists.any { it.id == image.artistId }
                        if (!exists) {
                            Log.e("Database", "❌ Imagem não pode ser inserida! Artista ${image.artistId} não encontrado.")
                        }
                    }

                    repository.insertLocalImages(imagesDB)
                } else {
                    Log.e("Database", "❌ Nenhum artista foi inserido! Não podemos salvar imagens.")
                }

                responseApi
            } else {
                throw Exception("Resposta da API está vazia")
            }
        } catch (e: Exception) {
            Log.e("GetTopArtistsUseCase", "❌ Erro ao buscar artistas: ${e.message}")

            val localArtists = repository.getLocalTopArtists()
            val images = repository.getLocalArtistImages()

            localArtists.map { artist ->
                val filteredImages = images.filter { it.artistId == artist.id } // Agora ambas são Strings
                artist.toArtist(filteredImages)
            }
        }
    }

    private fun mapToArtistDB(artists: List<TopArtistInfoResponse>): Pair<List<ArtistDB>, List<ImageArtistDB>> {
        val artistDBList = artists.map { artistResponse ->
            ArtistDB(
                id = artistResponse.id,
                name = artistResponse.name,
                popularity = artistResponse.popularity,
                topArtistsId = artistResponse.id.hashCode()
            )
        }

        val imageArtistList = artists.flatMap { artistResponse ->
            artistResponse.images.map { imageUrl ->
                Log.d("GetTopArtistsUseCase", "📸 Salvando imagem: ${imageUrl.url} para artista ${artistResponse.name}")
                ImageArtistDB(
                    url = imageUrl.url,
                    artistId = artistResponse.id // Agora ambas são Strings
                )
            }
        }

        return Pair(artistDBList, imageArtistList)
    }

    private fun ArtistDB.toArtist(images: List<ImageArtistDB>): TopArtistInfoResponse {
        val imageUrls = images.map { it.url }

        Log.d("GetTopArtistsUseCase", "🔍 Recuperando imagens para ${this.name}: $imageUrls")

        return TopArtistInfoResponse(
            id = this.id,
            name = this.name,
            popularity = this.popularity,
            images = imageUrls.map { ImageArtist(url = it) }
        )
    }
}

    //    private fun ArtistDB.toArtist(images: List<ImageArtist>): TopArtistInfoResponse {
//        return TopArtistInfoResponse(
//            id = this.id,
//            name = this.name,
//            popularity = this.popularity,
//            images = images.filter { it.artistId == this.databaseId }.map { it.url }
//        )
//    }
//}

//    private fun mapToArtistDB(artists: List<TopArtistInfoResponse>): List<ArtistDB> {
//        return artists.map { artistResponse ->
//            ArtistDB(
//                id = artistResponse.id,
//                name = artistResponse.name,
//                popularity = artistResponse.popularity,
//                topArtistsId = artistResponse.id.hashCode()
//            )
//        }
//    }