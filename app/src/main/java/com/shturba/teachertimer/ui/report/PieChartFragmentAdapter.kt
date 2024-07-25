package com.shturba.teachertimer.ui.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PieChartFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = PieChartFragment().also {
        it.arguments = Bundle().apply {
            putInt(PieChartFragment.ARG_CHART_TYPE, position + 1)
        }
    }
}
