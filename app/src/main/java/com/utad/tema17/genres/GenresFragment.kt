package com.utad.tema17.genres

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
import com.utad.tema17.movies.MoviesFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenresFragment : Fragment() {

    val TAG = "GenresFragment"
    var data: ArrayList<GenresResponse.Genre> = ArrayList()
    private var adapter: GenresAdapter? = null
    private var swiperefresh: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Buscar"
        ApiRest.initService()
        getGenres()
        val rvAgents = v.findViewById<RecyclerView>(R.id.rvGeneros)

        swiperefresh = v.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        //Mostrar como cuadricula
        val mLayoutManager = GridLayoutManager(context, 2)
        rvAgents.layoutManager = mLayoutManager

        //Creamos el adapter y lo vinculamos con el recycler
        adapter = GenresAdapter(data){ genre ->
            activity?.let {
                val fragment = MoviesFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable("genre", genre)
                }
                it.supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.mainContainer, fragment).commit()
            }
        }
        rvAgents.adapter = adapter

        swiperefresh?.setOnRefreshListener {
            getGenres()
        }
    }

    private fun getGenres() {
        val call = ApiRest.service.getGenres()
        call.enqueue(object : Callback<GenresResponse> {
            override fun onResponse(call: Call<GenresResponse>, response: Response<GenresResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.genres)
                    // Imprimir aqui el listado con logs
                    // adapter?.notifyDataSetChanged()
                    adapter?.notifyItemRangeChanged(0, data.size)
                } else {
                    response.errorBody()?.string()?.let { Log.e(TAG, it) }
                }
                swiperefresh?.isRefreshing = false
            }
            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                t.message?.let { Log.e(TAG, it) }
                swiperefresh?.isRefreshing = false
            }
        })
    }
}