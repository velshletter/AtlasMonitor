package com.velshletter.atlasmonitor.presentation.views


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.velshletter.atlasmonitor.R
import com.velshletter.atlasmonitor.presentation.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun MainView(
    mainViewModel: MainViewModel = viewModel(),
) {
    val valueFrom = mainViewModel.cityFromFlow.collectAsState()
    val valueTo = mainViewModel.cityToFlow.collectAsState()
    val datePickerState = rememberMaterialDialogState()
    var pickedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    val formattedDateView by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMMM")
                .format(pickedDate)
        }
    }

    MaterialDialog(
        dialogState = datePickerState,
        backgroundColor = MaterialTheme.colorScheme.surface,
        buttons = {
            positiveButton(
                text = "Ок",
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
            negativeButton(
                text = "Отмена",
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
        },
    ) {
        datepicker(
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            initialDate = LocalDate.now(),
        ) {
            pickedDate = it
            mainViewModel.updateDate(pickedDate)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.7f),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextField(
                        value = valueFrom.value,
                        onValueChange = { mainViewModel.updateCityFrom(it) },
                        singleLine = true,
                        placeholder = { Text(text = "Откуда") },
                    )
                    TextField(
                        value = valueTo.value,
                        singleLine = true,
                        onValueChange = { mainViewModel.updateCityTo(it) },
                        placeholder = { Text(text = "Куда") },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.round_compare_arrows_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        mainViewModel.swapRoutes()
                                    },
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                    TextField(
                        readOnly = true,
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            datePickerState.show()
                                        }
                                    }
                                }
                            },
                        value = formattedDateView,
                        onValueChange = { },
                        placeholder = { Text("Дата") },
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                modifier = Modifier
                    .size(280.dp, 56.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    mainViewModel.findTrip()
                }
            ) {
                Text("Далее", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Предыдущий поиск:",
                fontSize = 12.sp
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        mainViewModel.setLastSearchInfo()
                    },
                text = mainViewModel.loadLastSearch(),
                fontSize = 14.sp
            )
        }
    }


}