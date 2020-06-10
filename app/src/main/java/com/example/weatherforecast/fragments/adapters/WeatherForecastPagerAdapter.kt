package com.example.weatherforecast.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecast.fragments.WeatherForecastFragment
import com.example.weatherforecast.models.WeatherModel

class WeatherForecastPagerAdapter(fm : Fragment) : FragmentStateAdapter(fm){

    val dataList : ArrayList<WeatherModel> = ArrayList()
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        println("creating fragments")
        return WeatherForecastFragment.newInstance(dataList[position])
    }
}