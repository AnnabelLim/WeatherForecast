package com.annabellim.myweatherforecast

import java.time.Instant
import java.time.ZoneId

//private const val TAG = "Utilities"

object  Utilities {

    // get date
    fun getFormattedDate(l: Long) : String {
        //Log.d(TAG, "getFormattedDate $l")
        //val date = Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).toLocalDate()
        val month = Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).month.toString()
        val day = Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).dayOfMonth.toString()
        val year = Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).year.toString()
        return "$month $day, $year"
    }

    // get week
    fun getWeekDayFromDate(l: Long) : String {
        // Log.d(TAG, "getWeekDayFromDate $l")
        return Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).dayOfWeek.toString()
    }

    // get time
    fun getHourAndMin(l: Long) : String {
        // Log.d(TAG, "getTime $l")
        val hour =  Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).hour
        val min =   Instant.ofEpochSecond(l).atZone(ZoneId.systemDefault()).minute
        val minText = min.toString().padStart(2, '0')
        var hourText = hour.toString().padStart(2, '0')

        return if (hour < 12) {
            "$hourText:$minText am"
        } else if (hour == 12 ) {
            if (min == 0) {
                "$hourText:$minText nn"
            } else {
                "$hourText:$minText pm"
            }
        } else {
            hourText = (hour-12).toString().padStart(2, '0')
            "$hourText:$minText pm"
        }
    }

}