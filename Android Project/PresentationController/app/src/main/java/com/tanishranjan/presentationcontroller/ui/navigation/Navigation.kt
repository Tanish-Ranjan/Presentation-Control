package com.tanishranjan.presentationcontroller.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tanishranjan.presentationcontroller.data.navigation.ControllerNav
import com.tanishranjan.presentationcontroller.data.viewmodel.RootModel
import com.tanishranjan.presentationcontroller.ui.screens.ConnectionScreen
import com.tanishranjan.presentationcontroller.ui.screens.ControllerScreen

@Composable
fun Navigation(
    controller: NavHostController,
    rootModel: RootModel
) {

    NavHost(controller, startDestination = ControllerNav.ConnectionScreen.route) {

        composable(ControllerNav.ConnectionScreen.route) {
            ConnectionScreen(controller)
        }

        composable(ControllerNav.ControllerScreen.route) {
            ControllerScreen(rootModel)
        }

    }

}