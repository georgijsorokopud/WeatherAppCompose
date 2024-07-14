package com.gosha.weatherappcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gosha.weatherappcompose.ui.theme.WeatherAppComposeTheme
import org.json.JSONObject


const val API_KEY = "00f97d8d3d2b4b9bb08131717241306"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Belgorod", this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, context: Context) {
    // Создание состояния для хранения температуры
    val state = remember {
        mutableStateOf("Unknown")
    }

    // Колонка для размещения элементов в вертикальном порядке
    Column(modifier = Modifier.fillMaxSize()) {
        // Блок для отображения текущей температуры
        Box(modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Текст с отображением температуры
            Text(text = "Temp in $name = ${state.value}°C")
        }

        // Блок для размещения кнопки "Refresh"
        Box(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter){
            Button(onClick = {
                // Вызов функции getResult при нажатии на кнопку
                getResult(name, state, context)
            },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()) {
                // Текст на кнопке
                Text(text = "Refresh")
            }
        }
    }
}

// Функция для получения результатов с API погоды
private fun getResult(city: String, state: MutableState<String>, context: Context){
    // Формирование URL для запроса
    val url = "https://api.weatherapi.com/v1/forecast.json" +
            "?key=$API_KEY&" +
            "q=$city&" +
            "days=3&" +
            "aqi=no&" +
            "alerts=no"

    // Создание очереди запросов
    val queue = Volley.newRequestQueue(context)
    // Создание строкового запроса
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            // Обработка успешного ответа
            val obj = JSONObject(response)
            // Извлечение температуры из ответа и обновление состояния
            state.value = obj.getJSONObject("current").getString("temp_c")
        },
        { error ->
            // Обработка ошибки
            Log.d("MyTag", "Error: $error")
        }
    )
    // Добавление запроса в очередь
    queue.add(stringRequest)
}
