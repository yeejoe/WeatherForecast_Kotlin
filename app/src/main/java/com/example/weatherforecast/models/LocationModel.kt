package com.example.weatherforecast.models


data class LocationModel(
    var locationObject : Location,
    var isSelected : Boolean
)

data class Location(
    val city : String,
    val lat : Double,
    val lng : Double
)