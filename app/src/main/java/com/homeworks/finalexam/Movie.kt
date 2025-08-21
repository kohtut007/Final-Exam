package com.homeworks.finalexam

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    var isFavorite: Boolean = false
)

data class MoviesResponse(
    @SerializedName("results") val results: List<Movie>
)

data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("overview") val overview: String
)

data class ProductionCountry(
    @SerializedName("iso_3166_1") val iso31661: String
)

data class Genre(@SerializedName("name") val name: String)

data class SpokenLanguage(@SerializedName("english_name") val englishName: String)

data class CreditsResponse(@SerializedName("cast") val cast: List<Cast>)

data class Cast(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val profilePath: String?
)