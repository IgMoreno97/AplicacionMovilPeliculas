package com.utad.tema17.Api

import com.utad.tema17.genres.GenresResponse
import com.utad.tema17.movies.MovieDetailResponse
import com.utad.tema17.movies.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apikey: String = ApiRest.api_key,
        @Query("language") language: String = ApiRest.language
    ): Call<GenresResponse>

    // https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&with_genres={id}
    @GET("discover/movie")
    fun getMovies(
        @Query("with_genres") with_genres: String,
        @Query("api_key") apikey: String = ApiRest.api_key,
        @Query("language") language: String = ApiRest.language,
        @Query("sort_by") sort_by: String = "popularity.desc"
    ): Call<MoviesResponse>


    @GET("movie/{movie_id}")
    fun getGenresMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") apikey: String = ApiRest.api_key,
        @Query("language") language: String = ApiRest.language
    ): Call<MovieDetailResponse>
}
