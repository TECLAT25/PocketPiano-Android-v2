package com.pocketpiano.official.firmware

import java.security.MessageDigest

data class FirmwareInfo(val size: Int, val sha256: String)

object FirmwareValidator {
    fun inspect(bytes: ByteArray): FirmwareInfo {
        require(bytes.isNotEmpty()) { "El firmware está vacío" }
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return FirmwareInfo(bytes.size, digest.joinToString("") { "%02x".format(it) })
    }
}
