package com.yeji.day13_excercise.models

import java.io.Serializable

data class Address(
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: String,
    val streetName: String,
    val type: String,
    val userId: String?
):Serializable{
    companion object{
        const val KEY_ADDRESS = "addressKey"
    }
}