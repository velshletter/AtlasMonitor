package com.velshletter.atlasmonitor.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velshletter.atlasmonitor.data.WebsiteRepositoryImpl
import com.velshletter.atlasmonitor.domain.models.ResponseState
import com.velshletter.atlasmonitor.domain.models.Url
import com.velshletter.atlasmonitor.domain.usecase.GetTripInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val getTripInfoUseCase = GetTripInfoUseCase(WebsiteRepositoryImpl())
    var timeArray: List<String> = listOf()

//    private var _cityFromFlow = MutableStateFlow("")

    private var _cityFromFlow = MutableStateFlow("Минск")
    val cityFromFlow: StateFlow<String> = _cityFromFlow

//    private var _cityToFlow = MutableStateFlow("")

    private var _cityToFlow = MutableStateFlow("Ивье")
    val cityToFlow: StateFlow<String> = _cityToFlow

    private var _dateFlow = MutableStateFlow(formatDate(LocalDate.now()))
    private val dateFlow: StateFlow<String> = _dateFlow

    private var _responseState = MutableStateFlow<ResponseState>(ResponseState.Waiting)
    val responseState: StateFlow<ResponseState> = _responseState

    fun findTrip() {
        viewModelScope.launch {
            if (cityFromFlow.value.trim().isEmpty() || cityToFlow.value.trim().isEmpty() || dateFlow.value.isEmpty()) {
                _responseState.value = ResponseState.Error("Заполните все поля")
                return@launch
            }
            val url = Url(cityFromFlow.value.trim(), cityToFlow.value.trim(), dateFlow.value).get()
            _responseState.value = ResponseState.Loading
            timeArray = getTripInfoUseCase.execute(url = url)
            if (timeArray.isNotEmpty()) {
                _responseState.value = ResponseState.Success
                Log.d("MyLog", timeArray.toString() + timeArray.size)
            } else {
                _responseState.value =
                    ResponseState.Error("Проверьте введенные данные и/или подключение к интернету")
            }
        }
    }

    fun clear() {
        _cityFromFlow.value = ""
        _cityToFlow.value = ""
        _dateFlow.value = formatDate(LocalDate.now())
        timeArray = listOf()
        _responseState.value = ResponseState.Waiting
    }

    fun updateCityFrom(city: String) {
        _cityFromFlow.value = city
    }

    fun updateCityTo(city: String) {
        _cityToFlow.value = city
    }

    fun updateDate(date: LocalDate) {
        _dateFlow.value = formatDate(date)
    }

    fun updateResponseState(state: ResponseState) {
        _responseState.value = state
    }

    private fun formatDate(date: LocalDate): String {
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .format(date)
    }

}