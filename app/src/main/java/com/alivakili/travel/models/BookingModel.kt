package com.alivakili.travel.models

import java.io.Serializable

data class BookingModel(
    val time: String,
    val date: String,
    val noOfPassangers: String,
    val sourceCity: String,
    val destinationCity: String,
    val daysDuration: String
) : Serializable
