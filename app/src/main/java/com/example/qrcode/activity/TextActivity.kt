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

/**
 * Activity that allows the user to input text and generate a QR code from it.
 * The user can also navigate to another activity using the menu button.
 */
class TextActivity : AppCompatActivity() {

    // Views for QR code generation and data input
    private lateinit var ivQRCode: ImageView
    private lateinit var etData: EditText
    private lateinit var btnGenerateQRCode: Button
    private lateinit var menuButton: ImageView

    /**
     * Initializes the activity, sets up the layout, and handles user interactions for QR code generation.
     *
     * @param savedInstanceState The saved instance state bundle (if any).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        // Initialize views
        ivQRCode = findViewById(R.id.ivQRCode)
        etData = findViewById(R.id.edText)
        btnGenerateQRCode = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        // Set up the menu button to navigate to another activity
        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        // Set up the generate QR code button
        btnGenerateQRCode.setOnClickListener {
            // Get the data entered by the user
            val data = etData.text.toString()

            // Check if the input data is empty
            if (data.isEmpty()) {
                // Show a toast message if no data is entered
                Toast.makeText(this, "Enter some data", Toast.LENGTH_SHORT).show()
            } else {
                // Create the QR code using the input data
                val writer = QRCodeWriter()
                try {
                    // Encode the data into a QR code
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    // Create a bitmap to display the QR code
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            // Set each pixel based on the QR code data
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    // Set the generated QR code image to the ImageView
                    ivQRCode.setImageBitmap(bmp)
                    ivQRCode.visibility = ImageView.VISIBLE

                    // Create a data object for the generated QR code and add it to the list
                    val qrCodeData = createData(bmp, "Text", data)
                    create_List.add(qrCodeData)

                } catch (e: WriterException) {
                    // Handle any exceptions that occur during QR code generation
                    e.printStackTrace()
                }
            }
        }
    }
}
