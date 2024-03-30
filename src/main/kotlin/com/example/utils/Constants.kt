package com.example.utils

object Constants {

    private const val API_VERSION = "v1"

    //for authentication
    const val ROUTE_TYPE = "auth"
    const val REGISTER_END_POINT = "$API_VERSION/$ROUTE_TYPE/register"
    const val LOGIN_END_POINT = "$API_VERSION/$ROUTE_TYPE/login"

    //for posts crud
    const val POST_TYPE = "post"
    const val CREATE_END_POINT = "$API_VERSION/$POST_TYPE/create"
    const val RETRIEVE_END_POINT = "$API_VERSION/$POST_TYPE/get"
    const val DELETE_END_POINT = "$API_VERSION/$POST_TYPE/delete"
}