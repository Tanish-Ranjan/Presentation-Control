package com.tanishranjan.presentationcontroller.data.network

import com.tanishranjan.presentationcontroller.data.util.VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class Network {

    companion object {

        private val _isConnected = MutableStateFlow(false)
        val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
        var isInitial = true

        private var socket: Socket? = null
        private var dos: DataOutputStream? = null

        suspend fun connect(ip: String): Boolean {
            return try {
                if (socket?.isConnected == true) {
                    close()
                }
                withContext(Dispatchers.IO) {
                    socket = Socket(ip, 1024)
                    dos = DataOutputStream(socket?.getOutputStream())
                    send("version:$VERSION", false)
                    val check = DataInputStream(socket?.getInputStream()).readUTF()
                    if (check != "Version Check: Passed") {
                        close()
                        return@withContext false
                    }
                    _isConnected.update { true }
                    true
                }
            } catch (e: Exception) {
                close()
                e.printStackTrace()
                false
            }
        }

        suspend fun send(msg: String, withDifferentiators: Boolean = true) {
            if (socket?.isConnected == true) {
                try {
                    withContext(Dispatchers.IO) {
                        dos?.writeUTF(if (withDifferentiators) "|$msg|" else msg)
                    }
                } catch (e: Exception) {
                    close()
                    e.printStackTrace()
                }
            }
        }

        private suspend fun close() {
            _isConnected.update { false }
            withContext(Dispatchers.IO) {
                dos?.close()
                socket?.close()
            }
        }

    }

}