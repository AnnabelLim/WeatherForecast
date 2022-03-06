package com.annabellim.myweatherforecast.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherDispatch"

object WeatherDispatch {

    /**********************************************
     *  Get the weather using Retrofit
     **********************************************/
     suspend fun getWeatherForecastAndPutInDb(lat: Double, lon: Double, db: AppDatabase) = withContext(Dispatchers.IO) {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getWeatherForecast(lat.toString(), lon.toString(), FORECAST_DAYS, FORECAST_UNITS, APP_ID)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    Log.d(TAG, "start database update")
                    db.forecastDao().deleteAll()
                    var id = 0
                    for (fc in weatherResponse.list) {
                        id+=1
                        val forecastTable = ForecastTable(
                            id,
                            weatherResponse.city.name,
                            weatherResponse.city.country,
                            fc.dt,
                            fc.weather[0].id,
                            fc.weather[0].main,
                            fc.weather[0].description,
                            fc.sunrise,
                            fc.sunset,
                            fc.temp.day,
                            fc.temp.night,
                            fc.temp.eve,
                            fc.temp.morn,
                            fc.temp.min,
                            fc.temp.max,
                            fc.pressure,
                            fc.humidity,
                            fc.speed,
                            fc.deg,
                            fc.gust,
                            fc.clouds,
                        )
                        db.forecastDao().insert(forecastTable)
                    }
                    Log.d(TAG, "finish database update")
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(TAG, "call failed")
            }
        })

    }
}