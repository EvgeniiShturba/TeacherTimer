package com.shturba.teachertimer.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shturba.teachertimer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStart.setOnClickListener {
            viewModel.start()
            binding.buttonStart.visibility = View.GONE
        }
        binding.buttonAdmin.setOnClickListener {
            viewModel.changeActivity(LessonActivity.ADMINISTRATIVE)
        }
        binding.buttonNew.setOnClickListener {
            viewModel.changeActivity(LessonActivity.NEW_MATERIAL)
        }
        binding.buttonCheck.setOnClickListener {
            viewModel.changeActivity(LessonActivity.CHECKING)
        }
        binding.buttonTest.setOnClickListener {
            viewModel.changeActivity(LessonActivity.TESTING)
        }
        binding.buttonCommun.setOnClickListener {
            viewModel.changeActivity(LessonActivity.COMMUNICATION)
        }
        viewModel.timerValue.observe(viewLifecycleOwner) { timerValue ->
            binding.textTimer.text = timerValue
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
