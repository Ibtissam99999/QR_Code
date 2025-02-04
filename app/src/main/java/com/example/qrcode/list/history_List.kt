package com.example.qrcode.list

import com.example.qrcode.data.historyData

/**
 * A mutable list that stores the historical data of QR codes.
 * Each item in the list is an instance of [historyData] representing a QR code with its associated type and content.
 *
 * This list is used to keep track of previously scanned or generated QR codes within the application.
 */
val history_List = mutableListOf<historyData>()
