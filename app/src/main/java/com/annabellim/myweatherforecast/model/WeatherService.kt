package com.annabellim.myweatherforecast.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// This is the structure of the response received from the weather forecast api call
interface WeatherService {
    @GET("data/2.5/forecast/daily?")
    fun getWeatherForecast(@Query("lat") lat: String,
                           @Query("lon") lon: String,
                           @Query("cnt") cnt: String,
                           @Query("units") units: String,
                           @Query("appid") app_id: String): Call<WeatherResponse>

}