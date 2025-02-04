package com.example.qrcode.data

import android.graphics.Bitmap

/**
 * Data class representing a QR code and its associated data.
 * This class holds the image, type, and content of a generated or scanned QR code.
 *
 * @param qrCode The Bitmap image representing the QR code.
 * @param type The type of data encoded in the QR code (e.g., "URL", "Email", "Text").
 * @param textQr The text or content encoded in the QR code.
 */
data class createData(
    val qrCode: Bitmap,  // The Bitmap image of the QR code
    val type: String,    // The type of the QR code (e.g., URL, Email)
    val textQr: String   // The content or data encoded in the QR code
)
