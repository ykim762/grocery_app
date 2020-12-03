package com.yeji.day13_excercise.models

import java.io.Serializable

data class ProductResponse(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
):Serializable

data class Product(
    val _id: String,
    val catId: Int? = null,
    val description: String? = null,
    val image: String? = null,
    val mrp: Double,   // mrp should be double not int
    val position: Int? = null,
    val price: Double,
    val productName: String,
    var quantity: Int = 0,
    val status: Boolean ? = null,
    val subId: Int? = null,
    val unit: String ? = null
):Serializable{
    companion object{
        const val KEY_PRODUCT = "product"
    }
}