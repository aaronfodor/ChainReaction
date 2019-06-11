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
import model.db.PlayerTypeStat
import model.db.PlayerTypeStatsDatabase
import presenter.AdPresenter

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

        mAdView = findViewById(R.id.statsAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Reads the database
     * Retrieves the stored data from the database: playerTypeStats[0] is the human, playerTypeStats[1] is the AI data
     */
    private fun databaseReader(){

        val db = Room.databaseBuilder(applicationContext, PlayerTypeStatsDatabase::class.java, "db").build()
        var playerTypeStats: MutableList<PlayerTypeStat>

        Thread {

            playerTypeStats = db.playerTypeStatDAO().getAll().toMutableList()

            runOnUiThread {

                val textViewStats = findViewById<TextView>(R.id.textViewStats)
                val textViewWinners = findViewById<TextView>(R.id.textViewWinningStats)

                if(playerTypeStats.isEmpty()){
                    textViewStats.text = getString(R.string.empty_db)
                    textViewWinners.text = ""
                }

                else{
                    val humanVictories = playerTypeStats[0].NumberOfVictories
                    val aiVictories = playerTypeStats[1].NumberOfVictories
                    textViewStats.text = getString(R.string.stats_data, playerTypeStats[2].NumberOfVictories, humanVictories+aiVictories)
                    textViewWinners.text = getString(R.string.winning_data, humanVictories, aiVictories)
                    displayChart(humanVictories, aiVictories)
                }

            }

            db.close()

        }.start()

    }

    private fun displayChart(humanVictories: Int, aiVictories: Int){

        val chart: PieChart = findViewById(R.id.statsChart)
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
        chart.transparentCircleRadius = 61f

        val l = chart.legend
        l.isEnabled = true
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.textColor = resources.getColor(R.color.colorMessage)

        victoryData.add(PieEntry(humanVictories.toFloat()))
        setColors.add(Color.GRAY)
        victoryData.add(PieEntry(aiVictories.toFloat()))
        setColors.add(Color.BLACK)

        val set = PieDataSet(victoryData, "Victories in percentage")
        set.colors = setColors

        val dataSet = PieData(set)
        dataSet.setValueTextColor(resources.getColor(R.color.colorMessage))

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