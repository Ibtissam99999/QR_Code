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

class UrlActivity : AppCompatActivity() {

    private lateinit var ivQRCode: ImageView
    private lateinit var etData: EditText
    private lateinit var btnGenerateQRCode: Button
    private lateinit var menuButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        //Initializing Views
        ivQRCode = findViewById(R.id.ivQRCode)
        etData = findViewById(R.id.edurl)
        btnGenerateQRCode = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        btnGenerateQRCode.setOnClickListener {
            val inputData = etData.text.toString().trim()

            if (inputData.isEmpty()) {
                Toast.makeText(this, "Enter some data", Toast.LENGTH_SHORT).show()
            } else {
                val data = when {
                    android.util.Patterns.EMAIL_ADDRESS.matcher(inputData).matches() -> "mailto:$inputData"
                    android.util.Patterns.WEB_URL.matcher(inputData).matches() && !inputData.startsWith("http") -> "https://$inputData"
                    else -> inputData
                }

                // Génération du QR code
                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
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

                    val qrCodeData = createData(bmp, "URL", inputData)
                    create_List.add(qrCodeData)

                    Toast.makeText(this, "QR Code generated and saved!", Toast.LENGTH_SHORT).show()

                } catch (e: WriterException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error generating QR Code", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
