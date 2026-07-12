package com.example.testp.network

import com.example.testp.Models.SkillforgeResponse

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getCourses(): Response<SkillforgeResponse>

}