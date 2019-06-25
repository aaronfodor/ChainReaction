package view

import android.arch.persistence.room.Room
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import hu.bme.aut.android.chainreaction.R
import com.github.mikephil.charting.components.Legend
import com.google.android.gms.ads.AdView
import model.db.stats.PlayerTypeStat
import model.db.stats.PlayerTypeStatsDatabase
import presenter.AdPresenter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class StatsActivity : AppCompatActivity() {

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_stats)
        databaseReader()

        mAdView = this.findViewById(R.id.statsAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Reads the database
     * Retrieves the stored data from the database: playerTypeStats[0] is the human, playerTypeStats[1] is the AI data
     */
    private fun databaseReader(){

        val dbStats = Room.databaseBuilder(applicationContext, PlayerTypeStatsDatabase::class.java, "db_stats").build()
        var playerTypeStats: MutableList<PlayerTypeStat>

        Thread {

            playerTypeStats = dbStats.playerTypeStatDAO().getAll().toMutableList()

            runOnUiThread {

                val textViewAiVsHumanStats = findViewById<TextView>(R.id.textViewAiVsHumanStats)
                val textViewWinners = findViewById<TextView>(R.id.textViewWinningStats)
                val textViewAllStats = findViewById<TextView>(R.id.textViewAllStats)

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
                    displayChart(humanVictories, aiVictories)
                }

            }

            dbStats.close()

        }.start()

    }

    private fun displayChart(humanVictories: Int, aiVictories: Int){

        val chart: PieChart = this.findViewById(R.id.statsChart)
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

        victoryData.add(PieEntry(humanVictories.toFloat(), "human"))
        setColors.add(Color.GRAY)
        victoryData.add(PieEntry(aiVictories.toFloat(), "AI"))
        setColors.add(Color.BLACK)

        val set = PieDataSet(victoryData, "Victories %")
        set.colors = setColors

        val dataLabels = ArrayList<String>()
        dataLabels.add("human")
        dataLabels.add("AI")

        val dataSet = PieData(set)
        dataSet.setValueTextColor(resources.getColor(R.color.colorMessage))
        dataSet.setValueTextSize(12f)

        chart.data = dataSet
        chart.invalidate()

    }

    /**
     * Called when leaving the activity - stops the presenter calculations too
     */
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    /**
     * Called when returning to the activity
     */
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    /**
     * Called before the activity is destroyed
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}