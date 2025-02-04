package com.example.qrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcode.R
import com.example.qrcode.adapter.HistoryAdapter
import com.example.qrcode.list.history_List
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream

/**
 * Activity permettant d'afficher l'historique des QR codes scannés ou créés.
 * L'utilisateur peut visualiser les détails des QR codes, les supprimer ou lancer un nouveau scan.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var historyList: RecyclerView // RecyclerView pour afficher la liste de l'historique
    private lateinit var noDataText: TextView // Message affiché en l'absence de données
    private lateinit var openScanButton: Button // Bouton pour ouvrir l'activité de scan
    private lateinit var clear: ImageView // Bouton pour effacer l'historique
    private lateinit var adapter: HistoryAdapter // Adaptateur pour gérer l'affichage des éléments de l'historique

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les vues, configure la RecyclerView et les actions des boutons.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialisation des vues
        historyList = findViewById(R.id.history_list)
        noDataText = findViewById(R.id.noData)
        openScanButton = findViewById(R.id.OpenScan)
        clear = findViewById(R.id.clear)

        // Configuration de la RecyclerView
        historyList.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(this, history_List) { selectedItem ->
<<<<<<< HEAD
            // Ouvrir l'activité des détails du QR code sélectionné
            val intent = Intent(this, SelectedItemActivity::class.java)
            intent.putExtra("scanImage", selectedItem.qrCode.toString())
=======
            val bitmap=selectedItem.qrCode
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val intent = Intent(this, SelectedItemActivity::class.java)
            intent.putExtra("scanImage", byteArray)
>>>>>>> ca0dc5e7a4991de1dff7b3dc8fccfd876888c525
            intent.putExtra("scanText", selectedItem.textQr)
            intent.putExtra("scanType", selectedItem.type)
            startActivity(intent)
        }
        historyList.adapter = adapter

        // Mise à jour de la vue en fonction de la présence de données
        updateRecyclerView()

        // Action pour effacer l'historique
        clear.setOnClickListener {
            history_List.clear()
            adapter.notifyDataSetChanged()
            updateRecyclerView()
        }

        // Action pour ouvrir l'activité de scan
        openScanButton.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }

        // Configuration de la navigation inférieure
        setupBottomNavigation()
    }

    /**
     * Met à jour la visibilité des éléments de l'interface en fonction de la présence de données dans l'historique.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView() {
        if (history_List.isEmpty()) {
            historyList.visibility = View.GONE
            noDataText.visibility = View.VISIBLE
            openScanButton.visibility = View.VISIBLE
            clear.visibility = View.GONE
        } else {
            historyList.visibility = View.VISIBLE
            noDataText.visibility = View.GONE
            openScanButton.visibility = View.GONE
            clear.visibility = View.VISIBLE
        }
    }

    /**
     * Configure la barre de navigation inférieure pour permettre la navigation entre les différentes activités.
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.historic

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scan -> {
                    startActivity(Intent(this, ScannerActivity::class.java))
                    finish()
                    true
                }
                R.id.historic -> true // Reste sur la même activité
                R.id.create -> {
                    startActivity(Intent(this, CreateActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}