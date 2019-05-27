package com.example.coursecatalog.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecatalog.database.CourseEntity
import com.example.coursecatalog.databinding.ListItemCourseBinding

class CourseAdapter(val clickListener: CourseListener): androidx.recyclerview.widget.ListAdapter<CourseEntity, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    // tells recyclerview how to getCourse the viewholder for the course list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder.from(parent)
    }

    // bind ClickListener to itemlist
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item!!)
    }

    /**
     * viewholder for course list item
     * recyclerview uses this to refer to the item
     */
    class CourseViewHolder private constructor(val binding: ListItemCourseBinding) : RecyclerView.ViewHolder(binding.root) {

        // bind views in list item fragment
        fun bind(clickListener: CourseListener, item: CourseEntity) {
            binding.course = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        // gets and returns viewholder object with bindings
        companion object {
            fun from(parent: ViewGroup): CourseViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCourseBinding
                    .inflate(layoutInflater, parent, false)
                return CourseViewHolder(binding)
            }
        }

    }

    /**
     * helps recyclerview tell what items have changed so it can efficiently
     * manage items without having to redraw the entire list
     */
    class CourseDiffCallback :

    // tells the DiffUtil how to deal with CourseEntities
        DiffUtil.ItemCallback<CourseEntity>() {

        // checks whether a course has changed
        override fun areContentsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
            return oldItem == newItem
        }

        // checks whether two courses are the same entry in the database
        override fun areItemsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
            return oldItem.courseId == newItem.courseId
        }
    }

    // ClickListener for course list object
    class CourseListener(val clickListener: (courseId: Long) -> Unit) {
        fun onClick(course: CourseEntity) = clickListener(course.courseId)
    }



}