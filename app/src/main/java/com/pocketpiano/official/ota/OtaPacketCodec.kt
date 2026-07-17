package com.pocketpiano.official.ota

import java.io.ByteArrayOutputStream

data class SectorPacket(val sectorIndex: Int, val sequence: Int, val bytes: ByteArray, val isLast: Boolean)

data class SectorAck(val sectorIndex: Int, val status: Int) {
    val accepted: Boolean get() = status == 0
}

data class CommandAck(val acknowledgedCommand: Int, val status: Int)

object OtaPacketCodec {
    fun command(id: Int, payload: ByteArray = byteArrayOf()): ByteArray {
        require(payload.size <= 16)
        val packet = ByteArray(OtaProtocol.COMMAND_PACKET_SIZE)
        packet[0] = id.toByte(); packet[1] = (id ushr 8).toByte()
        payload.copyInto(packet, 2)
        val crc = EspCrc16.calculate(packet, 0, 18)
        packet[18] = crc.toByte(); packet[19] = (crc ushr 8).toByte()
        return packet
    }

    fun startCommand(firmwareSize: Long): ByteArray {
        require(firmwareSize in 1..0xFFFF_FFFFL)
        val p = ByteArray(4) { i -> (firmwareSize ushr (8 * i)).toByte() }
        return command(OtaProtocol.COMMAND_START, p)
    }

    fun endCommand(): ByteArray = command(OtaProtocol.COMMAND_END)

    fun firmwarePackets(firmware: ByteArray, packetSize: Int): List<SectorPacket> {
        require(packetSize >= 8)
        val dataCapacity = packetSize - 3
        val output = mutableListOf<SectorPacket>()
        firmware.asList().chunked(OtaProtocol.SECTOR_SIZE).forEachIndexed { sectorIndex, boxed ->
            val sector = boxed.toByteArray()
            var offset = 0; var sequence = 0
            while (offset < sector.size) {
                val count = minOf(dataCapacity, sector.size - offset)
                val last = offset + count == sector.size
                val stream = ByteArrayOutputStream(count + if (last) 5 else 3)
                stream.write(sectorIndex and 0xFF); stream.write((sectorIndex ushr 8) and 0xFF)
                stream.write(if (last) 0xFF else sequence)
                stream.write(sector, offset, count)
                if (last) { val crc = EspCrc16.calculate(sector); stream.write(crc and 0xFF); stream.write((crc ushr 8) and 0xFF) }
                output += SectorPacket(sectorIndex, if (last) 0xFF else sequence, stream.toByteArray(), last)
                offset += count; sequence++
            }
        }
        return output
    }

    fun parseSectorAck(data: ByteArray): SectorAck {
        require(data.size >= 4)
        val sector = (data[0].toInt() and 0xFF) or ((data[1].toInt() and 0xFF) shl 8)
        val status = (data[2].toInt() and 0xFF) or ((data[3].toInt() and 0xFF) shl 8)
        return SectorAck(sector, status)
    }

    fun parseCommandAck(data: ByteArray): CommandAck {
        require(data.size >= 6)
        val id = u16(data, 0); require(id == OtaProtocol.COMMAND_ACK)
        return CommandAck(u16(data, 2), u16(data, 4))
    }

    private fun u16(b: ByteArray, i: Int) = (b[i].toInt() and 0xFF) or ((b[i+1].toInt() and 0xFF) shl 8)
    private fun List<Byte>.toByteArray() = ByteArray(size) { this[it] }
}
