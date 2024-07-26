package com.shturba.teachertimer.ui.lesson

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shturba.teachertimer.R
import com.shturba.teachertimer.databinding.FragmentLessonBinding
import com.shturba.teachertimer.ui.help.HelpDialogFragment
import com.shturba.teachertimer.utils.FragmentOnBackPressedCallback

class LessonFragment : Fragment(), AlertDialogListener {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LessonViewModel by viewModels()
    private lateinit var activitiesToCards: Map<LessonActivity, CardView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            FragmentOnBackPressedCallback(this),
        )

        activitiesToCards = mapOf(
            LessonActivity.ADMINISTRATIVE to binding.cardAdmin,
            LessonActivity.NEW_MATERIAL to binding.cardNewMaterial,
            LessonActivity.CHECKING to binding.cardChecking,
            LessonActivity.TESTING to binding.cardTesting,
            LessonActivity.COMMUNICATION to binding.cardCommunication,
        )

        activitiesToCards.forEach { entry ->
            entry.value.setOnClickListener { viewModel.changeActivity(entry.key) }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.isLessonFinished) {
                findNavController().popBackStack()
            }
            binding.textTimer.text = uiState.timerValue
            setActiveCard(uiState.currentActivity)
        }

        setupToolbar()
    }

    private fun setActiveCard(newActivity: LessonActivity) {
        activitiesToCards.values.onEach {
            it.setCardBackgroundColor(Color.parseColor("#E6E6E6"))
            it.elevation = 8f
        }
        activitiesToCards[newActivity]?.let {
            it.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            it.elevation = 16f
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_help -> {
                    if (childFragmentManager.findFragmentByTag(HelpDialogFragment.TAG) == null) {
                        HelpDialogFragment().show(
                            childFragmentManager,
                            HelpDialogFragment.TAG,
                        )
                    }
                    true
                }

                R.id.action_stop_lesson -> {
                    showStopLessonDialog()
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showStopLessonDialog() {
        StopLessonDialogFragment().show(childFragmentManager, StopLessonDialogFragment.TAG)
    }

    override fun onDialogPositiveClick() {
        viewModel.stop()
    }

    override fun onDialogNegativeClick() {
        // Do nothing
    }
}
