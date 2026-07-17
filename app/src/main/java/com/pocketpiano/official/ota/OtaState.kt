package com.pocketpiano.official.ota

sealed interface OtaState {
    data object Idle : OtaState
    data object Scanning : OtaState
    data class DeviceFound(val name: String, val address: String, val rssi: Int) : OtaState
    data class Connecting(val address: String) : OtaState
    data class Ready(val packetSize: Int) : OtaState
    data class Transferring(val sector: Int, val sectors: Int, val percent: Int) : OtaState
    data object Verifying : OtaState
    data object Completed : OtaState
    data class Failed(val reason: String, val recoverable: Boolean = true) : OtaState
}
