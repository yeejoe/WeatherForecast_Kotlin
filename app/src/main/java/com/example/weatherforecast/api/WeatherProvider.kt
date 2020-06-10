package com.example.weatherforecast.api

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.models.ForecastWeatherModel
import com.example.weatherforecast.models.LocationModel
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.GlobalData
import com.example.weatherforecast.utils.UrlHelper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object WeatherProvider{
    fun fetchWeather(location : String) : WeatherModel {
        val (_,_, result) = Fuel.get(UrlHelper.WEATHER_API_URL +"data/2.5/weather?q=${location}&APPID=e86bf40087be1e52116c2c02d70f702e")
            .responseObject<WeatherModel>()
        return result.get()
    }
    fun fetchForecastWeather(location : String) : ForecastWeatherModel {
        val (_,_, result) = Fuel.get(UrlHelper.WEATHER_API_URL +"data/2.5/forecast?q=${location}&APPID=e86bf40087be1e52116c2c02d70f702e")
            .responseObject<ForecastWeatherModel>()
        return result.get()
    }
    fun fetchWeatherCoordinate() : WeatherModel {
        val (_,_, result) = Fuel.get(UrlHelper.WEATHER_API_URL +"data/2.5/weather?lat=${GlobalData.currentLat}&lon=${GlobalData.currentLng}&APPID=e86bf40087be1e52116c2c02d70f702e")
            .responseObject<WeatherModel>()
        return result.get()
    }
    fun fetchForecastWeatherCoordinate() : ForecastWeatherModel {
        val (_,_, result) = Fuel.get(UrlHelper.WEATHER_API_URL +"data/2.5/forecast?lat=${GlobalData.currentLat}&lon=${GlobalData.currentLng}&APPID=e86bf40087be1e52116c2c02d70f702e")
            .responseObject<ForecastWeatherModel>()
        return result.get()
    }
}