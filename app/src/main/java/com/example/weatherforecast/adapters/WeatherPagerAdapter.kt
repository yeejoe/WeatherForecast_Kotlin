package com.example.weatherforecast.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherforecast.fragments.WeatherFragment
import com.example.weatherforecast.models.WeatherModel

class WeatherPagerAdapter(activity : AppCompatActivity) : FragmentStateAdapter(activity){
    private val dataList = ArrayList<String>()

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        val data = dataList[position]
        val fragment = WeatherFragment.create(data)
        return fragment
    }

    fun addWeatherItem(location : String){
        if(!location.contains(location)){
            dataList.add(0, location)
            notifyItemInserted(0)
        }else{
            dataList.remove(location)
            dataList.add(0, location)
            notifyItemInserted(0)
        }
    }
    fun clear(){
        dataList.clear()
        notifyDataSetChanged()
    }
}