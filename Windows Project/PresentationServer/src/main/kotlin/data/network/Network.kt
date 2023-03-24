package data.network

import Controller
import data.helper.Window
import utils.VERSION_CODE
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket

class Network {

    companion object {

        var connected: Boolean = false
        private val serverSocket = ServerSocket(1024)
        private var socket: Socket? = null
        private var dis: DataInputStream? = null

        fun getIP(): String {

            val address = Inet4Address.getLocalHost().hostAddress
            if (!address.startsWith("192.168.")) {
                throw IllegalStateException("Please make sure you are on a network.")
            }
            return address

        }

        fun start(): Boolean {

            return try {

                if (socket?.isConnected == true) {
                    close()
                }
                socket = serverSocket.accept() // Waits till we connect
                dis = DataInputStream(socket!!.getInputStream())
                var version: Double?

                // Check version compatibility
                dis?.readUTF().also {
                    if (it?.startsWith("version:") == true) {
                        version = it.split(":")[1].toDoubleOrNull()
                    } else {
                        DataOutputStream(socket!!.getOutputStream()).writeUTF("Version Check: Failed")
                        throw RuntimeException("Controller didn't send version code")
                    }
                }
                if (version == null) throw RuntimeException("Failed to resolve version code")
                if (version != VERSION_CODE) {
                    DataOutputStream(socket!!.getOutputStream()).writeUTF("Version Check: Failed")
                    throw RuntimeException(
                        if (version!! < VERSION_CODE) {
                            "Version Incompatible: Controller version is outdated. Please update the controller app."
                        } else {
                            "Version Incompatible: Client version is outdated. Please update the client software."
                        }
                    )
                }

                DataOutputStream(socket!!.getOutputStream()).writeUTF("Version Check: Passed")

                /*try {
                    calibrate()
                } catch (e: Exception) {
                    throw RuntimeException("\nCalibration failed!\n${e.message}")
                }*/

                connected = true
                println("Connected!")
                Window.minimizeWindow()
                true

            } catch (e: Exception) {
                connected = false
                close()
                if (e.message != null) {
                    println("Network Start: ${e.message}")
                }
                false
            }

        }

        /*private fun calibrate() {

             Window.createCalibrationWindow()

             var center = Coordinate(0.0, 0.0) // Theta Angle

             for (x in 1..3) {

                 when (x) {
                     1 -> {
                         Window.calibrateWindowAt(WindowPosition.CENTER)
                         val value = dis?.readUTF()?.split(",") ?: listOf()
                         center = Coordinate(value[0].toDouble(), value[1].toDouble())
                         Controller.setCenter(center)
                         DataOutputStream(socket!!.getOutputStream()).writeUTF("X Calibrating")
                     }

                     2 -> {
                         Window.calibrateWindowAt(WindowPosition.TOP)
                         val value = dis?.readUTF()?.toDouble()!!
                         Controller.setRateY(abs(center.Y - value) * 2)
                         DataOutputStream(socket!!.getOutputStream()).writeUTF("Y Calibrating")
                     }

                     3 -> {
                         Window.calibrateWindowAt(WindowPosition.LEFT)
                         val value = dis?.readUTF()?.toDouble()!!
                         Controller.setRateX(abs(center.X - value) * 2)
                         DataOutputStream(socket!!.getOutputStream()).writeUTF("Calibration Complete")
                     }
                 }

             }

             Window.closeCalibrationWindow()

         }*/

        fun receive(): ArrayList<String> {

            val msgs = ArrayList<String>()
            var lastMsg = ""

            if (socket?.isConnected == true) {
                try {

                    val iterator = (lastMsg + dis?.readUTF()).split("|").iterator()

                    iterator.forEach {
                        if (iterator.hasNext()) {
                            msgs += it
                        } else {
                            lastMsg = it
                        }
                    }

                } catch (e: Exception) {
                    msgs.clear()
                    connected = false
                    if (e.message != null) {
                        println("Network Receive: ${e.message}")
                    }
                    start()
                }
            }
            return msgs

        }

        private fun close() {
            println("Device Disconnected")
            connected = false
            socket?.close()
            Controller.reset()
        }

    }

}