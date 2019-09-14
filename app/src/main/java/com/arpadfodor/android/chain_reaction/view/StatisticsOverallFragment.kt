package com.arpadfodor.android.chain_reaction.view

import androidx.room.Room
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.arpadfodor.android.chain_reaction.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.arpadfodor.android.chain_reaction.model.db.stats.PlayerTypeStat
import com.arpadfodor.android.chain_reaction.model.db.stats.PlayerTypeStatsDatabase
import com.arpadfodor.android.chain_reaction.view.subclass.BaseFragment

class StatisticsOverallFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics_custom, container, false)
        statisticsDatabaseReader(view)
        return view
    }

    /**
     * Reads and displays statistics database
     *
     * @param     view      The hu.bme.aut.android.chain_reaction.view where the chart must be displayed if the database reading succeed
     */
    private fun statisticsDatabaseReader(view: View){

        val dbStats = Room.databaseBuilder(activity!!.applicationContext, PlayerTypeStatsDatabase::class.java, "db_stats").build()
        var playerTypeStats: MutableList<PlayerTypeStat>

        Thread {

            playerTypeStats = dbStats.playerTypeStatDAO().getAll().toMutableList()

            activity!!.runOnUiThread {

                val textViewAiVsHumanStats = view.findViewById<TextView>(R.id.textViewAiVsHumanStats)
                val textViewWinners = view.findViewById<TextView>(R.id.textViewWinningStats)
                val textViewAllStats = view.findViewById<TextView>(R.id.textViewAllStats)

                if(playerTypeStats.isEmpty()){
                    textViewAiVsHumanStats.text = getString(R.string.empty_db)
                    textViewWinners.text = ""
                    textViewAllStats.text = ""
                }

                else{
                    val humanVictories = playerTypeStats[0].NumberOfVictories
                    val aiVictories = playerTypeStats[1].NumberOfVictories
                    textViewAiVsHumanStats.text = getString(R.string.ai_vs_human_stats_data, humanVictories+aiVictories)
                    textViewWinners.text = getString(R.string.victory_data, humanVictories, aiVictories)
                    textViewAllStats.text = getString(R.string.all_stats_data, playerTypeStats[2].NumberOfVictories)
                    displayStatisticsChart(view, humanVictories, aiVictories)
                }

            }

            dbStats.close()

        }.start()

    }

    /**
     * Shows the statistics chart
     *
     * @param     view              The hu.bme.aut.android.chain_reaction.view containing the chart
     * @param     humanVictories    Number of human victories
     * @param     aiVictories       Number of AI victories
     */
    private fun displayStatisticsChart(view: View, humanVictories: Int, aiVictories: Int){

        val chart: PieChart = view.findViewById(R.id.statsChart)
        val victoryData = ArrayList<PieEntry>(2)
        val setColors = ArrayList<Int>(2)

        chart.tag = "AI vs human victories"
        chart.description.isEnabled = false
        chart.setUsePercentValues(true)
        chart.setExtraOffsets(5.toFloat(), 10.toFloat(), 5.toFloat(), 5.toFloat())
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.rotationAngle = 0.toFloat()
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.animateY(1000, Easing.EaseInOutQuad)

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(resources.getColor(R.color.colorTransparent))
        chart.setTransparentCircleColor(resources.getColor(R.color.colorTransparent))
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 60f

        val legend = chart.legend
        legend.isEnabled = true
        legend.isWordWrapEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 5f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
        legend.textColor = resources.getColor(R.color.colorMessage)
        legend.textSize = 12f
        legend.formSize = 10f
        legend.form = Legend.LegendForm.SQUARE

        victoryData.add(PieEntry(humanVictories.toFloat(), getString(R.string.type_human)))
        setColors.add(Color.GRAY)
        victoryData.add(PieEntry(aiVictories.toFloat(), getString(R.string.type_ai)))
        setColors.add(Color.BLACK)

        val set = PieDataSet(victoryData, getString(R.string.victories_chart_label))
        set.colors = setColors

        val dataLabels = ArrayList<String>()
        dataLabels.add(getString(R.string.type_human))
        dataLabels.add(getString(R.string.type_ai))

        val data = PieData(set)
        data.setValueTextColor(resources.getColor(R.color.colorMessage))
        data.setValueTextSize(12f)

        chart.data = data
        chart.invalidate()

    }

}