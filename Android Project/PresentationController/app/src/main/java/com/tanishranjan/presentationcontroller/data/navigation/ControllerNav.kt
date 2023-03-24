package com.tanishranjan.presentationcontroller.data.navigation

sealed class ControllerNav(val route: String) {
    object ConnectionScreen: ControllerNav("connection_scr")
    object ControllerScreen: ControllerNav("controller_scr")
}