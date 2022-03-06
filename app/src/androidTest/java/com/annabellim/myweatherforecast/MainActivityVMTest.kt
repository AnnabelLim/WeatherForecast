package com.annabellim.myweatherforecast

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.annabellim.myweatherforecast.model.AppDatabase
import com.annabellim.myweatherforecast.model.ForecastDao
import com.annabellim.myweatherforecast.model.ForecastTable
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityVMTest : TestCase() {

    private lateinit var viewModel: MainActivityVM
    private lateinit var db: AppDatabase
    private lateinit var dao: ForecastDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        dao = db.forecastDao()
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
        viewModel = MainActivityVM(Application())

    }

    @Test
    fun testMainActivityViewModel() {
        viewModel.getData(dao)
        val result = viewModel.forecastData.getOrAwaitValue().find {
            it.name == "Mont Vernon" && it.sunrise == 1646565278
        }
        assertThat(result != null).isTrue()

    }
}