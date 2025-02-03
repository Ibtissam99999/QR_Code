package com.example.qrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
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

class SelectedItemActivity : AppCompatActivity() {
    private lateinit var scanText: TextView
    private lateinit var scanImage: ImageView
    private lateinit var scanType: TextView
    private lateinit var openButton: Button
    private lateinit var shareButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selected_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        scanText = findViewById(R.id.text_result)
        scanType = findViewById(R.id.text_result_type)
        scanImage = findViewById(R.id.scanView)
        openButton = findViewById(R.id.button_open)
        shareButton = findViewById(R.id.button_share)

        val text = intent.getStringExtra("scanText")
        val imageUri = intent.getStringExtra("scanImage")
        val type = intent.getStringExtra("scanType")

        scanText.text = text
        scanImage.setImageURI(Uri.parse(imageUri))
        scanType.text = type

        openButton.setOnClickListener {
            if (text != null && type != null) {
                handleQRCode(text, type)
            }
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    private fun handleQRCode(resultText: String, type: String) = when (type) {
        "URL" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))
        "Phone Number" -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(resultText)))
        "SMS" -> startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(resultText)))
        "Email" -> startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$resultText")))
        "WhatsApp" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))
        "Location" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resultText)))
        "WiFi" -> Toast.makeText(this, "WiFi QR Code detected. Configure manually.", Toast.LENGTH_SHORT).show()
        else -> {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, resultText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}