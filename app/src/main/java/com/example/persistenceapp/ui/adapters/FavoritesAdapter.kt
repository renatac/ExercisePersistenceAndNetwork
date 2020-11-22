package com.example.persistenceapp.ui.adapters

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.persistenceapp.R
import com.example.persistenceapp.model.CityDatabase
import kotlinx.android.synthetic.main.favorites_item.view.*

class FavoritesAdapter(val list: MutableList<CityDatabase>?) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favorites_item, parent, false)
        )
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        when (holder) {
            is FavoriteViewHolder -> {
                if (position < (list?.size ?: 0)) {
                    val element = list?.get(position)
                    if (element != null) {
                        holder.bindView(element)
                    }
                }
            }
        }
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvCityName: TextView = itemView.tv_favorite_city_name
        private val tvID: TextView = itemView.tv_favorite_id
        private val tvCountry: TextView = itemView.tv_favorite_country
        private val ivIcon: ImageView = itemView.iv_icon
        private val tvWeatherMain: TextView = itemView.tv_weather_main
        private val tvDescryptionWeather: TextView = itemView.tv_descryption_weather

        fun bindView(cityDatabase: CityDatabase) {
            tvCityName.text = cityDatabase.cityName
            tvID.text = cityDatabase.id.toString()
            tvCountry.text = cityDatabase.country
            tvWeatherMain.text = cityDatabase.main
            tvDescryptionWeather.text = cityDatabase.description
            tvDescryptionWeather.text = cityDatabase.description

            Glide.with(itemView.context)
                .load("http://openweathermap.org/img/wn/${cityDatabase.icon}@4x.png")
                .placeholder(R.drawable.ic_baseline_cloud_24)
                .error(R.drawable.ic_baseline_cloud_24)
                .circleCrop()
                .into(ivIcon)
        }
    }

    class FavoritesItemDecoration(val size: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = size
                }
                left = size
                right = size
                bottom = size
            }
        }
    }
}