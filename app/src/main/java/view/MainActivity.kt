package view

import presenter.task.AILoaderTask
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.preference.PreferenceManager
import android.widget.Button

/**
 * Main Activity - entry point
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_main)

        //loading the AI component
        var taskAIload = AILoaderTask()
        taskAIload.LoadNeuralNetwork(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsActivity.changeSettings(sharedPreferences, this)

        val newGameButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonNewGame)
        newGameButton.setOnClickListener {
            //startActivity(Intent(this, GameActivity::class.java))
            startActivity(Intent(this, StartActivity::class.java))
        }

        val statsButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonStats)
        statsButton.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        val settingsButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonSettings)
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val aboutButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAbout)
        aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        val exitButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonExit)
        exitButton.setOnClickListener {
            //showing the home screen - app is not visible but running
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        // do nothing - accessing previous games are not allowed
    }

}
