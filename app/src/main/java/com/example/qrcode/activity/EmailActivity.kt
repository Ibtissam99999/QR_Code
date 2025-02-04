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
 * Activity permettant de générer un QR code à partir d'une adresse e-mail saisie par l'utilisateur.
 * Le QR code généré est affiché à l'écran et stocké dans une liste pour un usage ultérieur.
 */
class EmailActivity : AppCompatActivity() {

    private lateinit var ivQRCode: ImageView // ImageView pour afficher le QR code généré
    private lateinit var etData: EditText // Champ de saisie de l'adresse e-mail
    private lateinit var btnGenerateQRCode: Button // Bouton pour générer le QR code
    private lateinit var menuButton: ImageView // Bouton pour retourner au menu principal

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les vues et configure les actions des boutons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        // Initialisation des vues
        ivQRCode = findViewById(R.id.ivQRCode)
        etData = findViewById(R.id.edEmail)
        btnGenerateQRCode = findViewById(R.id.createQR)
        menuButton = findViewById(R.id.menu)

        // Action du bouton pour retourner au menu principal
        menuButton.setOnClickListener {
            val intent = Intent(this, TypeCodeActivity::class.java)
            startActivity(intent)
        }

        // Action du bouton pour générer un QR code
        btnGenerateQRCode.setOnClickListener {
            val data = etData.text.toString() // Récupération de l'adresse e-mail saisie

            if (data.isEmpty()) {
                // Vérification que le champ n'est pas vide
                Toast.makeText(this, "Enter some data", Toast.LENGTH_SHORT).show()
            } else {
                val writer = QRCodeWriter()
                try {
                    // Génération du QR code à partir des données saisies
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                    // Remplissage du bitmap avec les pixels du QR code
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }

                    // Affichage du QR code généré
                    ivQRCode.setImageBitmap(bmp)
                    ivQRCode.visibility = ImageView.VISIBLE

                    // Sauvegarde des données du QR code dans la liste
                    val qrCodeData = createData(bmp, "EMAIL", data)
                    create_List.add(qrCodeData)

                } catch (e: WriterException) {
                    // Gestion des erreurs lors de la génération du QR code
                    e.printStackTrace()
                }
            }
        }
    }
}