package com.example.testp.repository

import com.example.testp.network.ApiService
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getCourses() =
        apiService.getCourses()
}