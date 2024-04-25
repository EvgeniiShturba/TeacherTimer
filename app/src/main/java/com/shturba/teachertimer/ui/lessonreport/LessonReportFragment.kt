package com.shturba.teachertimer.ui.lessonreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shturba.teachertimer.R
import com.shturba.teachertimer.databinding.FragmentLessonReportBinding

class LessonReportFragment : Fragment() {

    private var _binding: FragmentLessonReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLessonReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStartLesson.setOnClickListener {
            findNavController().navigate(R.id.action_lessonReportFragment_to_timerFragment)
        }
        binding.buttonHelp.setOnClickListener {
            findNavController().navigate(R.id.action_lessonReportFragment_to_helpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}