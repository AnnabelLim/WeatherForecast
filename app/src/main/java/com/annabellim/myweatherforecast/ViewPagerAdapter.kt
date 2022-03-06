package com.annabellim.myweatherforecast

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.annabellim.myweatherforecast.model.ForecastTable

// The Adapter expects to receive data that is an array of PagerModels
class PagerModel(
    var image_drawable: Int,
    var forecastTable: ForecastTable
)
// The ViewPagerAdapter puts the data in the View Pager
class ViewPagerAdapter(context: Context, private val pagerModelList: ArrayList<PagerModel>) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return pagerModelList.size
    }


    override fun instantiateItem(view: ViewGroup, position: Int): Any {

        // populate the pager with data received
        val pagerLayout = inflater.inflate(R.layout.pager_page, view, false)!!

        val image = pagerLayout.findViewById(R.id.image) as ImageView
        image.setImageResource(pagerModelList[position].image_drawable)

        val city = pagerLayout.findViewById(R.id.tvCity) as TextView
        city.text = pagerModelList[position].forecastTable.name

        val country = pagerLayout.findViewById(R.id.tvCountry) as TextView
        country.text = pagerModelList[position].forecastTable.country

        val week = pagerLayout.findViewById(R.id.tvWeek) as TextView
        week.text = Utilities.getWeekDayFromDate(pagerModelList[position].forecastTable.dt.toLong())

        val date = pagerLayout.findViewById(R.id.tvDate) as TextView
        date.text = Utilities.getFormattedDate(pagerModelList[position].forecastTable.dt.toLong())

        val temp = pagerLayout.findViewById(R.id.tvTemp) as TextView
        temp.text = view.context.getString(R.string.temp, pagerModelList[position].forecastTable.day.toString())

        val main = pagerLayout.findViewById(R.id.tvMain) as TextView
        main.text = pagerModelList[position].forecastTable.main

        view.addView(pagerLayout, 0)
        return pagerLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}