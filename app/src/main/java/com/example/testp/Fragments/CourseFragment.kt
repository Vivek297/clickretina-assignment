package com.example.testp.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.testp.Adapter.LessonAdapter
import com.example.testp.Adapter.TagAdapter
import com.example.testp.R
import com.example.testp.ViewModel.HomeViewModel
import com.example.testp.databinding.FragmentCourseBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var tagAdapter: TagAdapter

    private var courseId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        courseId = arguments?.getString("courseId") ?: ""

        android.util.Log.d("CourseFragment", "courseId = $courseId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCourseBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        viewModel.getCourses()
        observeCourse()

        clickListeners()
    }

    private fun setupRecyclerViews() {

        lessonAdapter = LessonAdapter { lesson ->

            if (lesson.isFree) {

                val bundle = Bundle().apply {
                    putString("courseId", courseId)
                    putString("lessonId", lesson.id)
                }

                findNavController().navigate(
                    R.id.action_courseFragment_to_lessonFragment,
                    bundle
                )

            } else {

                Snackbar.make(
                    binding.root,
                    "You have to enroll first.",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Enroll") {
                        // Handle enroll action
                    }
                    .show()

            }
        }

        binding.rvLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lessonAdapter
        }

        tagAdapter = TagAdapter()

        binding.rvTags.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = tagAdapter
        }

    }

    private fun observeCourse() {

        viewModel.courses.observe(viewLifecycleOwner) { response ->

            android.util.Log.d("CourseFragment", "Response received")

            val allCourses = response.categories.flatMap { it.courses }

            android.util.Log.d("CourseFragment", "Courses = ${allCourses.size}")

            val course = allCourses.find { it.id == courseId }

            android.util.Log.d("CourseFragment", "Found course = $course")

            course?.let {
                bindCourse(it)
            }
        }

    }

    private fun bindCourse(course: com.example.testp.Models.Course) {
        Log.d("CourseFragment", "Binding ${course.title}")

        binding.tvHeroTitle.text = course.title

        binding.tvHeroTag.text =
            if (course.tags.isNotEmpty()) "// ${course.tags[0]}"
            else "// Course"

        binding.tvCourseTitle.text = course.title

        binding.tvSubtitle.text = course.subtitle

        binding.tvDescription.text = course.description

        binding.tvRating.text = course.rating.toString()

        binding.tvStudents.text =
            "${course.studentsEnrolled} Students"

        binding.tvDuration.text =
            "${course.durationHours} hrs"

        binding.tvLevel.text = course.level

        binding.tvInstructorName.text =
            course.instructor.name

        binding.tvInstructorTitle.text =
            course.instructor.title

        binding.tvLessonCount.text =
            "${course.lessons.size} lessons"

        Glide.with(requireContext())
            .load(course.thumbnailUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.ivThumbnail)

        Glide.with(requireContext())
            .load(course.instructor.avatarUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .circleCrop()
            .into(binding.ivInstructor)

        lessonAdapter.submitList(course.lessons)

        tagAdapter.submitList(course.tags)

    }

    private fun clickListeners() {

        binding.btnEnroll.setOnClickListener {

            findNavController().navigate(R.id.lessonFragment)

        }

        binding.btnBack.setOnClickListener {

            findNavController().popBackStack()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}