package com.velshletter.atlasmonitor.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.data.WebsiteRepositoryImpl
import com.example.domain.models.MonitoringData
import com.example.domain.models.ResponseState
import com.example.domain.models.TimeItem
import com.example.domain.models.TripInfo
import com.example.domain.models.UrlConverter
import com.example.domain.repository.SharedPrefRepository
import com.example.domain.usecase.GetTripInfoUseCase
import com.example.domain.usecase.StartMonitorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(
    private val startMonitorUseCase: com.example.domain.usecase.StartMonitorUseCase,
    private val sharedPrefRepositoryImpl: com.example.domain.repository.SharedPrefRepository
) : ViewModel() {

    private val getTripInfoUseCase =
        com.example.domain.usecase.GetTripInfoUseCase(com.example.data.data.WebsiteRepositoryImpl())
    var timeList: List<String> = listOf()
    private var url = ""

    private var _showToast = MutableStateFlow<String?>(null)
    val showToast: StateFlow<String?> get() = _showToast

//    private var _cityFromFlow = MutableStateFlow("")
    private var _cityFromFlow = MutableStateFlow("Молодечно")
    val cityFromFlow: StateFlow<String> get() = _cityFromFlow

//    private var _cityToFlow = MutableStateFlow("")
    private var _cityToFlow = MutableStateFlow("Минск")
    val cityToFlow: StateFlow<String> get() = _cityToFlow

    private var _dateFlow = MutableStateFlow(formatDate(LocalDate.now()))
    private val dateFlow: StateFlow<String> get() = _dateFlow

    private var _responseState = MutableStateFlow<com.example.domain.models.ResponseState>(com.example.domain.models.ResponseState.Waiting)
    val responseState: StateFlow<com.example.domain.models.ResponseState> get() = _responseState

    private var _selectedTime: MutableStateFlow<List<com.example.domain.models.TimeItem>> = MutableStateFlow(listOf())
    val selectedTime: StateFlow<List<com.example.domain.models.TimeItem>> get() = _selectedTime

    fun findTrip() {
        viewModelScope.launch {
            if (cityFromFlow.value.trim().isEmpty() || cityToFlow.value.trim().isEmpty() || dateFlow.value.isEmpty()) {
                _responseState.value = com.example.domain.models.ResponseState.Error("Заполните все поля")
                return@launch
            }
            val tripInfo = com.example.domain.models.TripInfo(
                cityFromFlow.value.trim(),
                cityToFlow.value.trim(),
                dateFlow.value
            )
            sharedPrefRepositoryImpl.saveTripInfo(tripInfo)
            url = com.example.domain.models.UrlConverter(tripInfo).getUrl()
            _responseState.value = com.example.domain.models.ResponseState.Loading
            timeList = getTripInfoUseCase.execute(url = url)
            if (timeList.isNotEmpty()) {
                _selectedTime.value = timeList.map {
                    com.example.domain.models.TimeItem(time = it, isSelected = false)
                }
                Log.d("MyLog", timeList.toString() + timeList.size)
                _responseState.value = com.example.domain.models.ResponseState.Success
            } else {
                _responseState.value =
                    com.example.domain.models.ResponseState.Error("Проверьте введенные данные и/или подключение к интернету")
            }
        }
    }

    fun startMonitor(){
        if (selectedTime.value.all { !it.isSelected }) {
            _showToast.value = "Укажите время"
        } else {
            sharedPrefRepositoryImpl.saveMonitoringData(
                com.example.domain.models.MonitoringData(
                    url,
                    timeList
                )
            )
            startMonitorUseCase.execute(url = url, timeList = selectedTime.value)
        }
    }

    fun loadSavedData(){
        val monitoringData = sharedPrefRepositoryImpl.getMonitorData()
        url = monitoringData.url
        timeList = monitoringData.timeList
        _selectedTime.value = timeList.map {
            com.example.domain.models.TimeItem(time = it, isSelected = false)
        }
        _responseState.value = com.example.domain.models.ResponseState.Success
    }

    fun clear() {
        _cityFromFlow.value = ""
        _cityToFlow.value = ""
        _dateFlow.value = formatDate(LocalDate.now())
        timeList = listOf()
        _responseState.value = com.example.domain.models.ResponseState.Waiting
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

    fun updateResponseState(state: com.example.domain.models.ResponseState) {
        _responseState.value = state
    }

    fun updateTime(items: List<com.example.domain.models.TimeItem>) {
        _selectedTime.value = items
    }

    private fun formatDate(date: LocalDate): String {
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .format(date)
    }

    fun clearToastMessage(){
        _showToast.value = null
    }

}