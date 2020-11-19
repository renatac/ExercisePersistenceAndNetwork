package com.example.persistenceapp.ui.adapters

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.persistenceapp.R
import com.example.persistenceapp.model.CityDatabase
import kotlinx.android.synthetic.main.favorites_item.view.*

class FavoritesAdapter(val list: List<CityDatabase>?)
    :RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_item, parent, false))
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        when(holder){
            is FavoriteViewHolder -> {
                if(position < (list?.size ?: 0)){
                    val element = list?.get(position)
                    if(element != null){
                        holder.bindView(element)
                    }
                }
            }
        }
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvCityName : TextView = itemView.tv_favorite_city_name
        val tvCountry : TextView = itemView.tv_favorite_country
        val tvWeatherMain : TextView = itemView.tv_weather_main
        val tvDescryptionWeather : TextView = itemView.tv_descryption_weather

        fun bindView(cityDatabase: CityDatabase){
            tvCityName.text = cityDatabase.cityName
            tvCountry.text = cityDatabase.country
            tvWeatherMain.text = cityDatabase.main
            tvDescryptionWeather.text = cityDatabase.description
        }
    }

    class FavoritesItemDecoration(val size: Int) : RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect){
                if(parent.getChildAdapterPosition(view) == 0){
                    top = size
                }
                left = size
                right = size
                bottom = size
            }
        }
    }
}