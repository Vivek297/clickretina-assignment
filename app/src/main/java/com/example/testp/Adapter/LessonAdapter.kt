package com.example.testp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testp.Models.Lesson
import com.example.testp.databinding.ItemLessonBinding

class LessonAdapter(
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    private val lessonList = ArrayList<Lesson>()


    inner class ViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = lessonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lesson = lessonList[position]

        holder.binding.tvLessonTitle.text = lesson.title

        holder.binding.tvLessonDuration.text =
            "${lesson.durationMinutes} min"

//        holder.binding.tvFreeBadge.text = lesson.isFree.toString()
        if(lesson.isFree == true){
            holder.binding.tvFreeBadge.text = "Free"
        }
        if(lesson.isFree == false){
            holder.binding.tvFreeBadge.text = "Paid"
        }




        holder.itemView.setOnClickListener {
            onClick(lesson)
        }

    }

    fun submitList(list: List<Lesson>) {
        lessonList.clear()
        lessonList.addAll(list)
        notifyDataSetChanged()
    }

}