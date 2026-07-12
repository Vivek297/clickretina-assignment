package com.example.testp.Models

import com.google.gson.annotations.SerializedName

data class Category(

    val courseCount: Int,
    val courses: List<Course>,
    val description: String,
    val iconColor: String,
    val id: String,
    val name: String
)