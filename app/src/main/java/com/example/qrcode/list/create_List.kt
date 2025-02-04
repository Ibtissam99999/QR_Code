package com.example.qrcode.list

import com.example.qrcode.data.createData

/**
 * A mutable list that stores the data of generated QR codes.
 * Each item in the list is an instance of [createData] representing a QR code with its associated type and content.
 *
 * This list is used to keep track of all QR codes created within the application.
 */
val create_List = mutableListOf<createData>()
