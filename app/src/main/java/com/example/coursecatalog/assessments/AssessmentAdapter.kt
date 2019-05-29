package com.example.coursecatalog.assessments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.database.Assessment
import com.example.coursecatalog.databinding.ListItemAssessmentBinding

class AssessmentAdapter(val clickListener: AssessmentListener): androidx.recyclerview.widget.ListAdapter<Assessment, AssessmentAdapter.AssessmentViewHolder>(AssessmentDiffCallback()) {

    // tells recyclerview how to getAssessment the viewholder for the assessment list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssessmentViewHolder {
        return AssessmentViewHolder.from(parent)
    }

    // bind ClickListener to itemlist
    override fun onBindViewHolder(holder: AssessmentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item!!)
    }

    /**
     * viewholder for assessment list item
     * recyclerview uses this to refer to the item
     */
    class AssessmentViewHolder private constructor(val binding: ListItemAssessmentBinding) : RecyclerView.ViewHolder(binding.root) {

        // bind views in list item fragment
        fun bind(clickListener: AssessmentListener, item: Assessment) {
            binding.assessment = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        // gets and returns viewholder object with bindings
        companion object {
            fun from(parent: ViewGroup): AssessmentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAssessmentBinding
                    .inflate(layoutInflater, parent, false)
                return AssessmentViewHolder(binding)
            }
        }

    }

    /**
     * helps recyclerview tell what items have changed so it can efficiently
     * manage items without having to redraw the entire list
     */
    class AssessmentDiffCallback :

    // tells the DiffUtil how to deal with AssessmentEntities
        DiffUtil.ItemCallback<Assessment>() {

        // checks whether a assessment has changed
        override fun areContentsTheSame(oldItem: Assessment, newItem: Assessment): Boolean {
            return oldItem == newItem
        }

        // checks whether two assessments are the same entry in the database
        override fun areItemsTheSame(oldItem: Assessment, newItem: Assessment): Boolean {
            return oldItem.assessmentId == newItem.assessmentId
        }
    }

    // ClickListener for assessment list object
    class AssessmentListener(val clickListener: (assessmentId: Long) -> Unit) {
        fun onClick(assessment: Assessment) = clickListener(assessment.assessmentId)
    }



}