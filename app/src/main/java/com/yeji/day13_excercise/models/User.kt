package com.yeji.day13_excercise.models

import java.io.Serializable

data class User(
    var id:String?,
    var name:String? = null,
    var email:String?,
    var password:String?,
    var mobile:String? = null
):Serializable