package com.example.testp.Fragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testp.Adapter.LessonAdapter
import com.example.testp.Adapter.LessonPlayerAdapter
import com.example.testp.Models.Course
import com.example.testp.Models.Lesson
import com.example.testp.R
import com.example.testp.ViewModel.HomeViewModel
import com.example.testp.databinding.FragmentLessonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var lessonAdapter: LessonPlayerAdapter

    private var courseId = ""
    private var lessonId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        courseId = arguments?.getString("courseId") ?: ""
        lessonId = arguments?.getString("lessonId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        observeLesson()

        clickListeners()
    }

    private fun setupRecycler() {

        lessonAdapter = LessonPlayerAdapter { lesson ->

            bindLesson(currentCourse!!, lesson)

            playVideo(lesson.videoUrl)
        }

        binding.rvLessons.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvLessons.adapter = lessonAdapter
    }

    private var currentCourse: Course? = null

    private fun observeLesson() {

        viewModel.courses.observe(viewLifecycleOwner) { response ->

            currentCourse = response.categories
                .flatMap { it.courses }
                .find { it.id == courseId }

            currentCourse?.let { course ->

                lessonAdapter.submitList(course.lessons)

                val lesson = course.lessons.find {
                    it.id == lessonId
                } ?: course.lessons.first()

                bindLesson(course, lesson)

                playVideo(lesson.videoUrl)
            }
        }
    }

    private fun bindLesson(
        course: Course,
        lesson: Lesson
    ) {

        binding.tvCourseName.text =
            "LESSON • ${course.title.uppercase()}"

        binding.tvLessonTitle.text =
            lesson.title

        binding.tvLessonDescription.text =
            lesson.content

        binding.tvCurrentTime.text = "00:00"

        binding.tvTotalTime.text =
            "${lesson.durationMinutes}:00"
    }

    private fun playVideo(url: String) {

        binding.videoView.setVideoURI(Uri.parse(url))

        binding.videoView.setOnPreparedListener {

            binding.btnPlayCenter.visibility = View.GONE

            binding.videoView.start()
        }

        binding.videoView.setOnCompletionListener {

            binding.btnPlayCenter.visibility = View.VISIBLE
        }
    }

    private fun clickListeners() {

        binding.btnPlayCenter.setOnClickListener {

            if (binding.videoView.isPlaying) {

                binding.videoView.pause()

                binding.btnPlayCenter.visibility = View.VISIBLE

            } else {

                binding.videoView.start()

                binding.btnPlayCenter.visibility = View.GONE
            }
        }

        binding.btnBack.setOnClickListener {

            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()

        if (binding.videoView.isPlaying)
            binding.videoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.videoView.stopPlayback()

        _binding = null
    }
}