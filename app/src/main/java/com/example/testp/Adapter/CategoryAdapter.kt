package com.example.testp.Adapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testp.databinding.ItemCategoryBinding
import com.example.testp.Models.Category

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categoryList[position]

        with(holder.binding) {

            tvCategoryTitle.text = category.name

            tvCategoryCount.text = "${category.courseCount} Courses"

            // Background color from API
            try {
                root.findViewById<android.widget.FrameLayout>(
                    com.example.testp.R.id.iconContainer
                )?.setBackgroundColor(Color.parseColor(category.iconColor))
            } catch (_: Exception) {
            }

            root.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size
}