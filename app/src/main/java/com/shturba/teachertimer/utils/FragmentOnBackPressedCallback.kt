package com.shturba.teachertimer.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FragmentOnBackPressedCallback(
    private val fragment: Fragment,
) : OnBackPressedCallback(true) {

    override fun handleOnBackPressed() {
        // Do nothing to block the back button
    }
}
