package view

import android.app.ActivityOptions
import presenter.task.AILoaderTask
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import android.view.WindowManager
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import kotlinx.android.synthetic.main.activity_main.*
import model.ai.PlayerLogic
import presenter.AdPresenter
import presenter.IMainView

/**
 * Main Activity - entry point
 */
class MainActivity : AppCompatActivity(), IMainView {

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsActivity.changeSettings(sharedPreferences)

        buttonNewGame.setOnClickListener {
            val intent = Intent(this, TypeActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonExit.setOnClickListener {
            exitDialog()
        }

        mAdView = findViewById(R.id.mainAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_exit))
            .setMessage(getString(R.string.confirm_exit_description))
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                //showing the home screen - app is not visible but running
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun startAILoading(){

        if(!PlayerLogic.isNeuralNetworkLoaded()){
            //loading the AI component
            val taskAILoad = AILoaderTask(this, applicationContext)
            taskAILoad.execute()
        }

    }

    /**
     * Notifies the user about AI loading result
     *
     * @param    isLoaded   True if loaded, false otherwise
     */
    override fun loadedAI(isLoaded: Boolean) {

        val view = findViewById<ConstraintLayout>(R.id.constraintLayoutMainActivity)

        if(isLoaded){
            buttonNewGame.setText(R.string.button_new_game)
            buttonNewGame.isEnabled = true
            Snackbar.make(view, R.string.ai_loaded, Snackbar.LENGTH_LONG).show()
        }

        else{
            buttonNewGame.setText(R.string.ai_load_error)
            buttonNewGame.isEnabled = false
            Snackbar.make(view, R.string.ai_load_error, Snackbar.LENGTH_LONG).show()
        }

    }

    /**
     * Notifies the user that AI component is not loaded
     */
    override fun notLoadedAI() {
        buttonNewGame.setText(R.string.button_loading)
        buttonNewGame.isEnabled = false
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
        startAILoading()
    }

    /**
     * Called before the activity is destroyed
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}
