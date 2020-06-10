package com.example.weatherforecast.api

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.MainApp
import com.example.weatherforecast.models.Location
import com.example.weatherforecast.models.LocationModel
import com.example.weatherforecast.utils.ORI_LOCATIONS
import com.example.weatherforecast.utils.PREF_FILE_NAME
import com.example.weatherforecast.utils.PREF_SELECTED_LOCATION
import com.example.weatherforecast.utils.PREF_USER_LOCATIONS

object LocationProvider{
    private val allLocations = MutableLiveData<HashMap<String, LocationModel>>()
    private val currentLocation = MutableLiveData<String>()
    fun fetchCurrentLocation(context: Context) : LiveData<String>{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        currentLocation.value = sharedPreferences.getString(PREF_SELECTED_LOCATION, "Kuala Lumpur")
        return currentLocation
    }
    fun selectLocation(context : Context, locationName: String){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PREF_SELECTED_LOCATION, locationName).commit()
        currentLocation.value = locationName
    }
    fun toggleLocation(context : Context, locationName : String){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val userLocations = sharedPreferences.getStringSet(PREF_USER_LOCATIONS, mutableSetOf())
        if(userLocations != null){
            if(userLocations.contains(locationName)){
                userLocations.remove(locationName)
            }else{
                userLocations.add(locationName)
            }
            sharedPreferences.edit().putStringSet(PREF_USER_LOCATIONS, userLocations).commit()

            val currentLocations = allLocations.value
            if(currentLocations != null){
                val locationModel = currentLocations[locationName]
                if(locationModel != null){
                    locationModel.isSelected = !locationModel.isSelected
                    val newLocations = HashMap<String,LocationModel>()
                    newLocations.putAll(currentLocations)
                    newLocations[locationName] = locationModel
                    allLocations.value = newLocations
                }
            }
        }
    }
    fun fetchLocationList(context : Context): Set<String>{
        val locations = mutableSetOf<String>()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val userLocations = sharedPreferences.getStringSet(PREF_USER_LOCATIONS, mutableSetOf())
        locations.addAll(ORI_LOCATIONS)
        if(userLocations != null) {
            locations.addAll(userLocations)
        }
        return locations.toSet()
    }
    fun fetchAvailableLocation(context : Context) : LiveData<HashMap<String, LocationModel>>{
        val allLocations = MainApp.allLocations
        val selectedLocationList = fetchLocationList(context).toMutableSet()
        for(i in 0 until allLocations.length()){
            val location = allLocations.getJSONObject(i)
            val cityName = location.getString("city")
            val lat = location.getDouble("lat")
            val lng = location.getDouble("lng")
            val currentLocations = this.allLocations.value ?: HashMap()
            val shouldSelect = selectedLocationList.contains(cityName)
            if(shouldSelect) {
                selectedLocationList.remove(cityName)
            }
            if(currentLocations.containsKey(cityName)){
                currentLocations[cityName]!!.isSelected = shouldSelect
            }else{
                val locationInfo = Location(cityName, lat, lng)
                val locationModel = LocationModel(locationInfo, shouldSelect)
                currentLocations[locationInfo.city] = locationModel
            }
            this.allLocations.value = currentLocations
        }
        return this.allLocations
    }
}