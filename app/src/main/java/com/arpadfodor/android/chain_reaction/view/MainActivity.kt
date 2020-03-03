package com.arpadfodor.android.chain_reaction.view

import android.app.ActivityOptions
import com.arpadfodor.android.chain_reaction.presenter.task.AILoaderTask
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import androidx.preference.PreferenceManager
import android.view.WindowManager
import com.arpadfodor.android.chain_reaction.R
import kotlinx.android.synthetic.main.activity_main.*
import com.arpadfodor.android.chain_reaction.model.ai.PlayerLogic
import com.arpadfodor.android.chain_reaction.presenter.AdPresenter
import com.arpadfodor.android.chain_reaction.presenter.AudioPresenter
import com.arpadfodor.android.chain_reaction.presenter.IMainView
import com.arpadfodor.android.chain_reaction.view.subclass.AdActivity
import com.arpadfodor.android.chain_reaction.view.subclass.BaseDialog
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PlayGamesAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn

/**
 * Main Activity - entry point
 */
class MainActivity : AdActivity(), IMainView {

    /**
     * Firebase authentication object
     */
    private lateinit var auth: FirebaseAuth

    /**
     * TAG of the activity
     */
    private val TAG = "MainActivity"

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
        AdPresenter.initMobileAds(applicationContext, getString(R.string.app_ad_id))

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

        initActivityAd(this.findViewById(R.id.mainAdView))
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

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

    private fun signInSilently() {

        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable
            firebaseAuthWithPlayGames(account!!)
        }

        else {
            // Haven't been signed-in before. Try the silent sign-in first
            val signInClient = GoogleSignIn.getClient(this, signInOptions)
            signInClient
                .silentSignIn()
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful()) {
                        // The signed in account is stored in the task's result.
                        val signedInAccount = task.getResult()
                        firebaseAuthWithPlayGames(signedInAccount!!)

                    } else {
                        // Player will need to sign-in explicitly using via UI.
                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                        // Interactive Sign-in.
                    }
                }
        }

    }

    /**
     * Tries to sign-in to Firebase with the Google Play account of the device
     */
    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {

        val view = findViewById<ConstraintLayout>(R.id.constraintLayoutMainActivity)

        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)

        val auth = FirebaseAuth.getInstance()
        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Snackbar.make(view, R.string.auth_completed, Snackbar.LENGTH_LONG).show()
                    showLoginStatus(user)
                }

                else {
                    // If sign in fails, display a message and the signed out UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(view, R.string.auth_failed, Snackbar.LENGTH_LONG).show()
                    showLoginStatus(null)
                }

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
     * Shows the Google Play login status
     *
     * @param    user   A nullable FirebaseUser object to show its data - if null, user is not signed in
     */
    override fun showLoginStatus(user: FirebaseUser?){}

    /**
     * Called when returning to the activity - loads the AI component if not loaded, tries to sign-in
     */
    override fun onResume() {
        super.onResume()
        startAILoading()
        signInSilently()
    }

}
