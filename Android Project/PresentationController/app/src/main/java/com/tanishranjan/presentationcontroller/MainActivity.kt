package com.tanishranjan.presentationcontroller

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.tanishranjan.presentationcontroller.data.navigation.ControllerNav
import com.tanishranjan.presentationcontroller.data.network.Network
import com.tanishranjan.presentationcontroller.data.viewmodel.RootModel
import com.tanishranjan.presentationcontroller.ui.navigation.Navigation
import com.tanishranjan.presentationcontroller.ui.theme.PresentationControllerTheme

class MainActivity : ComponentActivity() {

    private val rootModel: RootModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            PresentationControllerTheme {

                val controller = rememberNavController()
                val context = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(controller, rootModel)
                }

                LaunchedEffect(Unit) {
                    Network.isConnected.collect {
                        if (!it) {
                            if (!Network.isInitial) {
                                controller.navigate(ControllerNav.ConnectionScreen.route) {
                                    launchSingleTop = true
                                    val r = controller.currentBackStackEntry?.destination?.route
                                    if (r != null) {
                                        popUpTo(
                                            r
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                                Toast.makeText(context, "Connection Lost", Toast.LENGTH_SHORT).show()
                            }
                            Network.isInitial = false
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    rootModel.apply {
                        scroll.collect {
                            Network.send("scr:$it")
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    rootModel.apply {
                        touchPad.collect {
                            Network.send("drg:${it.x},${it.y}")
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    rootModel.apply {
                        isLeft.collect {
                            Network.send("lb:$it")
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    rootModel.apply {
                        isRight.collect {
                            Network.send("rb:$it")
                        }
                    }
                }

            }

        }

    }

}