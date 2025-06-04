package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM

class GetTopArtistsUseCase(
    private val repository: TopArtistsRepository
) {

    suspend fun fetchAndSaveTopArtists(
        offset: Int = 0, timeRange: String = MEDIUM_TERM
    ): TopArtistsResponse {
        val responseApi = repository.getTopArtistsApi(offset, timeRange)
        saveTopArtistsToDatabase(responseApi, timeRange)
        return responseApi
    }

    /**
     * PRELOAD: Busca todas as páginas da API e salva todos os artistas e imagens no banco!
     * Chame este método para garantir que o modo offline terá todos os dados.
     */
    suspend fun preloadAllTopArtists(timeRange: String = MEDIUM_TERM) {
//        repository.clearTopArtistsData(timeRange)

        // Cria um TopArtistsDB "base" só para manter o relacionamento (pode ajustar depois)
        val topArtistsDB = TopArtistsMapper.toTopArtistsDB(
            TopArtistsResponse(emptyList(), 0, 0, 0, null, null, null), timeRange
        )
        val topArtistsId = repository.insertTopArtistsDB(topArtistsDB).toInt()

        var offset = 0
        val pageSize = 50
        while (true) {
            val response = repository.getTopArtistsApi(offset, timeRange)
            if (response.items.isEmpty()) break

            val artistsDB = TopArtistsMapper.toArtistsDB(response, topArtistsId)
            repository.insertArtists(artistsDB)

            // Busca os databaseIds corretos dos artistas pelo id do Spotify + topArtistsId
            val artistIdMaps = repository.getArtistDatabaseIdsBySpotifyIds(
                response.items.map { it.id }, topArtistsId
            )
            val spotifyIdToDbId = artistIdMaps.associate { it.id to it.databaseId }

            // Monta as imagens corretamente associadas ao databaseId do artista
            val imageArtistsDB = response.items.flatMap { artist ->
                artist.images.map { image ->
                    ImageArtist(
                        url = image.url,
                        artistId = spotifyIdToDbId[artist.id] ?: 0
                    )
                }
            }
            repository.insertImageArtists(imageArtistsDB)

            offset += pageSize
        }
    }

    /**
     * Salva UMA página de artistas e imagens no banco.
     * Usada quando o app está online e busca só um "pedaço" dos artistas.
     */
    private suspend fun saveTopArtistsToDatabase(response: TopArtistsResponse, timeRange: String) {
        val topArtistsDB = TopArtistsMapper.toTopArtistsDB(response, timeRange)
        val topArtistsId = repository.insertTopArtistsDB(topArtistsDB).toInt()

        val artistsDB = TopArtistsMapper.toArtistsDB(response, topArtistsId)
        repository.insertArtists(artistsDB)

        // Busca os databaseIds corretos dos artistas pelo id do Spotify + topArtistsId
        val artistIdMaps = repository.getArtistDatabaseIdsBySpotifyIds(
            response.items.map { it.id }, topArtistsId
        )
        val spotifyIdToDbId = artistIdMaps.associate { it.id to it.databaseId }

        // Monta as imagens corretamente associadas ao databaseId do artista
        val imageArtistsDB = response.items.flatMap { artist ->
            artist.images.map { image ->
                ImageArtist(
                    url = image.url,
                    artistId = spotifyIdToDbId[artist.id] ?: 0
                )
            }
        }
        repository.insertImageArtists(imageArtistsDB)
    }
}