package com.shturba.teachertimer.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.shturba.teachertimer.R
import com.shturba.teachertimer.databinding.FragmentReportBinding

class ReportFragment : Fragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = PieChartFragmentAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = PieChartType.from(position + 1)?.title ?: "Unknown"
        }.attach()

        binding.buttonStartLesson.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(CountdownDialogFragment.TAG) == null) {
                CountdownDialogFragment().show(
                    childFragmentManager,
                    CountdownDialogFragment.TAG,
                )
            }
            viewModel.startCountdown()
        }
        viewModel.isLessonStarted.observe(viewLifecycleOwner) {
            if (it) {
                childFragmentManager.findFragmentByTag(CountdownDialogFragment.TAG)?.let { df ->
                    (df as? DialogFragment)?.dismiss()
                }
                findNavController().navigate(R.id.action_reportFragment_to_timerFragment)
                viewModel.isLessonStartedHandled()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
