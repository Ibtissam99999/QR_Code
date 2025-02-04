package com.example.qrcode.data

import android.graphics.Bitmap

/**
 * Data class representing the history of a generated or scanned QR code.
 * This class holds the QR code image, its type, and the content encoded in the QR code.
 *
 * @param qrCode The Bitmap image representing the QR code.
 * @param type The type of data encoded in the QR code (e.g., "URL", "Email", "Text").
 * @param textQr The text or content encoded in the QR code.
 */
data class historyData(
    val qrCode: Bitmap,  // The Bitmap image of the QR code
    val type: String,    // The type of the QR code (e.g., URL, Email)
    val textQr: String   // The content or data encoded in the QR code
)
