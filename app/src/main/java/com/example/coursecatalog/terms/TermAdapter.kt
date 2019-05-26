package com.example.coursecatalog.terms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.database.TermEntity
import com.example.coursecatalog.databinding.ListItemTermBinding

class TermAdapter(val clickListener: TermListener): androidx.recyclerview.widget.ListAdapter<TermEntity, TermAdapter.TermViewHolder>(TermDiffCallback()) {

    // tells recyclerview how to getTerm the viewholder for the term list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        return TermViewHolder.from(parent)
    }

    // bind ClickListener to itemlist
    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item!!)
    }

    /**
     * viewholder for term list item
     * recyclerview uses this to refer to the item
     */
    class TermViewHolder private constructor(val binding: ListItemTermBinding) : RecyclerView.ViewHolder(binding.root) {

        // bind views in list item fragment
        fun bind(clickListener: TermListener, item: TermEntity) {
            binding.term = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        // gets and returns viewholder object with bindings
        companion object {
            fun from(parent: ViewGroup): TermViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTermBinding
                    .inflate(layoutInflater, parent, false)
                return TermViewHolder(binding)
            }
        }

    }

    /**
     * helps recyclerview tell what items have changed so it can efficiently
     * manage items without having to redraw the entire list
     */
    class TermDiffCallback :

        // tells the DiffUtil how to deal with TermEntities
        DiffUtil.ItemCallback<TermEntity>() {

        // checks whether a term has changed
        override fun areContentsTheSame(oldItem: TermEntity, newItem: TermEntity): Boolean {
            return oldItem == newItem
        }

        // checks whether two terms are the same entry in the database
        override fun areItemsTheSame(oldItem: TermEntity, newItem: TermEntity): Boolean {
            return oldItem.termId == newItem.termId
        }
    }

    // ClickListener for term list object
    class TermListener(val clickListener: (termId: Long) -> Unit) {
        fun onClick(term: TermEntity) = clickListener(term.termId)
    }



}