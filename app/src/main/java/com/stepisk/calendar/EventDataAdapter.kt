package com.stepisk.calendar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stepisk.calendar.database.EventData
import kotlinx.android.synthetic.main.fragment_event.view.*
import java.util.*

class EventDataAdapter(
    private val context: Context
) : RecyclerView.Adapter<EventDataAdapter.DataViewHolder>() {

    private var values = mutableListOf<EventData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_event, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.setData(values[position])
    }

    override fun getItemCount(): Int = values.size

    fun setData(list: List<EventData>) {
        values.clear()
        values.addAll(list)
        notifyDataSetChanged()
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(eventData: EventData) {
            val time: String = MyDateFormat.stf.format(Date(eventData.timeInMillis))
            val titleText = "${eventData.title} ($time)"
            itemView.apply {
                title.text = titleText
                text.text = eventData.text
            }

            itemView.setOnClickListener {
                ContextCompat.startActivity(context, Intent(context, EventInfoActivity::class.java).apply {
                    putExtra("event_id", eventData.id)
                }, null)
            }
        }
    }
}