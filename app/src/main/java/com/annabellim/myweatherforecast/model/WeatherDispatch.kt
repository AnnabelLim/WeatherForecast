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
     *  Get the weather
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
//                    Log.d(TAG, "City Name is " + weatherResponse.city.id + " " + weatherResponse.city.name);
//                    Log.d(TAG, " Country Name is " + weatherResponse.city.country);
//                    Log.d(TAG, "cod is " + weatherResponse.cod);
//                    Log.d(TAG, " Lat is " + weatherResponse.city.coord.lat);
//                    Log.d(TAG, " Lon is " + weatherResponse.city.coord.lon);
//                    Log.d(TAG, " Date is " + weatherResponse.list[0].dt);
//                    Log.d(TAG, " Sunrise is " + weatherResponse.list[0].sunrise);
//                    Log.d(TAG, " Sunset is " + weatherResponse.list[0].sunset);
//                    Log.d(TAG, " Pressure is " + weatherResponse.list[0].pressure);
//                    Log.d(TAG, " Humidity is " + weatherResponse.list[0].humidity);
//                    Log.d(TAG, " Temp day is " + weatherResponse.list[0].temp.day);
//                    Log.d(TAG, " Temp min is " + weatherResponse.list[0].temp.min);
//                    Log.d(TAG, " Temp max is " + weatherResponse.list[0].temp.max);
//                    Log.d(TAG, " Temp night is " + weatherResponse.list[0].temp.night);
//                    Log.d(TAG, " Temp eve is " + weatherResponse.list[0].temp.eve);
//                    Log.d(TAG, " Temp morn is " + weatherResponse.list[0].temp.morn);
//                    Log.d(TAG, " Temp night is " + weatherResponse.list[0].temp.night);
//                    Log.d(TAG, " Weather id is " + weatherResponse.list[0].weather[0].id);
//                    Log.d(TAG, " Weather main is " + weatherResponse.list[0].weather[0].main);
//                    Log.d(TAG, " Weather descr is " + weatherResponse.list[0].weather[0].description);

                    Log.d(TAG, "start db update")
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
                    Log.d(TAG, "finish db update")
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(TAG, "call failed")
            }
        })

    }
}