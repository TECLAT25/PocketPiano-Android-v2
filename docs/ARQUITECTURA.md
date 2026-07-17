# Arquitectura propuesta

## Capas

- `scanner`: descubrimiento BLE y parsing del advertising.
- `transport`: sesión GATT, MTU, cola de escrituras e indicaciones.
- `ota`: máquina de estados, sectorización, CRC, START/END y reintentos.
- `firmware`: catálogo, SHA-256, firma digital, modelo/hardware y políticas de versión.
- `diagnostics`: RSSI, MTU, servicios, logs exportables y códigos de error.
- `ui`: modo cliente y modo técnico.

## Fases

1. Compatibilidad exacta con OTA actual.
2. Captura dinámica BLE y pruebas con unidad recuperable.
3. Reintento por sector y recuperación de desconexiones.
4. Catálogo oficial HTTPS y firma Ed25519/ECDSA del firmware.
5. Identidad del piano: serie, hardware, bootloader y firmware.
6. Configuración de velocity/MIDI/LED, únicamente cuando se documenten UUID y comandos reales del firmware.

## Regla de ingeniería inversa

El código descompilado se usa como evidencia para describir comportamiento. La nueva implementación se mantiene separada, con nombres, arquitectura y pruebas propias.
