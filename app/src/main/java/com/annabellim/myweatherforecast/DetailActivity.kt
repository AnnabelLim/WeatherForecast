package com.annabellim.myweatherforecast

import android.os.Bundle
import android.util.Log
import com.annabellim.myweatherforecast.databinding.ActivityDetailBinding

private const val TAG = "DetailActivity"

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG,"onCreate starts")
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeFullScreen()
        //  pull the data from the bundle and put in the UI
        binding.tvLoc.text = intent.getStringExtra("LOCATION")
        binding.tvDate.text = intent.getStringExtra("DATE")
        binding.tvWeather.text = intent.getStringExtra("DESCR")
        binding.tvSunrise.text = intent.getStringExtra("SUNRISE")
        binding.tvSunset.text = intent.getStringExtra("SUNSET")
        binding.tvDay.text = intent.getStringExtra("DAY")
        binding.tvNight.text = intent.getStringExtra("NIGHT")
        binding.tvEve.text = intent.getStringExtra("EVE")
        binding.tvMorn.text = intent.getStringExtra("MORN")
        binding.tvMin.text = intent.getStringExtra("MIN")
        binding.tvMax.text = intent.getStringExtra("MAX")

        binding.tvPressure.text = intent.getStringExtra("PRESSURE")
        binding.tvHumidity.text = intent.getStringExtra("HUMIDITY")
        binding.tvSpeed.text = intent.getStringExtra("SPEED")
        binding.tvGust.text = intent.getStringExtra("GUST")

        binding.button.setOnClickListener {
            finish()
        }
    }
}