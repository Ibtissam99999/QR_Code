package com.example.qrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qrcode.R

/**
 * Activity that handles the selected QR code item.
 * Displays the scan result (text, image, type) and allows actions such as opening the content or sharing it.
 */
class SelectedItemActivity : AppCompatActivity() {

    // Views for displaying scan information
    private lateinit var scanText: TextView
    private lateinit var scanImage: ImageView
    private lateinit var scanType: TextView
    private lateinit var openButton: Button
    private lateinit var shareButton: Button

    /**
     * Initializes the activity, sets up the layout, and handles the QR code scan result.
     *
     * @param savedInstanceState The saved instance state bundle (if any).
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge layout
        enableEdgeToEdge()

        // Set the activity layout
        setContentView(R.layout.activity_selected_item)

        // Apply window insets to handle system bars (status and navigation bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the views
        scanText = findViewById(R.id.text_result)
        scanType = findViewById(R.id.text_result_type)
        scanImage = findViewById(R.id.scanView)
        openButton = findViewById(R.id.button_open)
        shareButton = findViewById(R.id.button_share)

        // Get data passed through the Intent (QR code scan result)
        val text = intent.getStringExtra("scanText")
        val imageUri = intent.getStringExtra("scanImage")
        val type = intent.getStringExtra("scanType")

        // Set the scan result text and type
        scanText.text = text
        scanType.text = type

        // If an image URI is provided, display the image
        if (!imageUri.isNullOrEmpty()) {
            scanImage.setImageURI(Uri.parse(imageUri))
        }

        // Open button click listener to handle different QR code types
        openButton.setOnClickListener {
            if (text != null && type != null) {
                handleQRCode(text, type)
            }
        }

        // Share button click listener to share the scanned text
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    /**
     * Handles different actions based on the QR code type (URL, phone number, SMS, email, etc.).
     *
     * @param resultText The text extracted from the QR code.
     * @param type The type of QR code (e.g., URL, phone number, SMS).
     */
    private fun handleQRCode(resultText: String, type: String) = when (type) {
        // Handle URL type QR code
        "URL" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))

        // Handle phone number QR code
        "Phone Number" -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(resultText)))

        // Handle SMS QR code
        "SMS" -> startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(resultText)))

        // Handle email QR code
        "Email" -> startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$resultText")))

        // Handle WhatsApp QR code
        "WhatsApp" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))

        // Handle location QR code
        "Location" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))

        // Handle WiFi QR code (manual configuration required)
        "WiFi" -> Toast.makeText(this, "WiFi QR Code detected. Configure manually.", Toast.LENGTH_SHORT).show()

        // Default case: Share the result text
        else -> {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, resultText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}
