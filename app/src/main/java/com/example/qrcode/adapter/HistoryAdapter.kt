package com.example.qrcode.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcode.R
import com.example.qrcode.data.historyData

/**
 * Adapter for displaying a list of QR codes in the history section.
 * Each item represents a previously scanned or created QR code, including its image, type, and content.
 *
 * @param context The context in which the adapter operates.
 * @param historyDataList A mutable list of [historyData] objects representing QR code history.
 * @param onItemClick A lambda function to handle item clicks with the [historyData] passed as an argument.
 */
class HistoryAdapter(
    private val context: Context,
    private val historyDataList: MutableList<historyData>,
    private val onItemClick: (historyData) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    /**
     * ViewHolder class for binding QR code history data to the item view.
     * This class is responsible for holding the views that will display the QR code image, type, and content.
     *
     * @param itemView The view representing each item in the RecyclerView.
     */
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ImageView for displaying the QR code image
        val qrImageView: ImageView = itemView.findViewById(R.id.qrImageView)

        // TextView for displaying the type of the QR code (e.g., URL, Email)
        val qrTypeTextView: TextView = itemView.findViewById(R.id.TextView)

        // TextView for displaying the content of the QR code
        val qrContentTextView: TextView = itemView.findViewById(R.id.qrContentTextView)
    }

    /**
     * Called when a new view holder is created. It inflates the layout for each item in the RecyclerView.
     *
     * @param parent The parent view group in which the new item view will be added.
     * @param viewType The view type for the new item view.
     * @return A new instance of [HistoryViewHolder] to be used for binding data.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    /**
     * Called to bind the data to the views within each view holder.
     * This sets the image, type, and content of the QR code.
     *
     * @param holder The view holder that will hold the views.
     * @param position The position of the current item in the list.
     */
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        // Get the current history QR code data item
        val qrData = historyDataList[position]

        // Set the QR code image
        holder.qrImageView.setImageBitmap(qrData.qrCode)

        // Set the QR code type (e.g., URL, Email)
        holder.qrTypeTextView.text = qrData.type

        // Set the QR code content
        holder.qrContentTextView.text = qrData.textQr

        // Set an onClickListener for the item to handle item clicks
        holder.itemView.setOnClickListener {
            onItemClick(qrData)
        }
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the [historyDataList].
     */
    override fun getItemCount(): Int = historyDataList.size
}
