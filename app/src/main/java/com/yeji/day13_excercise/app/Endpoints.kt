package com.yeji.day13_excercise.app

// url concat 할
class Endpoints {

    companion object{
        private val URL_CATEGORY = "category"
        private val URL_SUBCATEGORY = "subcategory/"
        private val URL_PRODUCT_BY_SUB_ID = "products/sub/"
        private val URL_REGISTER = "auth/register"
        private val URL_LOGIN = "auth/login"
        private val URL_ADDRESS = "address"
        private val URL_ORDER = "orders"
        private val URL_BAR = "/"

        fun getCategory():String {
            return "${Config.BASE_URL + URL_CATEGORY}"
        }

        fun getSubCategoryByCatId(catId:Int) :String{
            return "${Config.BASE_URL + URL_SUBCATEGORY + catId}"
        }

        fun getProductBySubID(subId:Int) :String{
            return "${Config.BASE_URL + URL_PRODUCT_BY_SUB_ID + subId}"
        }

        fun getRegister() :String{
            return "${Config.BASE_URL + URL_REGISTER}"
        }

        fun getLogin() :String{
            return "${Config.BASE_URL + URL_LOGIN}"
        }

        fun getAddress():String{
            return "${Config.BASE_URL + URL_ADDRESS}"
        }

        fun postOrders():String{
            return "${Config.BASE_URL + URL_ORDER}"
        }

        fun getOrdersByUserID(userId: String):String{
            return "${Config.BASE_URL + URL_ORDER + URL_BAR + userId}"
        }

    }
}