package com.example.qrcode.data

/**
 * Data class representing an item that has been created, typically a QR code with its associated data.
 * This class holds the resource ID of the QR code image and the text encoded in the QR code.
 *
 * @param qr The resource ID (integer) of the generated QR code image.
 * @param textQr The text or content that was encoded into the QR code.
 */
data class ItemCreated(
    val qr: Int,        // The resource ID of the generated QR code image
    val textQr: String  // The text or content encoded into the QR code
)
