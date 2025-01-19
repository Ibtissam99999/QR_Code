package com.example.qrcode.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcode.R

class TypeCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val menuButton: ImageButton = findViewById(R.id.menu)
        val urlButton: ImageButton = findViewById(R.id.url)
        val locationButton: ImageButton = findViewById(R.id.loc)
        val emailButton: ImageButton = findViewById(R.id.email)
        val textButton: ImageButton = findViewById(R.id.txt)

        menuButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        urlButton.setOnClickListener {
            val intent = Intent(this, UrlActivity::class.java)
            startActivity(intent)
        }

        locationButton.setOnClickListener {
            val intent = Intent(this, LocalisationActivity::class.java)
            startActivity(intent)
        }

        emailButton.setOnClickListener {
            val intent = Intent(this, EmailActivity::class.java)
            startActivity(intent)
        }

        textButton.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)
        }
    }
}
