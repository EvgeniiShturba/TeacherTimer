package com.shturba.teachertimer.ui.help

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.shturba.teachertimer.databinding.FragmentHelpBinding

class HelpDialogFragment : DialogFragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = """<h1>How to use this App</h1><p>This App is designed to help teachers make better use of time in the classroom. The app allows its users to monitor the amount of time that is spent on each activity during the lesson.</p><p>Register to get started; Use whatever name you feel comfortable with. At the beginning of the lesson, click the start button. The countdown timer will start and the program will start running. There will be five activities presented on the screen in the format of an icon. Click on an icon of your choice to switch between different activities.</p><p>The choice of activities is the following: <ul><li><b>Administrative</b>: Activities not related to teaching. For example, if attendance needs to be taken during the lesson, equipment needs to be set up, experiment needs to be prepared etc.</li><li><b>New Material</b>: Teaching new material.</li><li><b>Practicing the material</b>: Repeating or practicing what was previously learned, for example: solving problems using a formula learned during one of the lessons.</li><li><b>Testing</b>: Testing knowledge. For example: using the already learned and practiced vocabulary.</li><li><b>Communication</b>: Communication with students not related to teaching. For example: one of the students is not feeling well and the nurse and/or the student's parents need to be contacted, the teacher needs time to calm down the said student.</li></ul></p><p>At the end of the timer, the program will create a graph that shows how much time was spent on each of the actions during the lesson. A graph representing the entire time that the program was used will also be displayed.</p>"""
        binding.helpText.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

        binding.buttonClose.setOnClickListener {
            this.dismissNow()
        }
    }

    override fun onResume() {
        dialog?.window?.let { window ->
            val params: ViewGroup.LayoutParams = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params as WindowManager.LayoutParams
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HelpDialogFragment"
    }
}
