package com.example.atlasmonitor.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atlasmonitor.UrlData
import org.jsoup.Jsoup
import java.io.IOException


val timeArray = mutableListOf<String>()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen" ){
        composable("main_screen"){ MainView(navController)}
        composable("sec_screen"){ SecView(timeArray) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun MainView(navController: NavController = rememberNavController()) {
    val valueFrom = remember { mutableStateOf(TextFieldValue()) }
    val valueTo = remember { mutableStateOf(TextFieldValue()) }
    val valueDate = remember { mutableStateOf(TextFieldValue()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
                ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedCard(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color.Black),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    TextField(
                        value = valueFrom.value,
                        onValueChange = { valueFrom.value = it },
                        placeholder = { Text(text = "Откуда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
                        value = valueTo.value,
                        onValueChange = { valueTo.value = it },
                        placeholder = { Text(text = "Куда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
                        value = valueDate.value,
                        onValueChange = { valueDate.value = it },
                        placeholder = { Text("Дата", fontFamily = FontFamily.Monospace) },
                    )
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .size(280.dp, 56.dp)
                    .offset(0.dp, 5.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color.Black),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 2.dp
                ),
                onClick = {
                    val urlComp = UrlData(valueFrom.value.toString(), valueTo.value.toString(), valueDate.value.toString())
                    connectUrl(urlComp)
                    Thread.sleep(3000)
                    navController.navigate("sec_screen")
                }
            ) {
                Text("Далее", fontSize = 20.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}
fun connectUrl(urlComp: UrlData){
    val runnable2 = Runnable {
        try {

            val urlFPart: String = "https://atlasbus.by/Маршруты/"
            val urlSecP = "?date=2023-"
//    val websiteUrl = "https://atlasbus.by/Маршруты/" + urlComp.from + "/" + urlComp.to + "?date=2023-" + urlComp.date
            val websiteUrl = "https://atlasbus.by/Маршруты/Минск/Ивье?date=2023-09-25"
            val doc = Jsoup.connect(websiteUrl).get()
            val time = doc.select("div.MuiGrid-grid-md-3.MuiGrid-item.MuiGrid-root:nth-of-type(1)")


            val info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")

            var i = 0; var g = 0; var j = 1
            while (i < info.size) {
                val stTime = time[j].allElements
                timeArray.add(stTime.get(3).text())
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

