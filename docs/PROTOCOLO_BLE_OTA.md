# Protocolo BLE OTA de PocketPiano — especificación reconstruida

## Alcance y nivel de certeza

Esta especificación procede del análisis estático de `PocketPianoAppV1.apk`. Los campos marcados como **confirmados** aparecen directamente en el código. Los canales 8021 y 8023 están presentes y se suscriben, pero su semántica no está implementada en la APK analizada.

## Identificación

- Nombre anunciado aceptado: contiene `Pocket`.
- Company ID observado: `741` (`0x02E5`).
- Servicio OTA: `00008018-0000-1000-8000-00805f9b34fb`.
- Firmware/ACK sector: `8020`.
- Progreso: `8021`.
- Comandos/ACK: `8022`.
- Canal auxiliar: `8023`.
- CCCD: `2902`, configurado con indicaciones.

## Negociación

1. Conexión GATT LE.
2. Prioridad alta.
3. Solicitud MTU 517.
4. Si tiene éxito, `packetSize=463`; si falla, `packetSize=20`.
5. Descubrimiento del servicio y cuatro características.
6. Activación secuencial de indicaciones: 8020 → 8021 → 8022 → 8023.
7. Envío de START.

## Comando de 20 bytes

| Offset | Tamaño | Contenido |
|---|---:|---|
| 0 | 2 | ID little-endian |
| 2 | hasta 16 | payload, resto a cero |
| 18 | 2 | CRC16 little-endian de bytes 0..17 |

IDs: `START=1`, `END=2`, `ACK=3`.

START contiene el tamaño total del `.bin` como entero de 32 bits little-endian en offsets 2..5. END no contiene payload.

## Firmware

- Sectores de 4096 bytes.
- Cada paquete empieza con: sector low, sector high, secuencia.
- La capacidad de datos es `packetSize - 3`.
- El último paquete usa secuencia `0xFF` y añade CRC16 del sector, low/high.
- Después del último paquete, el cliente espera ACK antes de continuar con el siguiente sector.

## ACK de sector

- Offset 0..1: índice sector little-endian.
- Offset 2..3: estado little-endian.
- Estados: 0 OK; 1 CRC; 2 índice; 3 longitud; otros desconocidos.

## Riesgos y pendientes de prueba dinámica

- Confirmar el polinomio/valor inicial del CRC con tramas reales o vectores del firmware.
- Confirmar estructura completa del ACK de comando y su CRC.
- Confirmar finalidad de 8021 y 8023.
- Confirmar comportamiento del bootloader ante desconexión, downgrade y firmware incompatible.
- Confirmar si existe validación criptográfica dentro del dispositivo.
