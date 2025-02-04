package com.example.qrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcode.R
import com.example.qrcode.adapter.CreateAdapter
import com.example.qrcode.list.create_List
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activity responsable de l'affichage et de la gestion des QR codes créés par l'utilisateur.
 * Elle permet également la navigation entre différentes sections de l'application via un menu de navigation inférieur.
 */
class CreateActivity : AppCompatActivity() {

    private lateinit var createList: RecyclerView // Liste des QR codes créés
    private lateinit var noDataText: TextView // Texte affiché en cas d'absence de données
    private lateinit var clear: ImageView // Bouton pour effacer la liste des QR codes
    private lateinit var adapter: CreateAdapter // Adaptateur pour gérer l'affichage des QR codes

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise les vues et configure les actions des boutons.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        // Initialisation des vues
        createList = findViewById(R.id.create_list)
        noDataText = findViewById(R.id.noData)
        clear = findViewById(R.id.clear)
        val createQrButton: View = findViewById(R.id.createqr)

        // Configuration du RecyclerView avec un LinearLayoutManager
        createList.layoutManager = LinearLayoutManager(this)
        adapter = CreateAdapter(this, create_List) { selectedItem ->
            // Démarrage de SelectedItemActivity avec les données du QR code sélectionné
            val intent = Intent(this, SelectedItemActivity::class.java).apply {
                putExtra("scanImage", selectedItem.qrCode)
                putExtra("scanText", selectedItem.textQr)
                putExtra("scanType", selectedItem.type)
            }
            startActivity(intent)
        }
        createList.adapter = adapter

        updateVisibility() // Mise à jour de la visibilité des vues en fonction des données

        // Action pour le bouton d'effacement de la liste des QR codes
        clear.setOnClickListener {
            create_List.clear()
            adapter.notifyDataSetChanged()
            updateVisibility()
        }

        // Action pour le bouton de création d'un nouveau QR code
        createQrButton.setOnClickListener {
            startActivity(Intent(this, TypeCodeActivity::class.java))
        }

        setupBottomNavigation() // Configuration de la navigation inférieure
    }

    /**
     * Met à jour la visibilité des éléments de l'interface en fonction de la présence de données.
     */
    private fun updateVisibility() {
        if (create_List.isEmpty()) {
            noDataText.visibility = View.VISIBLE
            createList.visibility = View.GONE
            clear.visibility = View.GONE
        } else {
            noDataText.visibility = View.GONE
            createList.visibility = View.VISIBLE
            clear.visibility = View.VISIBLE
        }
    }

    /**
     * Configure la barre de navigation inférieure pour permettre la navigation entre les différentes activités.
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.create // Sélection de l'élément actuel

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scan -> {
                    startActivity(Intent(this, ScannerActivity::class.java))
                    finish()
                    true
                }
                R.id.historic -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.create -> true // L'activité actuelle
                else -> false
            }
        }
    }
}
