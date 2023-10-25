package ru.sokolov.flyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.sokolov.flyapp.dto.Flight
import ru.sokolov.flyapp.model.ListState
import ru.sokolov.flyapp.repository.AirTravelRepository
import javax.inject.Inject

@HiltViewModel
class AirTravelViewModel @Inject constructor(
    application: Application,
    private val repository: AirTravelRepository,
) : AndroidViewModel(application) {

    private var _data: List<Flight> = listOf()

    private var _dataState: ListState = ListState.Idle

    init {
        loadFlights()
    }

    val data: Flow<List<Flight>>
        get() = flow {
            while (true) {
                getData()
                emit(_data)
                delay(500)
            }
        }

    val dataState: Flow<ListState>
        get() = flow {
            while (true) {
                emit(_dataState)
                delay(500)
            }
        }

    fun loadFlights() = viewModelScope.launch {
        try {
            _dataState = ListState.Loading
            repository.getAll()
            _dataState = ListState.Idle
        } catch (e: Exception) {
            _dataState = ListState.Error
        }
    }

    private fun getData() = viewModelScope.launch {
        repository.data.collectLatest {
            _data = it
        }
    }

    fun like(flight: Flight) {
        repository.like(flight)
    }
}
