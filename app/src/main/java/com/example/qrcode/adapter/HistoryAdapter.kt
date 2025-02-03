package com.example.qrcode.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcode.R
import com.example.qrcode.data.historyData

class HistoryAdapter(
    private val context: Context,
    private val historyDataList: MutableList<historyData>,
    private val onItemClick: (historyData) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>()
{

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qrImageView: ImageView = itemView.findViewById(R.id.qrImageView)
        val qrTypeTextView: TextView = itemView.findViewById(R.id.TextView)
        val qrContentTextView: TextView = itemView.findViewById(R.id.qrContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val qrData = historyDataList[position]

        holder.qrImageView.setImageBitmap(qrData.qrCode)

        holder.qrTypeTextView.text = qrData.type

        holder.qrContentTextView.text = qrData.textQr

        holder.itemView.setOnClickListener {
            onItemClick(qrData)
        }
    }

    override fun getItemCount(): Int = historyDataList.size
}
