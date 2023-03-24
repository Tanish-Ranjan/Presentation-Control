package com.tanishranjan.presentationcontroller.ui.screens

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tanishranjan.presentationcontroller.R
import com.tanishranjan.presentationcontroller.data.model.Coordinate
import com.tanishranjan.presentationcontroller.data.viewmodel.RootModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControllerScreen(
    rootModel: RootModel
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val leftIS = remember {
            MutableInteractionSource()
        }

        val rightIS = remember {
            MutableInteractionSource()
        }

        val scrollUpIS = remember {
            MutableInteractionSource()
        }

        val scrollDownIS = remember {
            MutableInteractionSource()
        }

        LaunchedEffect(true) {
            leftIS.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        rootModel.updateLeft(true)
                    }
                    else -> {
                        rootModel.updateLeft(false)
                    }
                }
            }
        }

        LaunchedEffect(true) {
            rightIS.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        rootModel.updateRight(true)
                    }
                    else -> {
                        rootModel.updateRight(false)
                    }
                }
            }
        }

        LaunchedEffect(true) {
            scrollUpIS.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        rootModel.updateScroll(-1)
                    }
                    else -> {
                        rootModel.updateScroll(0)
                    }
                }
            }
        }

        LaunchedEffect(true) {
            scrollDownIS.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        rootModel.updateScroll(1)
                    }
                    else -> {
                        rootModel.updateScroll(0)
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth()
        ) {

            Button(
                {},
                Modifier
                    .height(64.dp)
                    .weight(1f),
                shape = RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp),
                interactionSource = leftIS
            ) {
                Text("L")
            }

            Spacer(Modifier.width(4.dp))

            Button(
                {},
                Modifier
                    .height(64.dp)
                    .weight(1f),
                shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 0.dp),
                interactionSource = rightIS
            ) {
                Text("R")
            }

        }

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier.weight(1f)
        ) {

            var pointer by remember {
                mutableStateOf(false)
            }

            // Touch Pad Mimic
            Surface(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .pointerInput(pointer) {
                        detectDragGestures(
                            onDrag = { _, amt ->
                                rootModel.updateTouch(Coordinate(amt.x, amt.y))
                            },
                            onDragEnd = {
                                pointer = !pointer
                            }
                        )
                    },
                RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp),
                Color.LightGray
            ) {}

            Spacer(Modifier.width(4.dp))

            Column(
                Modifier.fillMaxHeight()
            ) {

                Surface(
                    onClick = {},
                    modifier = Modifier
                        .width(56.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(0.dp, 12.dp, 0.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                    interactionSource = scrollUpIS
                ) {

                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            painterResource(R.drawable.scroll_up),
                            "Scroll Up",
                            Modifier.size(36.dp),
                            MaterialTheme.colorScheme.onPrimary
                        )

                    }

                }

                Spacer(Modifier.height(4.dp))

                Surface(
                    onClick = {},
                    modifier = Modifier
                        .width(56.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                    interactionSource = scrollDownIS
                ) {

                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            painterResource(R.drawable.scroll_down),
                            "Scroll Down",
                            Modifier.size(36.dp),
                            MaterialTheme.colorScheme.onPrimary
                        )

                    }

                }

            }
        }
    }

}