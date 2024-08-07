package com.velshletter.atlasmonitor.presentation.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.velshletter.atlasmonitor.presentation.viewmodel.MainViewModel


@Composable
fun SecondScreen(
    mainViewModel: MainViewModel = viewModel()
) {

    val timeList by mainViewModel.selectedTime.collectAsState()

    BackHandler {
        mainViewModel.clear()
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
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.Center
                ) {
                    items(timeList.size) { index ->
                        Row(
                            modifier = Modifier
                                .size(280.dp, 40.dp)
                                .clickable {
                                    mainViewModel.updateTime(timeList.mapIndexed { j, item ->
                                        if (index == j) {
                                            item.copy(isSelected = !item.isSelected)
                                        } else item
                                    })
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = timeList[index].time,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(16.dp, 0.dp)
                            )
                            if (timeList[index].isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(end = 16.dp)
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
                    mainViewModel.startMonitor()
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
                    mainViewModel.stopService()
                }
            ) {
                Text("Остановить поиск", fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}