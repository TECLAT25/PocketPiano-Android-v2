# PocketPiano Android v2

Primera base limpia para sustituir la APK OTA antigua.

Incluye:
- Especificación reconstruida del protocolo BLE OTA.
- Codec Kotlin de comandos, paquetes y ACK.
- CRC16 y validación SHA-256.
- Estados de operación y permisos modernos Android 12+.
- Interfaz inicial Android Material 3.
- Pruebas unitarias del formato de paquetes.

## Abrir

1. Android Studio con JDK 17.
2. Instalar Android SDK 36.
3. Abrir esta carpeta y sincronizar Gradle.
4. Ejecutar las pruebas `OtaPacketCodecTest`.

## Estado

Esta entrega es la **fase 1**. El motor GATT y la actualización real deben validarse con un PocketPiano de ensayo antes de utilizarse en unidades de cliente.
