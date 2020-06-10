package com.example.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.example.weatherforecast.adapters.WeatherPagerAdapter
import com.example.weatherforecast.api.LocationProvider
import com.example.weatherforecast.api.WeatherProvider
import com.example.weatherforecast.items.LocationItem
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.GlobalData
import com.example.weatherforecast.utils.UrlHelper.WEATHER_API_URL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlinpermissions.KotlinPermissions
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var drawer : Drawer
    private var pagerAdapter : WeatherPagerAdapter? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        LocationProvider.fetchCurrentLocation(this@MainActivity).observe( this, Observer {
            toolbar_title.text = it
        })
        setupDrawer(savedInstanceState)
        setupPagerWithIndicator()
    }

    private fun setupDrawer(savedInstanceState: Bundle?){
        drawer = drawer {
            toolbar = this@MainActivity.toolbar
            closeOnClick = false
            footer{
                primaryItem{
                    name = "Edit"
                    onClick { _ ->
                        val i = Intent(this@MainActivity, LocationActivity::class.java)
                        startActivity(i)
                        drawer.closeDrawer()
                        false
                    }
                    selectable = false
                    selectedColorRes = R.color.colorPrimary
                }
            }
        }
        LocationProvider.fetchAvailableLocation(this@MainActivity).observe( this, Observer {
            drawer.itemAdapter.clear()
            it.forEach {
                if(it.value.isSelected) {
                    drawer.itemAdapter.add(
                        PrimaryDrawerItem().withName(it.key).withSelectedColor(R.color.colorPrimary).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                            override fun onItemClick(
                                view: View?,
                                position: Int,
                                drawerItem: IDrawerItem<*>
                            ): Boolean {
                                drawerItem as PrimaryDrawerItem
                                LocationProvider.selectLocation(this@MainActivity, drawerItem.name.toString())
                                drawer.closeDrawer()
                                return false
                            }

                        }).withSelected(toolbar_title.text == it.key)
                    )
                    drawer.itemAdapter.add(
                        DividerDrawerItem()
                    )
                }
            }
        })
    }

    private fun setupPagerWithIndicator(){
        pagerAdapter = WeatherPagerAdapter(this)
        home_pager.adapter = pagerAdapter
        TabLayoutMediator(home_pager_indicator, home_pager){ tab: TabLayout.Tab, i: Int ->
            home_pager.setCurrentItem(tab.position, true)
        }.attach()

    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData(){
        if (pagerAdapter != null) {
            pagerAdapter!!.clear()
        }
        LocationProvider.fetchLocationList(this@MainActivity).forEach {
            if (pagerAdapter != null) {
                pagerAdapter!!.addWeatherItem(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.main_menu_my_location){
            getLastLocation()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .onAccepted { permissions ->
                //List of accepted permissions
                if(permissions.size == 2){
                    //means all accepted

                    if (isLocationEnabled()) {

                        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                            var location: Location? = task.result
                            if (location == null) {
                                requestNewLocationData()
                            } else {
                                GlobalData.currentLat = location.latitude
                                GlobalData.currentLng = location.longitude
                                addCurrentLocationWeatherInfo()
                            }
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.no_permission_message), Toast.LENGTH_LONG).show()
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this, getString(R.string.no_permission_message), Toast.LENGTH_LONG).show()
                }
            }
            .onDenied { permissions ->
                //List of denied permissions
            }
            .onForeverDenied { permissions ->
                //List of forever denied permissions
            }
            .ask()
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    var mLastLocation: Location = locationResult.lastLocation
                    GlobalData.currentLat = mLastLocation.latitude
                    GlobalData.currentLng = mLastLocation.longitude
                    addCurrentLocationWeatherInfo()
                }
            },
            Looper.myLooper()
        )
    }

    private fun addCurrentLocationWeatherInfo(){
        if(pagerAdapter != null){
            pagerAdapter!!.addWeatherItem("Self")
        }
    }
}
