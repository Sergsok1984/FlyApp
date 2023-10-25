package ru.sokolov.flyapp.api

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

    class ApiWbTest @Inject constructor(
    private val client: OkHttpClient,
) {
    @Inject
    fun getListData(startLocationCode: String = "LED"): String {
        val startLocationCodeRequest = "{\"startLocationCode\":\"$startLocationCode\"}"
        val body =
            startLocationCodeRequest.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(ApiModule.BASE_URL)
            .header("Accept", "application/json, text/plain, /")
            .header("Content-Type", "application/json")
            .post(body)
            .build()
        try {
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        } catch (e: Exception) {
            throw RuntimeException()
        }
    }
}
