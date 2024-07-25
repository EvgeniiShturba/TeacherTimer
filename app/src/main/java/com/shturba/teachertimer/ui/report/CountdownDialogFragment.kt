package com.shturba.teachertimer.ui.report

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shturba.teachertimer.databinding.FragmentCountdownBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownDialogFragment : DialogFragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentCountdownBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels({ requireParentFragment() })
    private var job: Job? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentCountdownBinding.inflate(requireActivity().layoutInflater)
        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
            .also { dialog ->
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                dialog.setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && dialog.isShowing) {
                        cancelCountdown()
                    }
                    true
                }
            }
    }

    // Need to return the view here or onViewCreated won't be called by DialogFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCancel.setOnClickListener {
            cancelCountdown()
        }
        viewModel.countdown.observe(viewLifecycleOwner) {
            animateCountdown(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateCountdown(value: String) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            for (i in 1..16) {
                binding.textCountdown.textSize = 8f + i * 2
                binding.textCountdown.text = value
                delay(50)
            }
        }
    }

    private fun cancelCountdown() {
        viewModel.cancelCountdown()
        dismiss()
    }

    companion object {
        const val TAG = "CountdownDialogFragment"
    }
}
