package com.example.weatherforecast.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherforecast.R
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.convertKelvinToCelsius
import kotlinx.android.synthetic.main.custom_weather_top_view.view.*

class WeatherTopView : ConstraintLayout {
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
        inflater.inflate(R.layout.custom_weather_top_view,this,true)
    }
    fun build(model : WeatherModel){
        weather_top_temp_value.text = String.format("%.1f", convertKelvinToCelsius(model.main?.temp ?: 0.0))
        weather_top_feel_like.text = String.format(context.getString(R.string.weather_top_feel_like_template), convertKelvinToCelsius(model.main?.feels_like ?: 0.0))
    }
}