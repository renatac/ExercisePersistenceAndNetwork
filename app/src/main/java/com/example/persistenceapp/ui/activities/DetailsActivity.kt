package com.example.persistenceapp.ui.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.persistenceapp.R
import com.example.persistenceapp.model.Element
import com.example.persistenceapp.ui.activities.MainActivity.Companion.MODEL_ELEMENT
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.toolbar_included.toolbarMain

class DetailsActivity : BaseActivity() {

    private var element: Element? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupToolbar(toolbarMain, R.string.app_name, true)

        recoverElemtents()
        setCityDatabase()
    }

    private fun recoverElemtents() {
        element = intent.getParcelableExtra(MODEL_ELEMENT)
    }

    private fun setCityDatabase() {
        tv_detalis_city_name.text = element?.name
        tv_detalis_id.text = element?.id.toString()
        tv_details_weather_main.text = element?.weather!![0].main
        tv_details_descryption_weather.text = element?.weather!![0].description

        Glide.with(applicationContext)
            .load("http://openweathermap.org/img/wn/${element!!.weather[0].icon}@4x.png")
            .placeholder(R.drawable.ic_baseline_cloud_24)
            .error(R.drawable.ic_baseline_cloud_24)
            .circleCrop()
            .into(iv_icon)
    }

}