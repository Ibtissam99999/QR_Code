package com.example.qrcode.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcode.R
import com.example.qrcode.data.createData
import com.example.qrcode.list.create_List
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class LocalisationActivity : AppCompatActivity() {
    private lateinit var ivQRCode: ImageView
    private lateinit var etLongitude: EditText
    private lateinit var etLatitude: EditText
    private lateinit var btnCreateQR: Button
    private lateinit var menuButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)

        ivQRCode = findViewById(R.id.ivQRCode)
        etLongitude = findViewById(R.id.longtitude)
        etLatitude = findViewById(R.id.latitude)
        btnCreateQR = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        btnCreateQR.setOnClickListener {
            val longitude = etLongitude.text.toString()
            val latitude = etLatitude.text.toString()

            if (longitude.isEmpty() || latitude.isEmpty()) {
                Toast.makeText(this, "Please enter both longitude and latitude", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val geoUri = "geo:$latitude,$longitude"

            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(geoUri, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }

                ivQRCode.setImageBitmap(bmp)
                ivQRCode.visibility = ImageView.VISIBLE

                val qrCodeData = createData(bmp, "LOCALISATION", geoUri)
                create_List.add(qrCodeData)

            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }
}
