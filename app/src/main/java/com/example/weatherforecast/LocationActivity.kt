package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.api.LocationProvider
import com.example.weatherforecast.items.LocationItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    var locationAdapter = FastAdapter<LocationItem>()
    var locationItemAdapter = ItemAdapter<LocationItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setSupportActionBar(toolbar)
        setupRecyclerView()
        getLocations()
        toolbar_back.setOnClickListener {
            finish()
        }
    }
    private fun setupRecyclerView(){
        locationAdapter = FastAdapter.with(locationItemAdapter)
        locationAdapter.onClickListener = { view: View?, iAdapter: IAdapter<LocationItem>, locationItem: LocationItem, i: Int ->
            val locationModel = locationItem.locationModel
            if(locationModel != null){
                LocationProvider.toggleLocation(this@LocationActivity, locationModel.locationObject.city)
            }
            false
        }
        location_rv.adapter = locationAdapter
        location_rv.layoutManager = LinearLayoutManager(this)
        location_rv.itemAnimator = null
    }
    private fun getLocations(){
        LocationProvider.fetchAvailableLocation(this@LocationActivity).observe( this, Observer {
            locationItemAdapter.clear()
            it.forEach {
                val item = LocationItem()
                item.locationModel = it.value
                locationItemAdapter.add(item)
            }
        })
    }

}
