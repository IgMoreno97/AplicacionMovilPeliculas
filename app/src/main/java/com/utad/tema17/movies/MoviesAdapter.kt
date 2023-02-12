package com.utad.tema17.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utad.tema17.Api.ApiRest
import com.utad.tema17.R
import com.utad.tema17.genres.GenresResponse

class MoviesAdapter(private val data: ArrayList<MoviesResponse.Movie>, val onClick: (MoviesResponse.Movie) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val card = v.findViewById<CardView>(R.id.card)
        val titulo = v.findViewById<TextView>(R.id.titulo)
        val img = v.findViewById<ImageView>(R.id.caratula)
        fun bind(item: MoviesResponse.Movie) {
            titulo.text = item.title
            Picasso.get().load("${ApiRest.URL_IMAGES}${item.posterPath}").into(img)
           // card.setCardBackgroundColor(generateColor().toInt())
            card.setOnClickListener {
                Log.v("Pulso sobre", item.id.toString())
                onClick(item)
            }
        }

        fun generateColor(): Long {
            val colors = arrayListOf(
                0xfff44336,0xffe91e63,0xff9c27b0,0xff673ab7,
                0xff3f51b5,0xff2196f3,0xff03a9f4,0xff00bcd4,
                0xff009688,0xff4caf50,0xff8bc34a,0xffcddc39,
                0xffffeb3b,0xffffc107,0xffff9800,0xffff5722,
                0xff795548,0xff9e9e9e,0xff607d8b,0xff333333
            )
            return colors.random()
        }
    }
}