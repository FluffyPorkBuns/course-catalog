package com.example.coursecatalog.terms

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.R
import com.example.coursecatalog.database.TermEntity
import com.example.coursecatalog.util.formatDate
import kotlinx.android.synthetic.main.list_item_term.view.*

class TermAdapter: RecyclerView.Adapter<TermAdapter.TermViewHolder>() {

    var data = listOf<TermEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_term, parent, false)
        return TermViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class TermViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val res = itemView.context.resources

        val title: TextView = itemView.title_text
        val start: TextView = itemView.start_text
        val end: TextView = itemView.end_text

        fun bind(item: TermEntity) {
            this.title.text = item.termTitle
            this.start.text = formatDate(item.startDate)
            this.end.text = formatDate(item.endDate)
        }

    }

}