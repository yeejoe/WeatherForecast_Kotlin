package com.example.weatherforecast.models

import android.os.Parcel
import android.os.Parcelable

data class ForecastWeatherModel (
    var list : List<WeatherModel>,
    var cnt : Int
)