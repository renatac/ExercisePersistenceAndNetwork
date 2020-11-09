package com.example.persistenceapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.persistenceapp.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var prefs : SharedPreferences
    private lateinit var rgTemperature : RadioGroup
    private lateinit var rgLanguage : RadioGroup

    private lateinit var rbCelsius : RadioButton
    private lateinit var rbF : RadioButton
    private lateinit var rbEnglish : RadioButton
    private lateinit var rbPortuguese : RadioButton

    private var temperatureUnit = ""
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
        prefs = view.context.getSharedPreferences("my_weather_prefs", Context.MODE_PRIVATE)

        btnSave.setOnClickListener {
            onSavedClicked(it)
        }

        rbCelsius = view.findViewById(R.id.rb_c)
        rbF = view.findViewById(R.id.rb_f)
        rbEnglish= view.findViewById(R.id.rb_english)
        rbPortuguese = view.findViewById(R.id.rb_portuguese)

        language = prefs?.getString("language", "EN").toString()
        temperatureUnit = prefs?.getString("temperature_unit", "C").toString()

        when(temperatureUnit){
            "C" -> rbCelsius.isChecked = true
            "F" -> rbF.isChecked = true
        }

        when(language){
            "EN" -> rbEnglish.isChecked = true
            "PT" -> rbPortuguese.isChecked = true
        }

        rgTemperature = view.findViewById(R.id.rg_temperature_unit)
        rgLanguage = view.findViewById(R.id.rg_language)

        rgTemperature.setOnCheckedChangeListener{ view, id ->
            val radioButton = view.findViewById<RadioButton>(id)

            if(radioButton.isChecked){
                when(radioButton.id){
                    R.id.rb_c -> temperatureUnit = "C"
                    R.id.rb_f -> temperatureUnit = "F"
                }
            }
        }
        
        rgLanguage.setOnCheckedChangeListener { view, id ->
            val radioButton = view.findViewById<RadioButton>(id)

            if(radioButton.isChecked){
                when(radioButton.id){
                    R.id.rb_english -> language = "EN"
                    R.id.rb_portuguese -> language = "PT"
                }
            }
        }
    }

    protected fun onSavedClicked(view: View){
        //vai salvar os dados no SharedPreferences
        val editor = prefs?.edit()
        editor?.apply {
            putString("temperature_unit", temperatureUnit)
            putString("language", language)
            apply()
        }
    }
}