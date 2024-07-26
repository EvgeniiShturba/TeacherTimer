package com.shturba.teachertimer.ui.lesson

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StopLessonDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        MaterialAlertDialogBuilder(it)
            .setMessage("Are you sure you want to stop the lesson?")
            .setPositiveButton("Stop") { _, _ ->
                (parentFragment as? AlertDialogListener)?.onDialogPositiveClick()
            }.setNegativeButton("Cancel") { _, _ ->
                (parentFragment as? AlertDialogListener)?.onDialogNegativeClick()
            }.create()
    } ?: throw IllegalStateException("Activity cannot be null")

    companion object {
        const val TAG = "StopLessonDialogFragment"
    }
}
