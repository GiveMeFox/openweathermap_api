package cc.givemefox

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.givemefox.databinding.ActivityMainBinding
import cc.givemefox.result.Err
import cc.givemefox.result.Ok
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ah = ApiHandler()

        binding.buttonGetWeather.setOnClickListener {
            val input = binding.townInput.text.toString()

            when (val result = ah.getWeather(input)) {
                is Ok -> {
                    val response = result.value

                    val url = URL("https://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png")
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    binding.image.setImageBitmap(bmp)

                    binding.main.text = "Main:"
                    binding.mainTemp.text = "   Temp: ${response.main.temp}°C"
                    binding.mainFeelsLike.text = "   Feels like: ${response.main.feels_like}°C"
                    binding.mainTempMin.text = "   Temp min: ${response.main.temp_min}°C"
                    binding.mainTempMax.text = "   Temp max: ${response.main.temp_max}°C"
                    binding.mainPressure.text = "   Pressure: ${response.main.pressure}hPa"
                    binding.mainHumidity.text = "   Humidity: ${response.main.humidity}%"
                    binding.mainSeaLevel.text = "   Sea level: ${response.main.sea_level}"
                    binding.mainGrndLevel.text = "   Grnd level: ${response.main.grnd_level}"

                    binding.visibility.text = "Visibility: ${response.visibility}"
                    binding.wind.text = "Wind: ${response.wind}"
                    binding.rain.text = "Rain: ${response.rain ?: "None"}"
                    binding.snow.text = "Snow: ${response.snow ?: "None"}"
                    binding.clouds.text = "Clouds: ${response.clouds.all}%"
                    binding.name.text = "Name: ${response.name}"

                }
                is Err -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}