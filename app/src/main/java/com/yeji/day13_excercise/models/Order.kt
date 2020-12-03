package com.yeji.day13_excercise.models

import java.io.Serializable

data class OrderResponse(
    val count: Int,
    val data: ArrayList<Order>,
    val error: Boolean
)

data class Order(
    val __v: Int? = null,
    val _id: String? = null,
    val date: String? = null,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val products: ArrayList<Product>,
    val shippingAddress: Address,
    val user: User,
    val userId: String
) :Serializable {
    companion object{
        const val KEY_ORDER = "order"
    }
}

data class OrderSummary(
    val _id: String,
    val deliveryCharges: Int,
    val discount: Double,
    val orderAmount: Int,
    val ourPrice: Double,
    val totalAmount: Int
):Serializable