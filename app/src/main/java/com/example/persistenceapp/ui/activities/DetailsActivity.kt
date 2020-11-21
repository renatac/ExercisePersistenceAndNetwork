package com.example.persistenceapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.persistenceapp.R
import com.example.persistenceapp.model.Element
import com.example.persistenceapp.ui.activities.MainActivity.Companion.MODEL_ELEMENT
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var element: Element? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

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
    }

}