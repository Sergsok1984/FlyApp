package ru.sokolov.flyapp.repository

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.sokolov.flyapp.api.ApiWbTest
import ru.sokolov.flyapp.dto.Flight
import ru.sokolov.flyapp.dto.FlightResponse
import java.net.SocketTimeoutException
import javax.inject.Inject

class AirTravelRepositoryImpl @Inject constructor(
    private val apiWbTest: ApiWbTest
) : AirTravelRepository {

    private var _data: List<Flight> = listOf()

    private val gson = Gson()

    override val data: Flow<List<Flight>>
        get() = flow {
            while (true) {
                emit(_data)
                delay(500)
            }
        }

    override suspend fun getAll() {
        withContext(Dispatchers.IO) {
            try {
                val responseJson = apiWbTest.getListData()
                val responseObject: FlightResponse =
                    gson.fromJson(responseJson, FlightResponse::class.java)
                _data = responseObject.flights
            } catch (e: SocketTimeoutException) {
                throw Exception()
            }
        }
    }

    override fun like(flight: Flight) {
        _data = _data.map {
            if (it.searchToken == flight.searchToken) flight.copy(likedByMe = !flight.likedByMe)
            else it
        }
    }
}
