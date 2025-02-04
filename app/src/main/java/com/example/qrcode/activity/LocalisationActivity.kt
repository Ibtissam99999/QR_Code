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
 * Activity permettant de générer un QR code à partir des coordonnées de localisation (latitude et longitude).
 * L'utilisateur entre les coordonnées, et un QR code est généré pour un lien de géolocalisation.
 */
class LocalisationActivity : AppCompatActivity() {
    private lateinit var ivQRCode: ImageView // ImageView pour afficher le QR code généré
    private lateinit var etLongitude: EditText // Champ de saisie pour la longitude
    private lateinit var etLatitude: EditText // Champ de saisie pour la latitude
    private lateinit var btnCreateQR: Button // Bouton pour générer le QR code
    private lateinit var menuButton: ImageView // Bouton pour revenir au menu de sélection des types de QR code

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les vues et configure les actions des boutons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)

        // Initialisation des vues
        ivQRCode = findViewById(R.id.ivQRCode)
        etLongitude = findViewById(R.id.longtitude)
        etLatitude = findViewById(R.id.latitude)
        btnCreateQR = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        // Action pour revenir au menu des types de QR code
        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        // Action pour générer un QR code à partir des coordonnées saisies
        btnCreateQR.setOnClickListener {
            val longitude = etLongitude.text.toString()
            val latitude = etLatitude.text.toString()

            // Vérifie que les champs ne sont pas vides
            if (longitude.isEmpty() || latitude.isEmpty()) {
                Toast.makeText(this, "Please enter both longitude and latitude", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val geoUri = "geo:$latitude,$longitude" // Format URI pour la géolocalisation

            val writer = QRCodeWriter()
            try {
                // Génération du QR code
                val bitMatrix = writer.encode(geoUri, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                // Remplissage des pixels du QR code (noir et blanc)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }

                // Affichage du QR code généré
                ivQRCode.setImageBitmap(bmp)
                ivQRCode.visibility = ImageView.VISIBLE

                // Ajout des données du QR code à la liste des créations
                val qrCodeData = createData(bmp, "LOCALISATION", geoUri)
                create_List.add(qrCodeData)

            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }
}
