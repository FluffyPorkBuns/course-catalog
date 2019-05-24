package com.example.coursecatalog.terms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.database.TermEntity
import com.example.coursecatalog.databinding.ListItemTermBinding
import com.example.coursecatalog.generated.callback.OnClickListener
import com.example.coursecatalog.util.formatDate

class TermAdapter(val clickListener: TermListener): androidx.recyclerview.widget.ListAdapter<TermEntity, TermAdapter.TermViewHolder>(TermDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        return TermViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,getItem(position)!!)
    }

    class TermViewHolder private constructor(val binding: ListItemTermBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: TermListener, item: TermEntity) {
            binding.term = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TermViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTermBinding
                    .inflate(layoutInflater, parent, false)
                return TermViewHolder(binding)
            }
        }

    }

    class TermDiffCallback :
            DiffUtil.ItemCallback<TermEntity>() {


        override fun areContentsTheSame(oldItem: TermEntity, newItem: TermEntity): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: TermEntity, newItem: TermEntity): Boolean {
            return oldItem.termId == newItem.termId
        }
    }

    class TermListener(val clickListener: (termId: Long) -> Unit) {
        fun onClick(term: TermEntity) = clickListener(term.termId)
    }



}