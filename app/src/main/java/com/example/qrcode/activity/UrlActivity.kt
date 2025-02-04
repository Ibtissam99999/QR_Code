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
 * Activity for generating a QR code from a URL or email address entered by the user.
 * The user can also navigate back to the TypeCodeActivity using the menu button.
 */
class UrlActivity : AppCompatActivity() {

    // Views for displaying and interacting with the URL input and QR code generation
    private lateinit var ivQRCode: ImageView
    private lateinit var etData: EditText
    private lateinit var btnGenerateQRCode: Button
    private lateinit var menuButton: ImageView

    /**
     * Initializes the activity, sets the layout, and handles user interactions to generate
     * a QR code based on the URL or email entered by the user.
     *
     * @param savedInstanceState The saved instance state bundle (if any).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        // Initialize views
        ivQRCode = findViewById(R.id.ivQRCode)
        etData = findViewById(R.id.edurl)
        btnGenerateQRCode = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        // Set up the menu button to navigate to the TypeCodeActivity
        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        // Set up the generate QR code button
        btnGenerateQRCode.setOnClickListener {
            // Get and trim the input URL or email address
            val inputData = etData.text.toString().trim()

            // Check if the input is empty
            if (inputData.isEmpty()) {
                Toast.makeText(this, "Enter some data", Toast.LENGTH_SHORT).show()
            } else {
                // Adjust the input to ensure it's a valid URL or email
                val data = when {
                    android.util.Patterns.EMAIL_ADDRESS.matcher(inputData).matches() -> "mailto:$inputData"
                    android.util.Patterns.WEB_URL.matcher(inputData).matches() && !inputData.startsWith("http") -> "https://$inputData"
                    else -> inputData
                }

                // Create the QR code using the adjusted data
                val writer = QRCodeWriter()
                try {
                    // Encode the data into a QR code matrix
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    // Create a bitmap to represent the QR code
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            // Set each pixel based on the QR code data
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }

                    // Display the generated QR code in the ImageView
                    ivQRCode.setImageBitmap(bmp)
                    ivQRCode.visibility = ImageView.VISIBLE

                    // Save the generated QR code in the list
                    val qrCodeData = createData(bmp, "URL", inputData)
                    create_List.add(qrCodeData)

                    // Show a success message
                    Toast.makeText(this, "QR Code generated and saved!", Toast.LENGTH_SHORT).show()

                } catch (e: WriterException) {
                    // Handle any errors during QR code generation
                    e.printStackTrace()
                    Toast.makeText(this, "Error generating QR Code", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
