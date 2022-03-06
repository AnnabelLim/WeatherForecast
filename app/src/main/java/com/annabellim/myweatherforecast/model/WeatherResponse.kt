package com.annabellim.myweatherforecast.model

import java.util.*

// This is the structure of the response received from the weather forecast api call
data class WeatherResponse(val city: City,
                           val cod: String,
                           val list: ArrayList<Day>)

data class City(val id: Int,
                val name: String,
                val country: String,
                val coord: Coord)

data class Coord( val lon: Double,
                  val lat: Double)

data class Day(val dt: Int,
               var sunrise: Int,
               var sunset: Int,
               var temp: Temp,
               var pressure: Int,
               var humidity: Int,
               var weather: ArrayList<Weather>,
               var speed: Double,
               var deg: Int,
               var gust: Double,
               var clouds: Int)

data class Weather(var id: Int,
                   var main: String,
                   var description: String,
                   var icon: String)

data class Temp(var day: Double,
                var min: Double,
                var max: Double,
                var night: Double,
                var eve: Double,
                var morn: Double)

