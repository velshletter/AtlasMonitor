package com.velshletter.atlasmonitor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.MonitoringData
import com.example.domain.models.ResponseState
import com.example.domain.models.TimeItem
import com.example.domain.models.TripInfo
import com.example.domain.models.UrlConverter
import com.example.domain.repository.ServiceManager
import com.example.domain.repository.ServiceStateChecker
import com.example.domain.repository.SharedPrefRepository
import com.example.domain.usecase.GetTripInfoUseCase
import com.example.domain.usecase.StartMonitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val startMonitorUseCase: StartMonitorUseCase,
    private val getTripInfoUseCase: GetTripInfoUseCase,
    private val sharedPrefRepository: SharedPrefRepository,
    private val serviceManager: ServiceManager,
    private val serviceStateChecker: ServiceStateChecker,
) : ViewModel() {

    private var timeList: List<String> = listOf()
    private var url = ""

    private var _showToast = MutableStateFlow<String?>(null)
    val showToast: StateFlow<String?> get() = _showToast

    private var _cityFromFlow = MutableStateFlow("")
    val cityFromFlow: StateFlow<String> get() = _cityFromFlow

    private var _cityToFlow = MutableStateFlow("")
    val cityToFlow: StateFlow<String> get() = _cityToFlow

    private var _dateFlow = MutableStateFlow(formatDate(LocalDate.now()))
    private val dateFlow: StateFlow<String> get() = _dateFlow

    private var _responseState = MutableStateFlow<ResponseState>(ResponseState.Waiting)
    val responseState: StateFlow<ResponseState> get() = _responseState

    private var _selectedTime: MutableStateFlow<List<TimeItem>> = MutableStateFlow(listOf())
    val selectedTime: StateFlow<List<TimeItem>> get() = _selectedTime

    fun findTrip() {
        viewModelScope.launch {
            if (cityFromFlow.value.trim().isEmpty() || cityToFlow.value.trim()
                    .isEmpty() || dateFlow.value.isEmpty()
            ) {
                _responseState.value = ResponseState.Error("Заполните все поля")
                return@launch
            }
            val tripInfo = TripInfo(
                cityFromFlow.value.trim(),
                cityToFlow.value.trim(),
                dateFlow.value
            )
            url = UrlConverter(tripInfo).getUrl()
            _responseState.value = ResponseState.Loading
            timeList = getTripInfoUseCase.execute(url = url)
            if (timeList.isNotEmpty()) {
                sharedPrefRepository.saveTripInfo(tripInfo)
                _selectedTime.value = timeList.map {
                    TimeItem(time = it, isSelected = false)
                }
                Log.d("MyLog", timeList.toString() + timeList.size)
                _responseState.value = ResponseState.Success
            } else {
                _responseState.value =
                    ResponseState.Error("Проверьте введенные данные и/или подключение к интернету")
            }
        }
    }

    fun startMonitor() {
        if (selectedTime.value.all { !it.isSelected }) {
            _showToast.value = "Укажите время"
        } else {
            sharedPrefRepository.saveMonitoringData(
                MonitoringData(url, timeList)
            )

            startMonitorUseCase.execute(url = url, timeList = selectedTime.value)
        }
    }

    fun isMonitoring() {
        if (!serviceStateChecker.isServiceRunning()) {
            return
        } else {
            val monitoringData = sharedPrefRepository.getMonitorData()
            url = monitoringData.url
            timeList = monitoringData.timeList
            _selectedTime.value = timeList.map {
                TimeItem(time = it, isSelected = false)
            }
            _responseState.value = ResponseState.Success
        }
    }

    fun stopService() {
        serviceManager.stopService()
    }

    fun setLastSearchInfo() {
        val lastTripInfo = sharedPrefRepository.getTripInfo()
        _cityFromFlow.value = lastTripInfo.from
        _cityToFlow.value = lastTripInfo.to
    }

    fun swapRoutes() {
        val from = cityFromFlow.value
        _cityFromFlow.value = cityToFlow.value
        _cityToFlow.value = from
    }

    fun loadLastSearch(): String {
        val lastTripInfo = sharedPrefRepository.getTripInfo()
        return if (lastTripInfo.from.isNotEmpty() && lastTripInfo.to.isNotEmpty()) {
            "${lastTripInfo.from} - ${lastTripInfo.to}"
        } else ""
    }

    fun clear() {
        _cityFromFlow.value = ""
        _cityToFlow.value = ""
        _dateFlow.value = formatDate(LocalDate.now())
        timeList = listOf()
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

    fun updateTime(items: List<TimeItem>) {
        _selectedTime.value = items
    }

    private fun formatDate(date: LocalDate): String {
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .format(date)
    }

    fun clearToastMessage() {
        _showToast.value = null
    }


}