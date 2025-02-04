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

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyList: RecyclerView
    private lateinit var noDataText: TextView
    private lateinit var openScanButton: Button
    private lateinit var clear: ImageView
    private lateinit var adapter: HistoryAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyList = findViewById(R.id.history_list)
        noDataText = findViewById(R.id.noData)
        openScanButton = findViewById(R.id.OpenScan)
        clear = findViewById(R.id.clear)

        historyList.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(this, history_List) { selectedItem ->
            val bitmap=selectedItem.qrCode
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val intent = Intent(this, SelectedItemActivity::class.java)
            intent.putExtra("scanImage", byteArray)
            intent.putExtra("scanText", selectedItem.textQr)
            intent.putExtra("scanType", selectedItem.type)
            startActivity(intent)
        }
        historyList.adapter = adapter

        updateRecyclerView()

        clear.setOnClickListener {
            history_List.clear()
            adapter.notifyDataSetChanged()
            updateRecyclerView()
        }

        openScanButton.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }
        setupBottomNavigation()
    }

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
                R.id.historic -> true
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
