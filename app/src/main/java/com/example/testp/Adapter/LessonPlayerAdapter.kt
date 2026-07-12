package com.example.testp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testp.Models.Lesson
import com.example.testp.databinding.ItemLessonPlayerBinding

class LessonPlayerAdapter(
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonPlayerAdapter.ViewHolder>() {

    private val lessonList = ArrayList<Lesson>()

    inner class ViewHolder(
        val binding: ItemLessonPlayerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemLessonPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = lessonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lesson = lessonList[position]

        with(holder.binding) {

            tvTitle.text = lesson.title

            tvSubtitle.text =
                "${lesson.durationMinutes} min"

            tvFreeBadge.visibility =
                if (lesson.isFree)
                    android.view.View.VISIBLE
                else
                    android.view.View.GONE

            root.setOnClickListener {
                onClick(lesson)
            }
        }
    }

    fun submitList(list: List<Lesson>) {
        lessonList.clear()
        lessonList.addAll(list)
        notifyDataSetChanged()
    }
}