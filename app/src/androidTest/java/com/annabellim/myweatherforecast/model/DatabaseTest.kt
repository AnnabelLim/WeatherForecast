package com.annabellim.myweatherforecast.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest : TestCase() {

    private lateinit var db: AppDatabase
    private lateinit var dao: ForecastDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.forecastDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadForecast() {
        val dataToInsert = ForecastTable(
            1,
            "Mont Vernon",
            "US",1646582400,
            1,
            "Clear",
            "clear skies",
            1646565278,
            1646606514,
            51.42,
            50.05,
            58.59,
            31.78,
            29.5,
            60.73,
            1010,
            83,
            18.9,
            10,
            48.05,
            25
        )
        dao.insert(dataToInsert)
        val dataRetrieved = dao.getForecasts()
        assertThat(dataRetrieved.contains(dataToInsert)).isTrue()

    }
}

