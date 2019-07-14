package view

import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.HorizontalBarChart
import hu.bme.aut.android.chain_reaction.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import model.db.challenge.ChallengeDatabase
import model.db.challenge.ChallengeLevel

class ChallengeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)
        challengeDatabaseReader(view)
        return view
    }

    /**
     * Reads and displays challenge database
     *
     * @param     view      The view where data must be displayed if the database reading succeed
     */
    private fun challengeDatabaseReader(view: View){

        val dbChallenge = Room.databaseBuilder(activity!!.applicationContext, ChallengeDatabase::class.java, "db_challenge").build()
        var challengeLevels: MutableList<ChallengeLevel>

        Thread {

            challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()

            activity!!.runOnUiThread {

                val textViewAllLevels = view.findViewById<TextView>(R.id.textViewAllLevels)
                val textViewCompletedLevels = view.findViewById<TextView>(R.id.textViewCompletedLevels)
                val textViewIsAllLevelsCompleted = view.findViewById<TextView>(R.id.textViewIsAllLevelsCompleted)

                if(challengeLevels.isEmpty()){
                    textViewAllLevels.text = getString(R.string.empty_challenge_db)
                    textViewCompletedLevels.text = ""
                    textViewIsAllLevelsCompleted.text = ""
                }

                else{

                    var levelsCompleted = 0
                    var levelsLeft = 0

                    for (level in challengeLevels){

                        if(level.Completed){
                            levelsCompleted++
                        }
                        else{
                            levelsLeft++
                        }

                    }

                    textViewAllLevels.text = getString(R.string.all_levels_available, levelsCompleted+levelsLeft)
                    textViewCompletedLevels.text = getString(R.string.levels_completed_left, levelsCompleted, levelsLeft)

                    if(levelsLeft == 0){
                        textViewIsAllLevelsCompleted.text = getString(R.string.all_levels_completed)
                    }
                    else{
                        textViewIsAllLevelsCompleted.text = ""
                    }

                    displayChallengeChart(view, levelsCompleted, levelsLeft)
                }

            }

            dbChallenge.close()

        }.start()

    }

    /**
     * Shows the challenge chart
     *
     * @param     view              The view containing the chart
     * @param     levelsCompleted   Levels completed
     * @param     levelsLeft        Levels left
     */
    private fun displayChallengeChart(view: View, levelsCompleted: Int, levelsLeft: Int){

        val chart: HorizontalBarChart = view.findViewById(R.id.challengeChart)
        val levelData = ArrayList<BarEntry>(1)
        val setColor = ArrayList<Int>(1)

        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        chart.setExtraOffsets(5.toFloat(), 10.toFloat(), 5.toFloat(), 5.toFloat())
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.isHighlightPerTapEnabled = true
        chart.animateY(1000, Easing.EaseInOutQuad)
        chart.setPinchZoom(false)
        chart.setScaleEnabled(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.setFitBars(true)

        chart.axisRight.textColor = resources.getColor(R.color.colorMessage)
        chart.axisRight.axisMinimum = 0f
        chart.axisRight.axisMaximum = (levelsCompleted+levelsLeft).toFloat()

        chart.axisLeft.setDrawLabels(false)
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = (levelsCompleted+levelsLeft).toFloat()

        chart.xAxis.setDrawLabels(false)

        val legend = chart.legend
        legend.isEnabled = true
        legend.isWordWrapEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 5f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
        legend.textColor = resources.getColor(R.color.colorMessage)
        legend.textSize = 12f
        legend.formSize = 10f
        legend.form = Legend.LegendForm.SQUARE

        levelData.add(BarEntry(levelsCompleted.toFloat(), (levelsCompleted).toFloat()))
        setColor.add(Color.GRAY)

        val set = BarDataSet(levelData, getString(R.string.levels_completed_chart_label))
        set.colors = setColor

        val dataLabels = ArrayList<String>()
        dataLabels.add(getString(R.string.levels_completed_chart_label))

        val dataSet = BarData(set)
        dataSet.setValueTextColor(resources.getColor(R.color.colorMessage))
        dataSet.setValueTextSize(12f)

        chart.data = dataSet
        chart.invalidate()

    }

}