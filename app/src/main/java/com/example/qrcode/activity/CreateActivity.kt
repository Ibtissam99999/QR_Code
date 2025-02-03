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

class CreateActivity : AppCompatActivity() {

    private lateinit var createList: RecyclerView
    private lateinit var noDataText: TextView
    private lateinit var clear: ImageView
    private lateinit var adapter: CreateAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        createList = findViewById(R.id.create_list)
        noDataText = findViewById(R.id.noData)
        clear = findViewById(R.id.clear)
        val createQrButton: View = findViewById(R.id.createqr)

        createList.layoutManager = LinearLayoutManager(this)
        adapter = CreateAdapter(this, create_List) { selectedItem ->
            val intent = Intent(this, SelectedItemActivity::class.java).apply {
                putExtra("scanImage", selectedItem.qrCode)
                putExtra("scanText", selectedItem.textQr)
                putExtra("scanType", selectedItem.type)
            }
            startActivity(intent)
        }
        createList.adapter = adapter

        updateVisibility()

        clear.setOnClickListener {
            create_List.clear()
            adapter.notifyDataSetChanged()
            updateVisibility()
        }

        createQrButton.setOnClickListener {
            startActivity(Intent(this, TypeCodeActivity::class.java))
        }

        setupBottomNavigation()
    }

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

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.create

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
                R.id.create -> true
                else -> false
            }
        }
    }
}
