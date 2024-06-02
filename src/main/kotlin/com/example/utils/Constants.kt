package com.example.utils

object Constants {

    private const val API_VERSION = "v1"
    private const val USER = "user"

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
    const val RETRIEVE_END_POINT_USER = "$API_VERSION/$USER/$POST_TYPE/get"

    const val SEARCH_POST = "$API_VERSION/$POST_TYPE/search/user"
    const val SEARCH_POST_CLUB = "$API_VERSION/$POST_TYPE/search/club"
    const val DELETE_END_POINT = "$API_VERSION/$POST_TYPE/delete/{id}"

    //for club admin
    private const val LOGIN_TYPE = "club"
    const val LOGIN_CLUB_ADMIN = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/login"
    const val GET_AUTHENTICATED_CLUB = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/authenticate"
    const val GET_SECRET_CLUB = "$API_VERSION/$ROUTE_TYPE/$LOGIN_TYPE/secret"
    const val GET_CLUB_DATA = "$API_VERSION/$LOGIN_TYPE/get"


    //college Admin

    private const val AUTH_TYPE = "admin"
    const val CREATE_CLUB = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/create/club"
    const val CREATE_ADMIN = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/create"
    const val ADMIN_LOGIN = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/login"
    const val GET_AUTHENTICATE_ADMIN = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/authenticate"
    const val GET_SECRET_ADMIN = "$API_VERSION/$AUTH_TYPE/$ROUTE_TYPE/secret"

    //announcement
    private const val ANNOUNCEMENT_TYPE = "announcement"
    const val CREATE_ANNOUNCEMENT = "$API_VERSION/$AUTH_TYPE/$ANNOUNCEMENT_TYPE/create"
    const val UPDATE_ANNOUNCEMENT = "$API_VERSION/$AUTH_TYPE/$ANNOUNCEMENT_TYPE/update"
    const val DELETE_ANNOUNCEMENT = "$API_VERSION/$AUTH_TYPE/$ANNOUNCEMENT_TYPE/delete/{id}"

    const val GET_ANNOUNCEMENT = "$API_VERSION/$AUTH_TYPE/$ANNOUNCEMENT_TYPE/get"
    const val GET_ANNOUNCEMENT_USER = "$API_VERSION/$USER/$ANNOUNCEMENT_TYPE/get"
    const val GET_ANNOUNCEMENT_CLUB = "$API_VERSION/$LOGIN_TYPE/$ANNOUNCEMENT_TYPE/get"



    //feedback
    private const val FEEDBACK = "feedback"
    const val CREATE_FEEDBACK = "$API_VERSION/$USER/$FEEDBACK/create"
    const val GET_FEEDBACK_ADMIN = "$API_VERSION/$AUTH_TYPE/$FEEDBACK/get"
    const val GET_FEEDBACK_CLUB = "$API_VERSION/$LOGIN_TYPE/$FEEDBACK/get"




}