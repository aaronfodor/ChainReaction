package view

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hu.bme.aut.android.chain_reaction.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import presenter.PlayerVisualRepresentation

class GameOverFragment : Fragment() {

    companion object {
        const val HUMAN = 1
        const val AI = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_over, container, false)

        val bundle = this.arguments
        if(bundle != null){

            val playersNumber = bundle.getInt("playersNumber")
            val winnerType = bundle.getInt((playersNumber-1).toString()+"TypeId")
            val textViewGameOver: TextView = view.findViewById(R.id.tvGameOver)
            val keyRoundsOfWinner = (playersNumber-1).toString()+"Rounds"

            val chart: BarChart = view.findViewById(R.id.timeChart)
            val timeData = ArrayList<BarEntry>()
            val setColors = ArrayList<Int>(playersNumber)

            chart.tag = getString(R.string.step_time_chart_label)
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.setFitBars(true)
            chart.setDrawValueAboveBar(true)
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
            chart.xAxis.isEnabled = false
            chart.xAxis.textColor = resources.getColor(R.color.colorMessage)
            chart.xAxis.textSize = 12.0F

            for (i in 1..playersNumber) {
                val keyAvgStepTime = (i-1).toString()+"AvgStepTime"
                val keyId = (i-1).toString()+"Id"
                timeData.add(BarEntry((i-1).toFloat(), bundle.getInt(keyAvgStepTime).toFloat()))
                setColors.add((i-1), PlayerVisualRepresentation.getColorById(bundle.getInt(keyId)))
            }

            val set = BarDataSet(timeData, "")
            set.colors = setColors

            val dataSet = BarData(set)
            dataSet.setValueTextColor(resources.getColor(R.color.colorMessage))

            chart.data = dataSet
            chart.invalidate()

            var winnerTypeText = ""
            when (winnerType) {
                HUMAN -> {
                    winnerTypeText = getString(R.string.type_human)
                    buildConfetti(view, setColors[playersNumber-1])
                }
                AI -> {
                    winnerTypeText = getString(R.string.type_ai)
                }
            }
            textViewGameOver.text = getString(R.string.game_over_data, winnerTypeText, bundle.getInt(keyRoundsOfWinner))

        }

        return view

    }

    /**
     * Shows confetti falling on the screen
     *
     * @param     winnerColorId     Color Id of the winner Player to show the confetti with
     * @param     view              The view which contains the confetti element
     */
    private fun buildConfetti(view: View, winnerColorId: Int){
        val confetti: KonfettiView = view.findViewById(R.id.viewConfetti)
        confetti.build()
            .addColors(Color.YELLOW, winnerColorId, Color.MAGENTA, winnerColorId)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            //hacking needed as confetti.width returns false value
            .setPosition(-50f, confetti.width +50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

}