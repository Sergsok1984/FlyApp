package ru.sokolov.flyapp.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolov.flyapp.dto.Flight

interface AirTravelRepository {

    val data: Flow<List<Flight>>

    suspend fun getAll()

    fun like(flight: Flight)

}
