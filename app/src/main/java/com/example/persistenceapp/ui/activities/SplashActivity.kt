package com.example.persistenceapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.persistenceapp.R
import kotlinx.android.synthetic.main.splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var settingsPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splash)
        super.onCreate(savedInstanceState)
        showSplash()
    }

    private fun showSplash() {
        val maxSplashTime: Long = 5000
        var progressSplash: Long = 0
        val percentResult = maxSplashTime / 100

        for(x in 0..100){
            Handler().postDelayed({
                progressSplashBar.progress = x
            }, progressSplash)
            progressSplash = progressSplash + percentResult
        }

        Handler().postDelayed({
            recoverLanguageSetting()
            finish()
        },maxSplashTime)
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
       refresh.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(refresh)
    }
}