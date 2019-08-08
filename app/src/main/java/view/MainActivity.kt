package view

import android.app.ActivityOptions
import presenter.task.AILoaderTask
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.preference.PreferenceManager
import android.view.WindowManager
import hu.bme.aut.android.chain_reaction.R
import kotlinx.android.synthetic.main.activity_main.*
import model.ai.PlayerLogic
import presenter.AdPresenter
import presenter.AudioPresenter
import presenter.IMainView
import view.subclass.AdActivity
import view.subclass.BaseDialog

/**
 * Main Activity - entry point
 */
class MainActivity : AdActivity(), IMainView {

    /**
     * Sets startup screen, initializes the global presenter objects
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        //Set the startup screen - make sure this is before calling super.onCreate
        setTheme(R.style.defaultScreenTheme)

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //initializing the global presenter objects
        AudioPresenter.init(applicationContext)
        AdPresenter.initMobileAds(applicationContext)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsActivity.changeSettings(sharedPreferences)

        //adding buttons to the activity register to animate all of them
        this.addButtonToRegister(buttonNewGame)
        this.addButtonToRegister(buttonStats)
        this.addButtonToRegister(buttonSettings)
        this.addButtonToRegister(buttonMore)
        this.addButtonToRegister(buttonExit)

        buttonNewGame.setOnClickEvent {
            val intent = Intent(this, TypeActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonStats.setOnClickEvent {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonSettings.setOnClickEvent {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonMore.setOnClickEvent {
            val intent = Intent(this, MoreActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonExit.setOnClickEvent {
            exitDialog()
        }

        initActivityAd(findViewById(R.id.mainAdView))

    }

    /**
     * Shows the exit dialog
     */
    override fun onBackPressed() {
        exitDialog()
    }

    /**
     * Asks for exit confirmation
     */
    private fun exitDialog(){

        val exitDialog = BaseDialog(this, getString(R.string.confirm_exit), getString(R.string.confirm_exit_description), resources.getDrawable(R.drawable.warning))
        exitDialog.setPositiveButton {
            //showing the home screen - app is not visible but running
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        exitDialog.show()

    }

    /**
     * Starts loading the AI component
     */
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
     * Called when returning to the activity - load the AI component if not loaded
     */
    override fun onResume() {
        super.onResume()
        startAILoading()
    }

}
