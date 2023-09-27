package com.example.atlasmonitor.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atlasmonitor.MyService
import com.example.atlasmonitor.TimeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecView(
    timeArray: List<String> = listOf("sdfsfsdf", "sdfsdf","sdfsf"),
) {
    val context: Context = LocalContext.current
    var items by remember {
        mutableStateOf(
            timeArray.map {
                TimeItem(
                    time = it,
                    isSelected = false
                )
            }
        )
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isSelected by remember {
        mutableStateOf("")
    }
    var isChecked by remember {
        mutableStateOf(false)
    }
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
                    LazyColumn(
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(items.size) { index ->
                            Row(
                                modifier = Modifier
                                    .size(280.dp, 40.dp)
                                    .clickable {
                                        items = items.mapIndexed { j, item ->
                                            if (index == j) {
                                                item.copy(isSelected = !item.isSelected)
                                            } else item
                                        }
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = items[index].time,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(10.dp, 0.dp)
                                )
                                if (items[index].isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "Selected",
                                        tint = Color.Green,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 10.dp)
                                    )
                                }
                            }
                        }
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
                        Intent(context,MyService::class.java).also {
                            it.action = MyService.Actions.START.toString()
                            context.startService(it)
                        }
                        /*
                        for (i in items){
                            Log.d("MyLog",i.time + i.isSelected.toString())
                        }
                        Log.d("MyLog", "----------------------")
                         */
                    }
                ) {
                    Text("Начать поиск", fontSize = 20.sp, fontFamily = FontFamily.Monospace)
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
                    Intent(context,MyService::class.java).also {
                        it.action = MyService.Actions.STOP.toString()
                        context.startService(it)
                    }
                }
            ) {
                Text("Остановить поиск", fontSize = 20.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}


