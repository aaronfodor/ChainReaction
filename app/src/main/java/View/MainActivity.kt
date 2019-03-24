package View

import Presenter.Task.AILoaderTask
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

        var task_AI_load = AILoaderTask()
        task_AI_load.LoadNeuralNetwork(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsActivity.changeSettings(sharedPreferences, this)

        val NewGameButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonNewGame)
        NewGameButton.setOnClickListener {
            //startActivity(Intent(this, GameActivity::class.java))
            startActivity(Intent(this, StartActivity::class.java))
        }

        val StatsButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonStats)
        StatsButton.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        val SettingsButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonSettings)
        SettingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val AboutButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAbout)
        AboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        val ExitButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonExit)
        ExitButton.setOnClickListener {
            finish()
            System.exit(0)
        }

    }

}
