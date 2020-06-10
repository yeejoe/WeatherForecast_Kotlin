package com.example.weatherforecast.models

import android.os.Parcel
import android.os.Parcelable

data class WeatherModel (
    var weather : List<Weather>,
    var main : Main?,
    var cloud : Cloud?,
    var wind : Wind?,
    var base : String?,
    var dt : Int,
    var name : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        listOf<Weather>().apply {
            parcel.readList(this, Weather::class.java.classLoader)
        },
        parcel.readParcelable(Main::class.java.classLoader),
        parcel.readParcelable(Cloud::class.java.classLoader),
        parcel.readParcelable(Wind::class.java.classLoader),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(weather)
        parcel.writeParcelable(main, flags)
        parcel.writeParcelable(cloud, flags)
        parcel.writeParcelable(wind, flags)
        parcel.writeString(base)
        parcel.writeInt(dt)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<WeatherModel> {
        override fun createFromParcel(parcel: Parcel): WeatherModel {
            return WeatherModel(parcel)
        }

        override fun newArray(size: Int): Array<WeatherModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "WeatherModel(weathers=$weather, main=$main, cloud=$cloud, wind=$wind, base=$base, dt=$dt, name=$name)"
    }

}

data class Weather(var main : String?,
                   var desciprtion : String?,
                   var icon : String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(main)
        parcel.writeString(desciprtion)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Weather(main=$main, desciprtion=$desciprtion, icon=$icon)"
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }

}

data class Main(var temp : Double,
                var feels_like : Double,
                var temp_min : Double,
                var temp_max : Double,
                var pressure : Int,
                var humidity : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(temp)
        parcel.writeDouble(feels_like)
        parcel.writeDouble(temp_min)
        parcel.writeDouble(temp_max)
        parcel.writeInt(pressure)
        parcel.writeInt(humidity)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Main(temp=$temp, feels_like=$feels_like, temp_min=$temp_min, temp_max=$temp_max, pressure=$pressure, humidity=$humidity)"
    }

    companion object CREATOR : Parcelable.Creator<Main> {
        override fun createFromParcel(parcel: Parcel): Main {
            return Main(parcel)
        }

        override fun newArray(size: Int): Array<Main?> {
            return arrayOfNulls(size)
        }
    }

}

data class Wind(var speed : Double) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(speed)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Wind(speed=$speed)"
    }

    companion object CREATOR : Parcelable.Creator<Wind> {
        override fun createFromParcel(parcel: Parcel): Wind {
            return Wind(parcel)
        }

        override fun newArray(size: Int): Array<Wind?> {
            return arrayOfNulls(size)
        }
    }

}

data class Cloud(var all : Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(all)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Cloud(all=$all)"
    }

    companion object CREATOR : Parcelable.Creator<Cloud> {
        override fun createFromParcel(parcel: Parcel): Cloud {
            return Cloud(parcel)
        }

        override fun newArray(size: Int): Array<Cloud?> {
            return arrayOfNulls(size)
        }
    }

}