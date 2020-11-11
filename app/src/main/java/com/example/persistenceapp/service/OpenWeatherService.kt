package com.example.persistenceapp.service

import com.example.persistenceapp.model.City
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Terei acesso as ações definidas na api - Criando a interface do serviço
interface OpenWeatherService {
    //http://api.openweathermap.org/data/2.5/weather?q=Recife&APPID=
    @GET("weather")
    fun getCityWeather(
        @Query("q") cityName : String,
        @Query("APPID") appid : String = "6034cbdc7a2448d77abc8e79ee5a47c0"
    ) : Call<City>
    //Call do Retrofit2
}