package com.annabellim.myweatherforecast

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.annabellim.myweatherforecast.model.ForecastDao
import com.annabellim.myweatherforecast.model.ForecastTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MainActivityVM(application: Application) : AndroidViewModel(application) {
//
//   private var mForecastData = MutableLiveData<List<ForecastTable>>()
//   val forecastData: LiveData<List<ForecastTable>>
//       get() = mForecastData
//
//
//    fun putData(forecastTable: ForecastTable) = viewModelScope.launch(Dispatchers.Main) {
//        val db = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
//        db.forecastDao().insert(forecastTable)
//    }
//
//    fun getData() = viewModelScope.launch(Dispatchers.Main) {
//        val db = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
//        mForecastData.value = db.forecastDao().getForecasts()
//    }
//}

class MainActivityVM(application: Application)  : AndroidViewModel(application) {

   private var mForecastData = MutableLiveData<List<ForecastTable>>()
   val forecastData: LiveData<List<ForecastTable>>
       get() = mForecastData


    fun getData(forecastDao: ForecastDao) = viewModelScope.launch(Dispatchers.Main) {
        mForecastData.value = forecastDao.getForecasts()
    }
}
