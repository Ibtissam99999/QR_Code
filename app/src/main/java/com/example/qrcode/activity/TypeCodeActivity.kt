package com.example.qrcode.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcode.R

/**
 * Activity that provides a menu with buttons to navigate to different activities
 * for creating different types of QR codes.
 */
class TypeCodeActivity : AppCompatActivity() {

    /**
     * Initializes the activity, sets the layout, and handles user interactions
     * to navigate to the corresponding QR code creation activity based on the user's selection.
     *
     * @param savedInstanceState The saved instance state bundle (if any).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        // Initialize the buttons for the different QR code types
        val menuButton: ImageButton = findViewById(R.id.menu)
        val urlButton: ImageButton = findViewById(R.id.url)
        val locationButton: ImageButton = findViewById(R.id.loc)
        val emailButton: ImageButton = findViewById(R.id.email)
        val textButton: ImageButton = findViewById(R.id.txt)

        // Set up the menu button to navigate to the CreateActivity
        menuButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        // Set up the URL button to navigate to the UrlActivity
        urlButton.setOnClickListener {
            val intent = Intent(this, UrlActivity::class.java)
            startActivity(intent)
        }

        // Set up the location button to navigate to the LocalisationActivity
        locationButton.setOnClickListener {
            val intent = Intent(this, LocalisationActivity::class.java)
            startActivity(intent)
        }

        // Set up the email button to navigate to the EmailActivity
        emailButton.setOnClickListener {
            val intent = Intent(this, EmailActivity::class.java)
            startActivity(intent)
        }

        // Set up the text button to navigate to the TextActivity
        textButton.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)
        }
    }
}
