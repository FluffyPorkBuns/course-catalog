package com.example.coursecatalog.terms

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.R
import com.example.coursecatalog.database.TermEntity

class TermAdapter: RecyclerView.Adapter<TermAdapter.TermViewHolder>() {

    var data = listOf<TermEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_term, parent, false) as TextView
        return TermViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.termTitle
    }

    inner class TermViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

}