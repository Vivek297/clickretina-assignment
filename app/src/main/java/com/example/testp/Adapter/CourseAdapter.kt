package com.example.testp.Adapter



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testp.databinding.ItemCourseBinding
import com.example.testp.Models.Course

class CourseAdapter(
    private val courseList: List<Course>,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {

        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        val course = courseList[position]

        with(holder.binding) {

            tvTitle.text = course.title

            tvThumbLabel.text = course.title

            tvLevel.text = course.level.uppercase()

            tvAuthor.text = course.instructor.name

            tvRating.text = course.rating.toString()

            tvDuration.text = "${course.durationHours}h"

            Glide.with(root.context)
                .load(course.thumbnailUrl)
                .into(imgThumbnail)

            root.setOnClickListener {
                onItemClick(course)
            }
        }
    }

    override fun getItemCount(): Int = courseList.size
}