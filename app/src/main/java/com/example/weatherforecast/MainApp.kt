package com.example.weatherforecast

import android.app.Application
import org.json.JSONArray
import org.json.JSONException
import java.io.*

class MainApp : Application() {
    companion object{
        var allLocations : JSONArray = JSONArray()
    }
    override fun onCreate() {
        super.onCreate()
        readLocationFile()
    }
    private fun readLocationFile(){
        try {
            val jsonString: String = resources.openRawResource(R.raw.locations).bufferedReader().use { it.readText() }
            allLocations = JSONArray (jsonString)
        }catch (ioE : IOException){
            ioE.printStackTrace()
        }catch (e : JSONException){
            e.printStackTrace()
        }
    }
}