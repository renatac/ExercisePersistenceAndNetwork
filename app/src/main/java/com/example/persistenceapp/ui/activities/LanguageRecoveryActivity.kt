package com.example.persistenceapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class LanguageRecoveryActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        recoverLanguageSetting()
        super.onCreate(savedInstanceState)
    }

    private fun recoverLanguageSetting() {
        settingsPrefs =
            applicationContext.getSharedPreferences("my_weather_prefs", Context.MODE_PRIVATE)
        val language = settingsPrefs?.getString("language", "").toString()
        setLocale(language)
    }

    @Suppress("DEPRECATION")
    private fun setLocale(localeName: String) {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(localeName)
        res.updateConfiguration(conf, dm)
        val refresh = Intent(
            applicationContext,
            MainActivity::class.java
        )
//        refresh.addFlags(
//            Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
//        )
        startActivity(refresh)
    }
}