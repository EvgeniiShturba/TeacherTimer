package com.shturba.teachertimer.ui.report

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.shturba.teachertimer.databinding.FragmentPieChartBinding
import com.shturba.teachertimer.ui.lesson.LessonActivity

class PieChartFragment : Fragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentPieChartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels({ requireParentFragment() })
    private var pieChartType: PieChartType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPieChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_CHART_TYPE) }?.apply {
            pieChartType = PieChartType.from(getInt(ARG_CHART_TYPE))
        }
        when (pieChartType) {
            PieChartType.LAST_LESSON -> {
                viewModel.lastLessonData.observe(viewLifecycleOwner) {
                    it?.let { createPieChart(it, binding.pieChart) }
                }
            }

            PieChartType.ALL_LESSONS -> {
                viewModel.allLessonsData.observe(viewLifecycleOwner) {
                    it?.let { createPieChart(it, binding.pieChart) }
                }
            }

            null -> { /* throw an Exception? */ }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createPieChart(data: Map<LessonActivity, Int>, pieChart: PieChart) {
        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting hole
        pieChart.isDrawHoleEnabled = false

        // on below line we are setting center text
        pieChart.setDrawCenterText(false)

        // disable rotation of the pieChart by touch
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = false

        // on below line we are enabling our legend for pie chart
        pieChart.legend.isEnabled = true
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries = data.toList().map { PieEntry(it.second.toFloat(), it.first.name) }

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // on below line we are setting colors.
        dataSet.colors = listOf(Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW)

        // on below line we are setting pie data set
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(15f)
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD)
        pieData.setValueTextColor(Color.WHITE)
        pieChart.setData(pieData)

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    companion object {
        const val ARG_CHART_TYPE = "chart_type"
    }
}
