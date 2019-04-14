package view

import presenter.task.AILoaderTask
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start.*
import model.ai.PlayerLogic

/**
 * Main Activity - entry point
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsActivity.changeSettings(sharedPreferences, this)

        buttonNewGame.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
        }

        buttonStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        buttonAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        buttonExit.setOnClickListener {
            //showing the home screen - app is not visible but running
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        Snackbar.make(constraintLayoutMainActivity, hu.bme.aut.android.chainreaction.R.string.how_to_exit, Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {

        super.onResume()

        if(!PlayerLogic.isNeuralNetworkLoaded()){
            //loading the AI component
            val taskAILoad = AILoaderTask(this, findViewById(android.R.id.content))
            taskAILoad.execute()
        }

    }

}
