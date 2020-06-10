package com.example.weatherforecast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load

import com.example.weatherforecast.R
import com.example.weatherforecast.api.WeatherProvider
import com.example.weatherforecast.fragments.adapters.WeatherForecastPagerAdapter
import com.example.weatherforecast.models.ForecastWeatherModel
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.UrlHelper.WEATHER_ICON_URL
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {
    private var model: WeatherModel? = null
    private var forecastModel: ForecastWeatherModel? = null
    private var locationName : String = "Kuala Lumpur"
    private val LOCATION_KEY = "weather_location_name"
    private lateinit var forecastAdapter : WeatherForecastPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locationName = it.getString(LOCATION_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        forecastAdapter = WeatherForecastPagerAdapter(this)
        weather_forecast_pager.adapter = forecastAdapter
        GlobalScope.launch {
            if(locationName == "Self"){

                model = WeatherProvider.fetchWeatherCoordinate()
                if(model != null) {
                    GlobalScope.launch(Dispatchers.Main){

                        weather_status.load(WEATHER_ICON_URL + model!!.weather[0].icon + "@2x.png")
                        weather_top_view.build(model!!)
                        weather_bottom_view.build(model!!)
                        weather_left_view.build(model!!)
                        weather_right_view.build(model!!)
                    }
                }
                forecastModel = WeatherProvider.fetchForecastWeatherCoordinate()
                if(forecastModel != null){
                    GlobalScope.launch(Dispatchers.Main) {
                        println("model list:${forecastModel!!.list.size}")
                        println("model list:${forecastAdapter.dataList.size}")
                        forecastAdapter.dataList.clear()
                        forecastAdapter.dataList.addAll(forecastModel!!.list)
                        println("model list:${forecastAdapter.dataList.size}")
                        forecastAdapter.notifyDataSetChanged()
                    }
                }
            }else{

                model = WeatherProvider.fetchWeather(locationName)
                if(model != null) {
                    GlobalScope.launch(Dispatchers.Main){

                        weather_status.load(WEATHER_ICON_URL + model!!.weather[0].icon + "@2x.png")
                        weather_top_view.build(model!!)
                        weather_bottom_view.build(model!!)
                        weather_left_view.build(model!!)
                        weather_right_view.build(model!!)
                    }
                }
                forecastModel = WeatherProvider.fetchForecastWeather(locationName)
                if(forecastModel != null){
                    GlobalScope.launch(Dispatchers.Main) {
                        forecastAdapter.dataList.clear()
                        forecastAdapter.dataList.addAll(ArrayList(forecastModel!!.list))
                        forecastAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(location: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(LOCATION_KEY, location)
                }
            }
    }
}
