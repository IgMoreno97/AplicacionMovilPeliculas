package com.utad.tema17.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.utad.tema17.Api.ApiRest
import com.utad.tema17.R
import com.utad.tema17.genres.GenresResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment : Fragment() {
    val TAG = "MoviesFragment"
    var data: ArrayList<MoviesResponse.Movie> = ArrayList()
    private var adapter: MoviesAdapter? = null
    private var swiperefresh: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val genre =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("genre", GenresResponse.Genre::class.java)
            } else {
                arguments?.getSerializable("genre") as? GenresResponse.Genre
            }
        (activity as? AppCompatActivity)?.supportActionBar?.title = genre?.name
        //ApiRest.initService()
        getMovies(genre?.id.toString())
        val rvMovies = v.findViewById<RecyclerView>(R.id.rvMovies)

        swiperefresh = v.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        //Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(context, 2)
        rvMovies.layoutManager = mLayoutManager

        //Creamos el adapter y lo vinculamos con el recycler
        adapter = MoviesAdapter(data){ movies ->
            activity?.let {
                val fragment = MovieDetailFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable("movies", movies)
                }
                it.supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.mainContainer, fragment).commit()
            }
        }
        rvMovies.adapter = adapter

        swiperefresh?.setOnRefreshListener {
            getMovies(genre?.id.toString())
        }
    }

    private fun getMovies(genreId: String ) {
        val call = ApiRest.service.getMovies(genreId)
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.movies)
                    // Imprimir aqui el listado con logs
                    // adapter?.notifyDataSetChanged()
                    adapter?.notifyItemRangeChanged(0, data.size)
                } else {
                    response.errorBody()?.string()?.let { Log.e(TAG, it) }
                }
                swiperefresh?.isRefreshing = false
            }
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                t.message?.let { Log.e(TAG, it) }
                swiperefresh?.isRefreshing = false
            }
        })
    }
}