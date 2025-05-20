package com.example.spotifyapi.app.ui.topartists.c


import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("items") val items: List<ArtistC>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("href") val href: String?,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?
)

data class ArtistC(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("images") val images: List<ImageArtista>
)

data class ImageArtista(
    @SerializedName("url") val url: String
)
