package com.utad.tema17.genres
import java.io.Serializable
data class GenresResponse(
    val genres: List<Genre>
) : Serializable {
    data class Genre(
        val id: Int,
        val name: String
    ) : Serializable
}