package com.example.persistenceapp.service

import com.example.persistenceapp.model.City
import com.example.persistenceapp.model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Terei acesso as ações definidas na api - Criando a interface do serviço
//Ação e parâmetro
interface OpenWeatherService {
    //Antes:
    //http://api.openweathermap.org/data/2.5/weather?q=Recife&APPID=6034cbdc7a2448d77abc8e79ee5a47c0
    //Agora:
    //http://api.openweathermap.org/data/2.5/weather?id=2172797&appid=6034cbdc7a2448d77abc8e79ee5a47c0
    @GET("weather")
    fun getCityWeather(
        //@Query("q") city : Long,
        @Query("id") id: Long,

        //@Path("id) id: Long,
        @Query("APPID") appid: String = "6034cbdc7a2448d77abc8e79ee5a47c0"
    ): Call<City>     //Call do Retrofit2

    @GET("find")
    fun findTemperatures(
        @Query("q") cityName: String,
        @Query("units") units: String = "metrics",
        @Query("APPID") appid: String = "6034cbdc7a2448d77abc8e79ee5a47c0"
    ): Call<Root>

}