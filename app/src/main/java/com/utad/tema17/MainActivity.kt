package com.utad.tema17

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.utad.tema17.Api.ApiRest
import com.utad.tema17.genres.GenresAdapter
import com.utad.tema17.genres.GenresFragment
import com.utad.tema17.genres.GenresResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, GenresFragment()).commit()
    }

}