package com.example.qrcode.activity

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.webkit.URLUtil.isValidUrl
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrcode.R
import com.example.qrcode.data.historyData
import com.example.qrcode.list.history_List
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.regex.Pattern

class ScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var importPic: TextView
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private val CAMERA_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        importPic = findViewById(R.id.import_pic)

        codeScanner = CodeScanner(this, scannerView)
        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    decodeQRCodeFromUri(uri)
                }
            }
        }

        setupCodeScanner(scannerView)
        setupBottomNavigation()

        importPic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            selectImageLauncher.launch(intent)
        }

        //Camera
        if (ContextCompat.checkSelfPermission(this,CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this,"Camera permission already agreed",Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(CAMERA), CAMERA_CODE
            )
        }
    }

    private fun setupCodeScanner(scannerView: CodeScannerView) {
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback { result ->
            runOnUiThread {
                showResultAfterDelay(result.text)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    /*
    cette fonction est pour affic
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.scan

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scan -> true
                R.id.historic -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.create -> {
                    startActivity(Intent(this, CreateActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun decodeQRCodeFromUri(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            bitmap?.let {
                val qrCodeResult = decodeQRCodeFromBitmap(it)
                qrCodeResult?.let { result ->
                    showBottomSheet(result.text)
                } ?: run {
                    Toast.makeText(this, "No QR Code detected", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error : ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decodeQRCodeFromBitmap(bitmap: Bitmap): Result? {
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val luminanceSource: LuminanceSource = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

        return try {
            com.google.zxing.qrcode.QRCodeReader().decode(binaryBitmap)
        } catch (e: Exception) {
            null
        }
    }

    private fun showResultAfterDelay(resultText: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            showBottomSheet(resultText)
        }, 2000)
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(resultText: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)

        val textViewResult = bottomSheetView.findViewById<TextView>(R.id.text_result)
        val buttonOpen = bottomSheetView.findViewById<TextView>(R.id.button_open)
        val buttonShare = bottomSheetView.findViewById<TextView>(R.id.button_share)

        textViewResult.text = resultText

        val qrCodeType = getQRCodeType(resultText)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(resultText, BarcodeFormat.QR_CODE, 400, 400)

        val el = historyData(bitmap, qrCodeType, resultText)
        history_List.add(el)

        buttonOpen.setOnClickListener {
            handleQRCode(resultText, qrCodeType)
        }

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, resultText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun getQRCodeType(resultText: String): String {
        return when {
            isValidUrl(resultText) -> "URL"
            isValidEmail(resultText) -> "Email"
            isValidWifi(resultText) -> "WiFi"
            isValidWhatsApp(resultText) -> "WhatsApp"
            isValidGeoLocation(resultText) -> "Location"
            isValidPhoneNumber(resultText) -> "Phone Number"
            isValidSMS(resultText) -> "SMS"
            isValidVCard(resultText) -> "Contact Info (vCard)"
            isValidEvent(resultText) -> "Event Info"
            else -> "Text"
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

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$")
        return pattern.matcher(email).matches()
    }

    private fun isValidWifi(wifi: String): Boolean {
        val pattern = Pattern.compile("^WIFI:T:(WPA|WEP);S:([^;]+);P:([^;]+);;$")
        return pattern.matcher(wifi).matches()
    }

    private fun isValidWhatsApp(whatsapp: String): Boolean {
        val pattern = Pattern.compile("^https://wa.me/\\d+$")
        return pattern.matcher(whatsapp).matches()
    }

    private fun isValidGeoLocation(geo: String): Boolean {
        val pattern = Pattern.compile("^geo:-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?$")
        return pattern.matcher(geo).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val pattern = Pattern.compile("^tel:\\+?\\d+$")
        return pattern.matcher(phone).matches()
    }

    private fun isValidSMS(sms: String): Boolean {
        val pattern = Pattern.compile("^SMSTO:\\+?\\d+:.+$")
        return pattern.matcher(sms).matches()
    }

    private fun isValidVCard(vCard: String): Boolean {
        return vCard.startsWith("BEGIN:VCARD") && vCard.endsWith("END:VCARD")
    }

    private fun isValidEvent(event: String): Boolean {
        return event.startsWith("BEGIN:EVENT") && event.endsWith("END:EVENT")
    }
}
