package com.example.utils

object Constants {

    private const val API_VERSION = "v1"

    //for authentication
    private const val ROUTE_TYPE = "auth"
    const val REGISTER_END_POINT = "$API_VERSION/$ROUTE_TYPE/register"
    const val LOGIN_END_POINT = "$API_VERSION/$ROUTE_TYPE/login"
    const val GET_AUTHENTICATED = "$API_VERSION/$ROUTE_TYPE/authenticate"
    const val GET_SECRET = "$API_VERSION/$ROUTE_TYPE/secret"


    //for posts crud
    private const val POST_TYPE = "post"
    const val CREATE_END_POINT = "$API_VERSION/$POST_TYPE/create"
    const val RETRIEVE_END_POINT = "$API_VERSION/$POST_TYPE/get"
    const val DELETE_END_POINT = "$API_VERSION/$POST_TYPE/delete/{id}"

    //for club admin
    private const val LOGIN_TYPE = "club"
    const val LOGIN_CLUB_ADMIN = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/login"
    const val GET_AUTHENTICATED_CLUB = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/authenticate"
    const val GET_SECRET_CLUB = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/secret"

    //college Admin
    private const val AUTH_TYPE = "admin"
    const val CREATE_CLUB = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/create/club"


}