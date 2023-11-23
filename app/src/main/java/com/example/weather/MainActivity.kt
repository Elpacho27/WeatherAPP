package com.example.weather

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var weatherAdapter: HourlyWeatherAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var hourlyWeatherList: ArrayList<HourlyWeather>





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val edittext = findViewById<EditText>(R.id.editTextText)
        val button=findViewById<Button>(R.id.button)

        fun setRecyclerView(weatherHourly: ArrayList<HourlyWeather>) {
            recyclerView = findViewById(R.id.horizontalrv)
            recyclerView.setHasFixedSize(true)
            hourlyWeatherList = weatherHourly
            weatherAdapter = HourlyWeatherAdapter(hourlyWeatherList)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = weatherAdapter
        }

        fun parseJsonToWeatherData(jsonString: String): WeatherData? {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val adapter: JsonAdapter<WeatherData> = moshi.adapter(WeatherData::class.java)

            return try {
                adapter.fromJson(jsonString)
            } catch (e: Exception) {
                // Handle parsing exceptions here
                e.printStackTrace()
                null
            }
        }

        val client = OkHttpClient()

        val city="Osijek"
        var request = Request.Builder()
            .url("https://weatherapi-com.p.rapidapi.com/forecast.json?q=${city}&days=5")
            .get()
            .addHeader("X-RapidAPI-Key", "your_api_key")
            .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
            .build()




       button.setOnClickListener {

            val input=edittext.text.toString()
            val apiurl="https://weatherapi-com.p.rapidapi.com/forecast.json?q=${input}&days=3"

            val client2=OkHttpClient()
            var request2 = Request.Builder()
                .url(apiurl)
                .get()
                .addHeader("X-RapidAPI-Key", "your_api_key")
                .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .build()

            client2.newCall(request2).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    println("Error: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {




                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val weatherData = parseJsonToWeatherData(responseBody.toString().trimIndent())
                        val lista:List<String>
                        Log.i("SIZE",weatherData!!.forecast.forecastday.size.toString())
                        val adapter=WeatherAdapter(weatherData)
                        lista=adapter.GetIcons()

                        val todayimage=findViewById<ImageView>(R.id.todayimage)
                        runOnUiThread { Picasso.get().load(lista[0]).into(todayimage) }

                        val tdytemp=findViewById<TextView>(R.id.currenttemp)
                        tdytemp.text=weatherData.current.temp_c.toString()+"°"




                        if(weatherData.current.temp_c<-5.0){
                            runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                                val drawableResId = R.drawable.snowman
                                val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                                empic.setImageDrawable(drawable)}}

                        if(weatherData.current.temp_c<0.0 && weatherData.current.temp_c>=-5.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.snowflake
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}

                        if(weatherData.current.temp_c>=0.0 && weatherData.current.temp_c<=8.0){
                            runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                                val drawableResId = R.drawable.cold
                                val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                                empic.setImageDrawable(drawable)}}

                        if(weatherData.current.temp_c>8.0 && weatherData.current.temp_c<=20.0){
                            runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                                val drawableResId = R.drawable.temperature
                                val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                                empic.setImageDrawable(drawable)}}

                        if(weatherData.current.temp_c>20.0){
                            runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                                val drawableResId = R.drawable.hot
                                val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                                empic.setImageDrawable(drawable)}}







                        val feels=findViewById<TextView>(R.id.feelsliketext)
                        feels.text="Feels like: "+weatherData.current.feelslike_c.toString()+"°"

                        val windkph=findViewById<TextView>(R.id.currentwindkph)
                        windkph.text="Wind: "+weatherData.current.wind_kph.toString()+"kph"

                        val city=findViewById<TextView>(R.id.cityname)
                        city.text=weatherData.location.name

                        runOnUiThread{ val humtext=findViewById<TextView>(R.id.humiditytext)
                            humtext.text="Humidity: "+weatherData.current.humidity.toString()}

                        val todaytemp=findViewById<TextView>(R.id.day1temp)
                        val day2temp=findViewById<TextView>(R.id.day2temp)
                        val day3temp=findViewById<TextView>(R.id.day3temp)


                        fun getCurrentHourLegacy(): Int {
                            val calendar = Calendar.getInstance()
                            return calendar.get(Calendar.HOUR_OF_DAY)
                        }
                        val momenthour=getCurrentHourLegacy()


                        runOnUiThread {   todaytemp.text=weatherData.forecast.forecastday[0].day.avgtemp_c.toString()+"°"
                            day2temp.text=weatherData.forecast.forecastday[1].day.avgtemp_c.toString()+"°"
                            day3temp.text=weatherData.forecast.forecastday[2].day.avgtemp_c.toString()+"°"}


                        val urlhour1="https:${weatherData.forecast.forecastday[0].day.condition.icon}"
                        val urlhour2="https:${weatherData.forecast.forecastday[1].day.condition.icon}"
                        val urlhour3="https:${weatherData.forecast.forecastday[2].day.condition.icon}"

                        val todayforimage=findViewById<ImageView>(R.id.todayforimage)
                        val hour2image=findViewById<ImageView>(R.id.day2image)
                        val hour3image=findViewById<ImageView>(R.id.day3image)

                        runOnUiThread { Picasso.get().load(urlhour1).into(todayforimage)
                            Picasso.get().load(urlhour2).into(hour2image)
                            Picasso.get().load(urlhour3).into(hour3image)

                        }

                        val day1=findViewById<TextView>(R.id.day1)
                        val day2=findViewById<TextView>(R.id.day2)
                        val day3=findViewById<TextView>(R.id.day3)

                        runOnUiThread {  day1.text="${momenthour+1}:00"
                            day2.text="${momenthour+2}:00"
                            day3.text="${momenthour+3}:00"}



                        val weatherhourly:ArrayList<HourlyWeather>
                        weatherhourly=adapter.generateWeatherHourly()

                        runOnUiThread {  setRecyclerView(weatherhourly)}



                        val listofdates:MutableList<String>
                        listofdates=adapter.GetDaysDate()

                        runOnUiThread {   day1.text=getDayAbbreviation(listofdates[0])
                            day2.text=getDayAbbreviation(listofdates[1])
                            day3.text=getDayAbbreviation(listofdates[2])}


                    } else {

                        Log.i("ApiError","Api response not succesfful")
                    }
                }
            })





        }

        /*val slikaedita=ContextCompat.getDrawable(this,R.drawable.research)

        slikaedita?.setBounds(0, 0, 70, 70)
        editText.setCompoundDrawables(null, null, slikaedita, null)*/




        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val weatherData = parseJsonToWeatherData(responseBody.toString().trimIndent())
                    val lista:List<String>
                    Log.i("SIZE",weatherData!!.forecast.forecastday.size.toString())
                    val adapter=WeatherAdapter(weatherData)
                    lista=adapter.GetIcons()

                    val todayimage=findViewById<ImageView>(R.id.todayimage)
                    runOnUiThread { Picasso.get().load(lista[0]).into(todayimage) }

                    val tdytemp=findViewById<TextView>(R.id.currenttemp)
                    tdytemp.text=weatherData.current.temp_c.toString()+"°"

                    val feels=findViewById<TextView>(R.id.feelsliketext)
                    feels.text="Feels like: "+weatherData.current.feelslike_c.toString()+"°"

                    val windkph=findViewById<TextView>(R.id.currentwindkph)
                    windkph.text="Wind: "+weatherData.current.wind_kph.toString()+"kph"

                    val city=findViewById<TextView>(R.id.cityname)
                    city.text=weatherData.location.name

                    runOnUiThread{ val humtext=findViewById<TextView>(R.id.humiditytext)
                    humtext.text="Humidity: "+weatherData.current.humidity.toString()}

                    val todaytemp=findViewById<TextView>(R.id.day1temp)
                    val day2temp=findViewById<TextView>(R.id.day2temp)
                    val day3temp=findViewById<TextView>(R.id.day3temp)


                    fun getCurrentHourLegacy(): Int {
                        val calendar = Calendar.getInstance()
                        return calendar.get(Calendar.HOUR_OF_DAY)
                    }
                    val momenthour=getCurrentHourLegacy()


                   runOnUiThread {   todaytemp.text=weatherData.forecast.forecastday[0].day.avgtemp_c.toString()+"°"
                    day2temp.text=weatherData.forecast.forecastday[1].day.avgtemp_c.toString()+"°"
                    day3temp.text=weatherData.forecast.forecastday[2].day.avgtemp_c.toString()+"°"}


                    val urlhour1="https:${weatherData.forecast.forecastday[0].day.condition.icon}"
                    val urlhour2="https:${weatherData.forecast.forecastday[1].day.condition.icon}"
                    val urlhour3="https:${weatherData.forecast.forecastday[2].day.condition.icon}"

                    val todayforimage=findViewById<ImageView>(R.id.todayforimage)
                    val hour2image=findViewById<ImageView>(R.id.day2image)
                    val hour3image=findViewById<ImageView>(R.id.day3image)

                    runOnUiThread { Picasso.get().load(urlhour1).into(todayforimage)
                        Picasso.get().load(urlhour2).into(hour2image)
                        Picasso.get().load(urlhour3).into(hour3image)

                    }

                    val day1=findViewById<TextView>(R.id.day1)
                    val day2=findViewById<TextView>(R.id.day2)
                    val day3=findViewById<TextView>(R.id.day3)

                    runOnUiThread {  day1.text="${momenthour+1}:00"
                    day2.text="${momenthour+2}:00"
                    day3.text="${momenthour+3}:00"}


                    if(weatherData.current.temp_c<-5.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.snowman
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}

                    if(weatherData.current.temp_c<0.0 && weatherData.current.temp_c>=-5.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.snowflake
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}

                    if(weatherData.current.temp_c>=0.0 && weatherData.current.temp_c<=8.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.cold
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}

                    if(weatherData.current.temp_c>8.0 && weatherData.current.temp_c<=20.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.temperature
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}

                    if(weatherData.current.temp_c>20.0){
                        runOnUiThread {   val empic:ImageView=findViewById(R.id.emojipic)
                            val drawableResId = R.drawable.hot
                            val drawable: Drawable? = ContextCompat.getDrawable(empic.context, drawableResId)
                            empic.setImageDrawable(drawable)}}




                    val weatherhourly:ArrayList<HourlyWeather>
                    weatherhourly=adapter.generateWeatherHourly()

                    runOnUiThread {  setRecyclerView(weatherhourly)}



                    val listofdates:MutableList<String>
                    listofdates=adapter.GetDaysDate()

                   runOnUiThread {   day1.text=getDayAbbreviation(listofdates[0])
                    day2.text=getDayAbbreviation(listofdates[1])
                   day3.text=getDayAbbreviation(listofdates[2])}

                    val lupdate=adapter.getLastUpdate()
                    val lastupdate=findViewById<TextView>(R.id.lastupdatetext)

                    val inputDate=lupdate
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                    val outputFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale("hr", "HR"))

                    try {
                        val date = inputFormat.parse(inputDate)
                        val formattedDate = outputFormat.format(date!!).toString()
                        runOnUiThread {   lastupdate.text="Last update: "+formattedDate}
                    } catch (e: Exception) {

                    }



                } else {

                    Log.i("ApiError","Api response not succesfful")
                }


            }
        })



    }
    private fun getDayAbbreviation(inputString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
        val date = dateFormat.parse(inputString) ?: throw IllegalArgumentException("Invalid date format")

        val dayAbbreviationFormat = SimpleDateFormat("E", Locale("en"))
        return dayAbbreviationFormat.format(date)
    }
}
