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
import com.example.qrcode.data.createData
import com.example.qrcode.data.historyData

class CreateAdapter(
    private val context: Context,
    private val CreateList: MutableList<createData>,
    private val onItemClick: (createData) -> Unit
) : RecyclerView.Adapter<CreateAdapter.CreateViewHolder>() {

    class CreateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qrImageView: ImageView = itemView.findViewById(R.id.qrImageView)
        val qrTypeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        val qrContentTextView: TextView = itemView.findViewById(R.id.TextViewRealiser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_qr_code, parent, false)
        return CreateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateViewHolder, position: Int) {
        val qrData = CreateList[position]

        holder.qrImageView.setImageBitmap(qrData.qrCode)

        holder.qrTypeTextView.text = qrData.type

        holder.qrContentTextView.text = qrData.textQr

        holder.itemView.setOnClickListener {
            onItemClick(qrData)
        }
    }

    override fun getItemCount(): Int = CreateList.size
}