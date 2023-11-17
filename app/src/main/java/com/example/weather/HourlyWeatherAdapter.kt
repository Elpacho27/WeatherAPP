package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.internal.trimSubstring
import java.util.ArrayList

class HourlyWeatherAdapter(private val hourlyWeatherlist:ArrayList<HourlyWeather>) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvday, parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val hourlyWeather =hourlyWeatherlist[position]
        holder.time.text = hourlyWeather.time
        holder.loadWeatherIcon(hourlyWeather.image)
        holder.tempcard.text=hourlyWeather.temp



    }

    override fun getItemCount(): Int {
        return hourlyWeatherlist.size
    }

    class HourlyWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.hourtext)
        val image:ImageView=itemView.findViewById(R.id.hourimage)
        val tempcard:TextView=itemView.findViewById(R.id.tempcard)
        fun loadWeatherIcon(url: String) {
            Glide.with(itemView.context)
                .load(url)
                .into(image )
        }
    }
}