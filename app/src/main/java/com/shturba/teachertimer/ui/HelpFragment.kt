package com.shturba.teachertimer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shturba.teachertimer.R
import com.shturba.teachertimer.databinding.FragmentHelpBinding
import com.shturba.teachertimer.databinding.FragmentLessonReportBinding
import com.shturba.teachertimer.databinding.FragmentTimerBinding
import com.shturba.teachertimer.ui.timer.TimerViewModel

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root


        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

