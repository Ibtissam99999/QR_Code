package com.example.qrcode.activity


import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcode.R

class SelectedItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selected_item)

       val  scanText: TextView = findViewById(R.id.text_res)
        val scanType: TextView = findViewById(R.id.text_resultType)
        val scanImage: ImageView = findViewById(R.id.scanView)
        val openButton: Button = findViewById(R.id.open)
        val shareButton: Button = findViewById(R.id.share)

        val text = intent.getStringExtra("scanText")
        val imageUri = intent.getByteArrayExtra("scanImage")
        val type = intent.getStringExtra("scanType")

        scanText.text = text
        scanType.text = type

        if (imageUri !=null){
            val bitmap = BitmapFactory.decodeByteArray(imageUri, 0, imageUri.size)
            scanImage.setImageBitmap(bitmap)
        }

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