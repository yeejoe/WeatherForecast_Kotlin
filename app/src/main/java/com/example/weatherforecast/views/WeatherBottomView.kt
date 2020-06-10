package com.example.weatherforecast.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherforecast.R
import com.example.weatherforecast.models.WeatherModel
import kotlinx.android.synthetic.main.custom_weather_bottom_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherBottomView : ConstraintLayout {
    constructor(context: Context?) : super(context){
        setupView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        setupView()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        setupView()
    }

    private fun setupView(){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_weather_bottom_view,this,true)
    }
    fun build(model : WeatherModel){
        weather_bottom_status_text.text = model.weather[0].main
        weather_bottom_wind.text = String.format(context.getString(R.string.weather_bottom_wind_template), model.wind?.speed ?: 0.0)
        val tempDateFormatter = SimpleDateFormat("MMM dd")
        val tempTimeFormatter = SimpleDateFormat("h:mm a")

        weather_bottom_date.text = tempDateFormatter.format(Calendar.getInstance().time)
        weather_bottom_time.text = tempTimeFormatter.format(Calendar.getInstance().time)
    }
}