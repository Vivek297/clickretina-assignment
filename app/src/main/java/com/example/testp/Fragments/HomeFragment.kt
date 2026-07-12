package com.example.testp.Fragments
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testp.Adapter.CategoryAdapter
import com.example.testp.Adapter.CourseAdapter
import com.example.testp.Models.Category
import com.example.testp.Models.Instructor
import com.example.testp.Models.Lesson
import com.example.testp.R
import com.example.testp.databinding.FragmentHomeBinding
import com.example.testp.Models.Course
import com.example.testp.ViewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var courseAdapter: CourseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)


        setupRecyclerViews()

        observeData()

        viewModel.getCourses()
    }

    private fun setupRecyclerViews() {

        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvCourses.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun observeData() {

        viewModel.courses.observe(viewLifecycleOwner) { response ->

            categoryAdapter = CategoryAdapter(response.categories) { category ->

                val firstCourse = category.courses.firstOrNull() ?: return@CategoryAdapter

                val bundle = Bundle().apply {
                    putString("courseId", firstCourse.id)
                }

                findNavController().navigate(
                    R.id.action_homeFragment_to_courseFragment,
                    bundle
                )
            }

            binding.rvCategories.adapter = categoryAdapter

            val allCourses = response.categories.flatMap { it.courses }

            courseAdapter = CourseAdapter(allCourses) { course ->

                val bundle = Bundle().apply {
                    putString("courseId", course.id)
                }

                findNavController().navigate(
                    R.id.action_homeFragment_to_courseFragment,
                    bundle
                )
            }

            binding.rvCourses.adapter = courseAdapter
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->

            // Show or hide ProgressBar
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->

            // Show Toast or Snackbar
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}