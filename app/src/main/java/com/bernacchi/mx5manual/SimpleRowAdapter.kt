package com.bernacchi.mx5manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SimpleRowAdapter(
    private val rows: List<RowData>
) : RecyclerView.Adapter<SimpleRowAdapter.RowViewHolder>() {

    data class RowData(
        val title: String,
        val subtitle: String,
        val onClick: () -> Unit
    )

    class RowViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val title: android.widget.TextView = view.findViewById(R.id.rowTitle)
        val subtitle: android.widget.TextView = view.findViewById(R.id.rowSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return RowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val row = rows[position]
        holder.title.text = row.title
        holder.subtitle.text = row.subtitle
        holder.subtitle.visibility = if (row.subtitle.isBlank()) android.view.View.GONE else android.view.View.VISIBLE
        holder.itemView.setOnClickListener { row.onClick() }
    }

    override fun getItemCount(): Int = rows.size
}
