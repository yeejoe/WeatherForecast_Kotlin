package com.example.weatherforecast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.example.weatherforecast.R
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.UrlHelper.WEATHER_ICON_URL
import com.example.weatherforecast.utils.convertKelvinToCelsius
import kotlinx.android.synthetic.main.custom_weather_bottom_view.view.*
import kotlinx.android.synthetic.main.fragment_weather_forecast.*
import java.text.SimpleDateFormat
import java.util.*


class WeatherForecastFragment : Fragment() {
    private var model: WeatherModel? = null
    private val WEATHER_KEY = "weather_location_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            model = it.getParcelable(WEATHER_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(model != null){
            weather_forecast_cloud_status.load(WEATHER_ICON_URL+model!!.weather[0].icon+".png")
            weather_forecast_temp_value.text = String.format(requireContext().getString(R.string.weather_forecast_temp_template), convertKelvinToCelsius(model!!.main?.temp ?: 0.0))
            val tempDateFormatter = SimpleDateFormat("MMM dd")
            val tempTimeFormatter = SimpleDateFormat("h:mm a")
            weather_forecast_date.text = tempDateFormatter.format(Calendar.getInstance().time)
            weather_forecast_time.text = tempTimeFormatter.format(Calendar.getInstance().time)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(model: WeatherModel) =
            WeatherForecastFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WEATHER_KEY, model)
                }
            }
    }
}