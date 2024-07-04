package com.velshletter.atlasmonitor.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.velshletter.atlasmonitor.domain.models.TimeItem

@Preview(showBackground = true)
@Composable
fun SecondScreen(
    mainViewModel: MainViewModel = viewModel(),
) {
    val timeArray: List<String> = mainViewModel.timeArray
    BackHandler {
        mainViewModel.clear()
    }
    var items by remember {
        mutableStateOf(timeArray.map {
            TimeItem(time = it, isSelected = false)
        })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.heightIn(0.dp, 280.dp),
                shape = RoundedCornerShape(20.dp),
//                    elevation = CardDefaults.cardElevation(5.dp)
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
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = Color.Blue,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .size(280.dp, 56.dp)
                    .offset(0.dp, 5.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue),
                shape = RoundedCornerShape(20.dp),
                onClick = {
//                    Intent(context,MyService::class.java).also {
//                        it.action = MyService.Actions.START.toString()
//                        context.startService(it)
//                    }
//
//                    Log.d("MyLog","")
//                    SiteConnect().startMonitor(urlData.getUrl(), items, context)
                },

                ) {
                Text("Начать поиск", fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            }
            Button(
                modifier = Modifier
                    .size(280.dp, 56.dp)
                    .offset(0.dp, 8.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue),
                shape = RoundedCornerShape(20.dp),
                onClick = {
//                    Intent(context,MyService::class.java).also {
//                        it.action = MyService.Actions.STOP.toString()
//                        context.startService(it)
//                    }
                }
            ) {
                Text("Остановить поиск", fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}