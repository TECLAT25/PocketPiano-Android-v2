package com.pocketpiano.official.ota

import org.junit.Assert.*
import org.junit.Test

class OtaPacketCodecTest {
    @Test fun startCommandHasExpectedSizeAndLittleEndianLength() {
        val p = OtaPacketCodec.startCommand(0x12345678)
        assertEquals(20, p.size); assertEquals(1, p[0].toInt()); assertEquals(0x78, p[2].toInt() and 0xFF)
        assertEquals(0x56, p[3].toInt() and 0xFF); assertEquals(0x34, p[4].toInt() and 0xFF); assertEquals(0x12, p[5].toInt() and 0xFF)
    }
    @Test fun sectorLastPacketUsesFFAndCrc() {
        val packets = OtaPacketCodec.firmwarePackets(ByteArray(30) { it.toByte() }, 20)
        assertTrue(packets.last().isLast); assertEquals(0xFF, packets.last().sequence)
    }
    @Test fun parsesSectorAckLittleEndian() {
        val ack = OtaPacketCodec.parseSectorAck(byteArrayOf(2,0,1,0))
        assertEquals(2, ack.sectorIndex); assertEquals(1, ack.status); assertFalse(ack.accepted)
    }
}
