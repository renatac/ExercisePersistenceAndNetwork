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
import com.example.persistenceapp.model.Element
import kotlinx.android.synthetic.main.recyclerview_item.view.*

//Adapter - é para adaptar um objeto em outro objeto - é uma extensão do RecyclerView. O android me
//deixa adaptar meus dados através do bind

class SearchAdapter(
    val list: MutableList<Element>?,
    private val callback: (Long) -> Unit,
    private val viewDetailCallback: (Element) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                if (position < (list?.size ?: 0)) {
                    val element = list?.get(position)
                    if (element != null) {
                        holder.bindView(element)
                        holder.itemView.setOnClickListener {
                            viewDetailCallback(element)
                        }
                        holder.itemView.iv_favorite.setOnClickListener {
                            callback(holder.itemView.tv_id_number.text.toString().toLong())
                        }
                    }
                }
            }
        }
    }

    fun addItems(newElements: MutableList<Element>?) {
        list?.clear()
        newElements?.forEach { list?.add(it) }
        notifyDataSetChanged()
    }

    //Poderia ter o "inner", mas nesse caso ele é opcional
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCityName: TextView = itemView.tv_city_name
        private val tvCityCode: TextView = itemView.tv_id_number
        private val ivWeather: ImageView = itemView.iv_city_weather

        fun bindView(element: Element) = with(itemView) {
            tvCityName.text = element.name
            tvCityCode.text = element.id.toString()

            Glide.with(context)
                .load("http://openweathermap.org/img/wn/${element.weather[0].icon}@4x.png")
                .placeholder(R.drawable.ic_baseline_cloud_24)
                .error(R.drawable.ic_baseline_cloud_24)
                .circleCrop()
                .into(ivWeather)
        }
    }

    class MyItemDecoration(private val height: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = height
                }
                left = height
                right = height
                bottom = height
            }
        }
    }
}