package com.annabellim.myweatherforecast

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UtilitiesTest {

    @Test
    fun formattedDateWhenInputIsValid() {
        val l : Long = 1646582400
        val exp = "MARCH 6, 2022"
        val result = Utilities.getFormattedDate(l)
        assertThat(result).isEqualTo(exp)
    }

    @Test
    fun formattedDateWhenInputIsInvalid() {
        val l : Long = -44343454
        val exp = "AUGUST 5, 1968"
        val result = Utilities.getFormattedDate(l)
        assertThat(result).isEqualTo(exp)
    }

    @Test
    fun getWeekDayFromDateAnyInput() {
        val l : Long = 1646582400
        val exp = "SUNDAY"
        val result = Utilities.getWeekDayFromDate(l)
        assertThat(result).isEqualTo(exp)
    }

    @Test
    fun getHourAndMinAnyAnyInput() {
        val l : Long = 1646606514
        val exp = "05:41 pm"
        val result = Utilities.getHourAndMin(l)
        assertThat(result).isEqualTo(exp)
    }
}