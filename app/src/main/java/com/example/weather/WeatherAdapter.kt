package com.example.weather

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.trimSubstring
import java.io.IOException
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class WeatherAdapter(val weatherdata: WeatherData) {

    fun GetIcons():MutableList<String>{
        val list:MutableList<String> = mutableListOf()
        for(i in 0..weatherdata.forecast.forecastday.size-1){
           list.add("https:${weatherdata.forecast.forecastday[i].day.condition.icon}")

        }


        return list
    }
    fun GetTemp():MutableList<String>{
        val list:MutableList<String> = mutableListOf()
        for(i in 0..weatherdata.forecast.forecastday.size-1){
            list.add("${weatherdata.forecast.forecastday[i].day.avgtemp_c}°")
        }
        return list

    }
    fun GetDaysDate():MutableList<String>{
        val list:MutableList<String> = mutableListOf()
        for (i in 0..weatherdata.forecast.forecastday.size-1){
            list.add(weatherdata.forecast.forecastday[i].date)
        }
        return list
    }

    fun GetSunrise():MutableList<String>{
        val list:MutableList<String> = mutableListOf()
        for (i in 0..weatherdata.forecast.forecastday.size-1){
            list.add("Sunrise at: ${weatherdata.forecast.forecastday[i].astro.sunrise}")
        }
        return list

    }
    fun getmaxwindkph():MutableList<String>{
        val list:MutableList<String> =mutableListOf()
        for(i in 0..weatherdata.forecast.forecastday.size-1)
        {
            list.add("Max wind speed: "+ "${weatherdata.forecast.forecastday[i].day.maxwind_kph.toString()}"+"kph")
        }
        return list
    }

    fun getCurrentHour():Int{
        val calendar= Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY)
    }


    fun generateWeatherHourly():ArrayList<HourlyWeather>{
        val currenthour=getCurrentHour()
        val hourly:ArrayList<HourlyWeather> =ArrayList()

        for(i in currenthour..23){
            hourly.add((HourlyWeather(weatherdata.forecast.forecastday[0].hour[i].time.trimSubstring(10,16),
                weatherdata.forecast.forecastday[0].hour[i].temp_c.toString()+"°",
                "https:${weatherdata.forecast.forecastday[0].hour[i].condition.icon}"
                )
                    )
            )
        }
        for(i in 0..currenthour){
            hourly.add((HourlyWeather(weatherdata.forecast.forecastday[0].hour[i].time.trimSubstring(10,16),
                weatherdata.forecast.forecastday[0].hour[i].temp_c.toString()+"°",
                "https:${weatherdata.forecast.forecastday[0].hour[i].condition.icon}"
            )
                    )
            )
        }

        return hourly


    }
    fun getLastUpdate():String{
        val lastupdate=weatherdata.current.last_updated
        return lastupdate
    }


}
