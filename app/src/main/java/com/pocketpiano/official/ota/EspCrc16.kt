package com.pocketpiano.official.ota

object EspCrc16 {
    fun calculate(data: ByteArray, offset: Int = 0, length: Int = data.size - offset): Int {
        require(offset >= 0 && length >= 0 && offset + length <= data.size)
        var crc = 0
        for (i in offset until offset + length) {
            crc = crc xor ((data[i].toInt() and 0xFF) shl 8)
            repeat(8) { crc = if ((crc and 0x8000) != 0) ((crc shl 1) xor 0x1021) else (crc shl 1); crc = crc and 0xFFFF }
        }
        return crc
    }
}
