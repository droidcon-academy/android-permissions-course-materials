package com.droidcon.weatherstation.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.weatherstation.api.networkresponse.NetworkResponse
import com.droidcon.weatherstation.network.Resource
import com.droidcon.weatherstation.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository
) : ViewModel() {
    // Home UI State
    private val _uiState = mutableStateOf<Resource<HomeUiState>>(Resource.Loading)

    // Backing property to avoid state updates from other classes
    val uiState: State<Resource<HomeUiState>>
        get() = _uiState

    init {
        val latitude = savedStateHandle.get<Float>("latitude")
        val longitude = savedStateHandle.get<Float>("longitude")
        if (longitude != null && latitude != null) {
            getCurrentWeather(latitude, longitude)
        }
    }

    private fun getCurrentWeather(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(
                latitude,
                longitude
            )
            when (response) {
                is NetworkResponse.Success -> {
                    _uiState.value = Resource.Success(HomeUiState(response.body))
                }
                is NetworkResponse.NetworkError -> {
                    _uiState.value = Resource.Error("Please check your internet connection")
                }
                else -> {
                    _uiState.value = Resource.Error("Something went wrong")
                }
            }
        }
    }

}