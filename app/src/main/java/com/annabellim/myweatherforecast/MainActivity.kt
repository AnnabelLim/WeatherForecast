package com.annabellim.myweatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.annabellim.myweatherforecast.databinding.ActivityMainBinding
import com.annabellim.myweatherforecast.model.AppDatabase
import com.annabellim.myweatherforecast.model.RC_LOCATION_PERMISSION
import com.annabellim.myweatherforecast.model.WeatherDispatch
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var pagerList = ArrayList<PagerModel>()
    private var selPos = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeFullScreen()

        // get location permission so we can get weather for this particular location
        manageLocation()

        // On the pager, listen to position changes
        val viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) { selPos = position }
            }

        // On the pager, listen for touches that are not swipes
        val viewPagerOnTouchListener: View.OnTouchListener =
            object: View.OnTouchListener {
                var pointX: Float = 0F
                var pointY: Float = 0F
                var tolerance = 50
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_MOVE -> { return false; }
                        MotionEvent.ACTION_DOWN -> {
                            pointX = event.x
                            pointY = event.y
                        }
                        MotionEvent.ACTION_UP -> {
                            val sameX = pointX + tolerance > event.x && pointX - tolerance < event.x
                            val sameY = pointY + tolerance > event.y && pointY - tolerance < event.y
                            if(sameX && sameY){
                                Log.d(TAG, "proceed with position =  $selPos")
                                navigateToDetail()
                            }
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            }

        binding.pager.setPadding(50, 0, 50, 0)
        binding.pager.clipToPadding = false
        binding.pager.pageMargin = 25
        binding.pager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding.pager.setOnTouchListener(viewPagerOnTouchListener)

        // get the data from database
        Log.d(TAG, "Getting data from db from main")
        getData()
    }

    /**********************************************
     *  Navigate to Detail
     **********************************************/
    fun navigateToDetail() {

        val intent = Intent(this, DetailActivity::class.java)

        val data = pagerList[selPos].forecastTable

        // format all the data that needs to be displayed in detailed view
        val loc = data.name + ", " + data.country
        val date = Utilities.getWeekDayFromDate(data.dt.toLong()) + " " + Utilities.getFormattedDate(data.dt.toLong())
        val sunrise = Utilities.getHourAndMin(data.sunrise.toLong())
        val sunset = Utilities.getHourAndMin(data.sunset.toLong())
        intent.putExtra("LOCATION", loc)
        intent.putExtra("DATE", date)
        intent.putExtra("DESCR", "Forecast: ${data.description}")
        intent.putExtra("SUNRISE", "Sunrise: $sunrise")
        intent.putExtra("SUNSET", "Sunset: $sunset")
        intent.putExtra("DAY", "Day Temperature: ${data.day} ℉")
        intent.putExtra("NIGHT", "Night Temperature: ${data.night} ℉")
        intent.putExtra("EVE", "Evening Temperature: ${data.eve} ℉")
        intent.putExtra("MORN", "Morning Temperature: ${data.morn} ℉")
        intent.putExtra("MIN", "Mininum Temperature: ${data.min} ℉")
        intent.putExtra("MAX", "Maximum Temperature: ${data.max} ℉")
        intent.putExtra("PRESSURE", "Pressure: ${data.pressure}")
        intent.putExtra("HUMIDITY", "Humidity: ${data.humidity} %")
        intent.putExtra("SPEED", "Wind Speed: ${data.speed} miles/hour")
        intent.putExtra("GUST", "Wind Gust: ${data.gust} miles/hour")

        startActivity(intent)
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        //     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    /**********************************************
     *  Get Data from Database
     **********************************************/
    private fun getData() {

        // ask view model to get the forecast from database
        val vm = ViewModelProvider(this).get(MainActivityVM::class.java)
        val dao = AppDatabase.getDatabase(applicationContext).forecastDao()
        vm.getData(dao)

        // observe the forecast live data
        vm.forecastData.observe(this, {
            Log.d(TAG, "Getting data from db count is " + it.count())
            if (it != null && it.count() > 0) {
                pagerList.clear()
                for (fc in it) {
                    when {
                        fc.main.contains("Clear") -> {
                            pagerList.add(PagerModel(R.drawable.ic_sunny, fc))
                        }
                        fc.main.contains("Rain") -> {
                            pagerList.add(PagerModel(R.drawable.ic_rain, fc))
                        }
                        fc.main.contains("Snow") -> {
                            pagerList.add(PagerModel(R.drawable.ic_snowy, fc))
                        }
                        else -> {
                            pagerList.add(PagerModel(R.drawable.ic_cloudy, fc))
                        }
                    }
                }
            }
            // after we receive the forecast data, pass it to adapter
            binding.pager.adapter = ViewPagerAdapter(this, this.pagerList)
        })

    }

    /**********************************************
     *  Get the User Location
     **********************************************/
    private fun manageLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // ask for permission
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),  RC_LOCATION_PERMISSION)
        } else {
            // we got permission so proceed to get location
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d(TAG, "Getting location")
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        this.fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d(TAG, "Location is $location")
                lifecycleScope.launch(Dispatchers.IO) {
                    // if we got the location coordinates, use this data to request weather forecast information
                    WeatherDispatch.getWeatherForecastAndPutInDb(location.latitude, location.longitude, AppDatabase.getDatabase(applicationContext))
                }
                Log.d(TAG, "pull data from db after server get")
                Handler(Looper.getMainLooper()).postDelayed({ getData() }, 500)
            } else {
                Log.d(TAG, "Location is NULL!!")
                val toast = Toast.makeText(applicationContext,"Cannot obtain location.",Toast.LENGTH_LONG)
                toast.setMargin(50F,50F)
                toast.show()
            }
        }

    }

    override fun onRequestPermissionsResult (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_LOCATION_PERMISSION  -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                // we are granted permission so proceed to get location
                getLocation()
            }
            else {
                Log.d(TAG, "No permission to get location")
                val toast = Toast.makeText(applicationContext,"No permission to get location.",Toast.LENGTH_LONG)
                toast.setMargin(50F,50F)
                toast.show()
            }
        }
    }

}

