package com.example.weatherforecast.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherforecast.R
import com.example.weatherforecast.models.WeatherModel
import com.example.weatherforecast.utils.convertKelvinToCelsius
import kotlinx.android.synthetic.main.custom_weather_right_view.view.*

class WeatherRightView : ConstraintLayout {
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
        inflater.inflate(R.layout.custom_weather_right_view,this,true)
    }
    fun build(model : WeatherModel){
        weather_right_max_value.text = String.format(context.getString(R.string.weather_right_max_template), convertKelvinToCelsius( model.main?.temp_max ?: 0.0))
        weather_right_pressure_value.text = String.format(context.getString(R.string.weather_right_pressure_template), model.main?.pressure ?: 0)
    }
}