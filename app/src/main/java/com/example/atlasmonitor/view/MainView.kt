package com.example.atlasmonitor.view

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atlasmonitor.TimeItem
import com.example.atlasmonitor.UrlData
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import org.jsoup.Jsoup
import java.io.IOException


val timeArray = mutableListOf<String>()
var urlData: UrlData = UrlData()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen" ){
        composable("main_screen"){ MainView(navController) }
        composable("load_screen"){ LoadingAnimation3() }
        composable("sec_screen"){ SecView(timeArray) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(
    navController: NavController = rememberNavController()
) {
    val valueFrom = remember { mutableStateOf(TextFieldValue()) }
    val valueTo = remember { mutableStateOf(TextFieldValue()) }
    val valueDate = remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
                ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                //elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextField(
//                        modifier = Modifier.background(Color.White).border(2.dp, Color.White),
                        value = valueFrom.value,
                        onValueChange = { valueFrom.value = it },
                        placeholder = { Text(text = "Откуда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
//                        modifier = Modifier.background(Color.White).border(2.dp, Color.White),
                        value = valueTo.value,
                        onValueChange = { valueTo.value = it },
                        placeholder = { Text(text = "Куда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
//                        modifier = Modifier.background(Color.White).border(2.dp, Color.White),
                        value = valueDate.value,
                        onValueChange = { valueDate.value = it },
                        placeholder = { Text("Дата", fontFamily = FontFamily.Monospace) },
                    )
                }
            }
            Button(
                modifier = Modifier
                    .size(280.dp, 56.dp)
                    .offset(0.dp, 5.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    urlData.setUrl(valueFrom.value.text, valueTo.value.text, valueDate.value.text)
                    //LoadingAnimation3(Color.Blue, 30.dp, 400, 0.3f)
                    //navController.navigate("load_screen")
                    connectUrl()
                    navController.navigate("sec_screen")

                }
            ) {
                Text("Далее", fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
@Preview
fun LoadingAnimation3(
    circleColor: Color = Color(0xFF002AFF),
    circleSize: Dp = 18.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {
    val circles = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ожидание подключения к интернету",
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier
            //.border(width = 2.dp, color = Color.Magenta)
        ) {
            circles.forEachIndexed { index, animatable ->

                // gap between the circles
                if (index != 0) {
                    Spacer(modifier = Modifier.width(width = 6.dp))
                }

                Box(
                    modifier = Modifier
                        .size(size = circleSize)
                        .clip(shape = CircleShape)
                        .background(
                            color = circleColor
                                .copy(alpha = animatable.value)
                        )
                ) {
                }
            }
        }
    }
}

fun connectUrl(){
    Log.d("MyLog", urlData.getUrl())
    val runnable2 = Runnable {
        try {
                     val url = "https://atlasbus.by/Маршруты/Минск/Ивье?date=2023-10-04"
            val doc = Jsoup.connect(url).get()
//            val doc = Jsoup.connect(urlData.getUrl()).get()
            val time = doc.select("div.MuiGrid-grid-md-3.MuiGrid-item.MuiGrid-root:nth-of-type(1)")
            val info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")

            var i = 0; var g = 0; var j = 1
            while (i < info.size) {
                val stTime = time[j].allElements
                timeArray.add(stTime[3].text())
                g++; i++; j += 2
            }
            for (n in timeArray ){
                Log.d("MyLog", n)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
    val thread2 = Thread(runnable2)
    thread2.start()
}

