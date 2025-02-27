package com.example.persistenceapp.manager

import com.example.persistenceapp.service.OpenWeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Classe para obter uma instância do serviço Retrofit, passando pra ela um base url. As ações mudam,
//mas a base é a mesma. O converter faz a serialização e desserealização do meu Json.
class OpenWeatherManager {
    private val instance = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getOpenWeatherService() = instance.create(OpenWeatherService::class.java)
}