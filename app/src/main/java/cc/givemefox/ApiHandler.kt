package cc.givemefox

import cc.givemefox.result.Err
import cc.givemefox.result.Ok
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double


) {
    override fun toString(): String {
        return "${speed}m/s ${deg}°, ${gust}m/s"
    }
}

data class Rain(
    val `1h`: Double
)

data class Snow(
    val `1h`: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain?,
    val snow: Snow?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

class ApiHandler {
    fun getWeather(town: String): cc.givemefox.result.Result<WeatherResponse, String> {
        if (town.isEmpty() || town.isBlank()) return Err("Town name cannot be empty")

        val apiKey = "API_KEY"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$town&appid=$apiKey&lang=pl&unit=metric"

        val client = OkHttpClient()
        val response = client.newCall(Request.Builder().url(url).build()).execute()

        when (response.code) {
            401 -> return Err("Invalid API key")
            404 -> return Err("City not found")
            !in (200..300) -> return Err("Failed to fetch weather data code: ${response.code}")
        }

        val result = response.body!!.string()
        val gson = Gson()

        return try {
            Ok(gson.fromJson(result, WeatherResponse::class.java))
        } catch (e: Exception) {
            Err("Failed to parse weather data")
        }
    }
}