package com.velshletter.atlasmonitor.presentation.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.velshletter.atlasmonitor.presentation.viewmodels.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun MainView(
    mainViewModel: MainViewModel = viewModel(),
){
    val valueFrom = mainViewModel.cityFromFlow.collectAsState()
    val valueTo = mainViewModel.cityToFlow.collectAsState()

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDateView by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMMM")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    MaterialTheme {
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton("Oк")
                negativeButton("Отмена")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now()
            ) {
                pickedDate = it
                mainViewModel.updateDate(pickedDate)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextField(
                        value = valueFrom.value,
                        onValueChange = { mainViewModel.updateCityFrom(it) },
                        placeholder = { Text(text = "Откуда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
                        value = valueTo.value,
                        onValueChange = { mainViewModel.updateCityTo(it) },
                        placeholder = { Text(text = "Куда", fontFamily = FontFamily.Monospace) },
                    )
                    TextField(
                        readOnly = true,
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            dateDialogState.show()
                                        }
                                    }
                                }
                            },
                        value = formattedDateView,
                        onValueChange = { },
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
                    mainViewModel.findTrip()
                }
            ) {
                Text("Далее", fontSize = 15.sp, fontFamily = FontFamily.Monospace)
            }

        }
    }


}