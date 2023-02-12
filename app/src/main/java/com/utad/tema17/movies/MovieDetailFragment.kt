package com.utad.tema17.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
import com.utad.tema17.Api.ApiRest
import com.utad.tema17.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailFragment : Fragment() {
    val TAG = "MovieDetailFragment"
    var data: ArrayList<MoviesResponse.Movie> = ArrayList()
    lateinit var generosPeli: ChipGroup
    lateinit var  newMovie: MovieDetailResponse
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("movies", MoviesResponse.Movie::class.java)
            } else {
                arguments?.getSerializable("movies") as? MoviesResponse.Movie
            }
        ApiRest.initService()
        getMovieDetails(movie?.id.toString())
        val titulo = view.findViewById<TextView>(R.id.tituloPeli)
        val imagenDetalle = view.findViewById<ImageView>(R.id.imageDetail)
        val imagenTop = view.findViewById<ImageView>(R.id.imagenTopDetail)
        val descripcionPeli = view.findViewById<TextView>(R.id.descripcionPeli)
        val añoEstreno = view.findViewById<TextView>(R.id.añoEstrenoPeli)
        val notaMedia = view.findViewById<TextView>(R.id.notaMediaPeli)
        val lenguajePeli = view.findViewById<TextView>(R.id.lenguajePeli)
        val estrella1 = view.findViewById<CheckBox>(R.id.estrella1)
        val estrella2 = view.findViewById<CheckBox>(R.id.estrella2)
        val estrella3 = view.findViewById<CheckBox>(R.id.estrella3)
         generosPeli = view.findViewById<ChipGroup>(R.id.chipGeneros)

        (activity as? AppCompatActivity)?.supportActionBar?.title = movie?.title.toString()
        titulo.text = movie?.title
        añoEstreno.text = movie?.releaseDate.toString()?.substring(0, 4)
        notaMedia.text = movie?.voteAverage.toString()
//Preguntar como hacer para que si tiene mas de nota media 8 que muestre las tres estrellas rellenas y si no que solo muestre una por ejemplo
        if (notaMedia != null && notaMedia.text as String > 8.toString()) {
            //estrella1.background =

        } else {

        }
        descripcionPeli.text = movie?.overview
        lenguajePeli.text = movie?.originalLanguage

        //Picasso.get().load("${ApiRest.URL_IMAGES}${item.posterPath}").into(img)
        Picasso.get().load("${ApiRest.URL_IMAGES}${movie?.posterPath}").into(imagenDetalle)
        Picasso.get().load("${ApiRest.URL_IMAGES}${movie?.backdropPath}").into(imagenTop)
    }

    private fun getMovieDetails(movieId: String ) {
        val call = ApiRest.service.getGenresMovie(movieId)
        call.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    //data.addAll(body.movies)
                    newMovie = body
                    datosnuevos()
                } else {
                    response.errorBody()?.string()?.let { Log.e(TAG, it) }
                }
            }
            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                t.message?.let { Log.e(TAG, it) }
            }
        })
    }
//Rellenar el chip
    private fun datosnuevos() {
        for (item in newMovie.genres){
            val chip = Chip(context)
            chip.text = item.name
            generosPeli.addView(chip)
        }
    }


}