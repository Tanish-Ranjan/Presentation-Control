package com.tanishranjan.presentationcontroller.data.viewmodel

import androidx.lifecycle.ViewModel
import com.tanishranjan.presentationcontroller.data.model.Coordinate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RootModel : ViewModel() {

    private val _touchPad = MutableStateFlow(Coordinate(0f, 0f))
    val touchPad: StateFlow<Coordinate> = _touchPad.asStateFlow()

    private val _isLeft = MutableStateFlow(false)
    val isLeft: StateFlow<Boolean> = _isLeft.asStateFlow()

    private val _isRight = MutableStateFlow(false)
    val isRight: StateFlow<Boolean> = _isRight.asStateFlow()

    private val _scroll = MutableStateFlow(0)
    val scroll: StateFlow<Int> = _scroll.asStateFlow()

    fun updateTouch(value: Coordinate) {
        _touchPad.update {
            it.copy(x = value.x, y = value.y)
        }
    }

    fun updateLeft(value: Boolean) {
        _isLeft.update {
            value
        }
    }

    fun updateRight(value: Boolean) {
        _isRight.update {
            value
        }
    }

    fun updateScroll(value: Int) {
        _scroll.update {
            value
        }
    }

}