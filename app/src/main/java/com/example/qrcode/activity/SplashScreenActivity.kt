package com.example.qrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcode.R

/**
 * Activity that displays a splash screen for a few seconds before transitioning
 * to the ScannerActivity.
 */
@SuppressLint("CustomSplashScreen")  // Suppresses the warning for custom splash screen implementation
class SplashScreenActivity : AppCompatActivity() {

    /**
     * Initializes the splash screen activity, sets the layout, and initiates
     * the transition to the ScannerActivity after a 3-second delay.
     *
     * @param savedInstanceState The saved instance state bundle (if any).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the splash screen layout
        setContentView(R.layout.activity_splash)

        // Delay of 3 seconds before transitioning to the ScannerActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an Intent to start the ScannerActivity
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
            // Finish the SplashScreenActivity to remove it from the back stack
            finish()
        }, 3000) // 3000 milliseconds delay
    }
}
