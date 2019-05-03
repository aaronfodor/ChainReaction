package view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_stats)

        //var db_task = InteractDBTask()
        //var list = db_task.LoadPlayerTypeDB(this)

        val TextViewStats = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewStats)
        TextViewStats.text = "No DB data found"

    }

}