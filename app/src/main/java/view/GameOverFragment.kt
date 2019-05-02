package view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import presenter.PlayerColor

class GameOverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_gameover, container, false)

        val bundle = this.arguments
        if(bundle != null){

            val playersNumber = bundle.getInt("playersNumber")
            val textViewGameOver: TextView = view.findViewById(hu.bme.aut.android.chainreaction.R.id.tvGameOver)
            val keyRoundsOfWinner = (playersNumber-1).toString()+"Rounds"
            textViewGameOver.text = getString(R.string.game_over_data, "Player", bundle.getInt(keyRoundsOfWinner))

            val chart: BarChart = view.findViewById(hu.bme.aut.android.chainreaction.R.id.timeChart)
            val timeData = ArrayList<BarEntry>()
            val setColors = ArrayList<Int>(playersNumber)

            chart.tag = "Avg. step time of Players"
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.setFitBars(true)
            chart.setDrawValueAboveBar(false)
            chart.setScaleEnabled(false)
            chart.setDrawGridBackground(false)
            chart.legend.isWordWrapEnabled = true
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.setDrawGridLines(false)
            chart.xAxis.setDrawGridLines(false)
            chart.axisLeft.textColor = resources.getColor(R.color.colorMessage)
            chart.axisLeft.textSize = 12.0F
            chart.axisRight.textColor = resources.getColor(R.color.colorMessage)
            chart.axisRight.textSize = 12.0F
            chart.xAxis.textColor = resources.getColor(R.color.colorMessage)
            chart.xAxis.textSize = 12.0F
            chart.xAxis.isEnabled = false

            for (i in 1..playersNumber) {
                val keyAvgStepTime = (i-1).toString()+"AvgStepTime"
                val keyId = (i-1).toString()+"Id"
                timeData.add(BarEntry((i-1).toFloat(), bundle.getInt(keyAvgStepTime).toFloat()))
                setColors.add((i-1), PlayerColor.GetColorById(bundle.getInt(keyId)))
            }

            val set = BarDataSet(timeData, "")
            set.colors = setColors
            val dataSet = BarData(set)
            chart.data = dataSet
            chart.invalidate()

        }

        return view

    }

}