package com.shturba.teachertimer.ui.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shturba.teachertimer.R
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
            binding.containerActivities.visibility = View.VISIBLE
            setClicked(binding.buttonAdmin)
        }
        binding.buttonAdmin.setOnClickListener {
            setClicked(it as ImageButton)
            viewModel.changeActivity(LessonActivity.ADMINISTRATIVE)
        }
        binding.buttonNewMaterial.setOnClickListener {
            setClicked(it as ImageButton)
            viewModel.changeActivity(LessonActivity.NEW_MATERIAL)
        }
        binding.buttonChecking.setOnClickListener {
            setClicked(it as ImageButton)
            viewModel.changeActivity(LessonActivity.CHECKING)
        }
        binding.buttonTesting.setOnClickListener {
            setClicked(it as ImageButton)
            viewModel.changeActivity(LessonActivity.TESTING)
        }
        binding.buttonCommunication.setOnClickListener {
            setClicked(it as ImageButton)
            viewModel.changeActivity(LessonActivity.COMMUNICATION)
        }
        viewModel.timerValue.observe(viewLifecycleOwner) { timerValue ->
            binding.textTimer.text = timerValue
        }
        binding.buttonHelp.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_helpFragment)
        }
        viewModel.lessonEnd.observe(viewLifecycleOwner) {
           if (it) {
               findNavController().popBackStack()
           }
        }
    }

    private fun setClicked(imageButton: ImageButton) {
        binding.containerActivities.children.toList().onEach {
            if (it is ImageButton) {
                it.background = null
                it.setPadding(0, 0, 0, 0)
            }
        }
        imageButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.border)
        imageButton.setPadding(15, 15, 15, 15)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
