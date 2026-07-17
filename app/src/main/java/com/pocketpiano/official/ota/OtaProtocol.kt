package com.pocketpiano.official.ota

import java.util.UUID

object OtaProtocol {
    const val DEVICE_NAME_TOKEN = "Pocket"
    const val COMPANY_ID = 741
    const val SECTOR_SIZE = 4096
    const val REQUESTED_MTU = 517
    const val LARGE_PACKET_SIZE = 463
    const val FALLBACK_PACKET_SIZE = 20
    const val COMMAND_PACKET_SIZE = 20

    const val COMMAND_START = 1
    const val COMMAND_END = 2
    const val COMMAND_ACK = 3

    val SERVICE_UUID: UUID = bleUuid("8018")
    val FIRMWARE_UUID: UUID = bleUuid("8020")
    val PROGRESS_UUID: UUID = bleUuid("8021")
    val COMMAND_UUID: UUID = bleUuid("8022")
    val CUSTOMER_UUID: UUID = bleUuid("8023")
    val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    private fun bleUuid(short: String) = UUID.fromString("0000$short-0000-1000-8000-00805f9b34fb")
}
