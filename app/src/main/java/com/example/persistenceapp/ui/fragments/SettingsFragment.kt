package com.example.persistenceapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.persistenceapp.R
import com.example.persistenceapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : Fragment() {

    lateinit var settingsPrefs : SharedPreferences
    private lateinit var rgLanguage : RadioGroup

    private lateinit var locale: Locale
    private var currentLanguage = "en"

    private lateinit var rbEnglish : RadioButton
    private lateinit var rbPortuguese : RadioButton

    private var language = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //false - quer dizer que o container não estará atachado ao layout root
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //context é o contexto da activity e applicationContext é o contexto da aplicação
        settingsPrefs = view.context.getSharedPreferences("my_weather_prefs", Context.MODE_PRIVATE)

        btnSave.setOnClickListener {
            onSavedClicked(it)
        }

        rbEnglish= view.findViewById(R.id.rb_english)
        rbPortuguese = view.findViewById(R.id.rb_portuguese)

        language = settingsPrefs?.getString("language", "").toString()
        currentLanguage = language.toLowerCase()

        when(language){
            "en" -> rbEnglish.isChecked = true
            "" -> rbPortuguese.isChecked = true
        }

        rgLanguage = view.findViewById(R.id.rg_language)
        
        rgLanguage.setOnCheckedChangeListener { view, id ->
            val radioButton = view.findViewById<RadioButton>(id)

            if(radioButton.isChecked){
                when(radioButton.id){
                    R.id.rb_english -> {
                        language = "en"
                    }
                    R.id.rb_portuguese -> {
                        language = ""
                    }
                }
            }
        }
    }

    protected fun onSavedClicked(view: View){
        //Vai salvar os dados no SharedPreferences
        val editor = settingsPrefs?.edit()
        editor?.apply {
            putString("language", language)
            apply()
        }
        setLocale(language)
    }

    @Suppress("DEPRECATION")
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                context,
                MainActivity::class.java
            )
            startActivity(refresh)
        } else {
            Toast.makeText(
                context, getString(R.string.toast_lg_already_selected), Toast.LENGTH_SHORT).show()
        }
    }
}