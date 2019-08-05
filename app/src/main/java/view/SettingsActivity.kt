package view

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import hu.bme.aut.android.chain_reaction.R
import presenter.AudioPresenter
import view.subclass.BaseActivity

/**
 * Activity of settings
 */
class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        const val KEY_PROPAGATION = "show_propagation"
        const val KEY_GIF = "gif_enabled"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_TIME_LIMIT = "time_limit"
        const val KEY_DELETE_DATABASE = "delete_database"

        fun changeSettings(sharedPreferences: SharedPreferences) {
            with (sharedPreferences.edit()) {
                remove(KEY_PROPAGATION)
                putBoolean(KEY_PROPAGATION, sharedPreferences.getBoolean(KEY_PROPAGATION, true))
                remove(KEY_GIF)
                putBoolean(KEY_GIF, sharedPreferences.getBoolean(KEY_GIF, true))
                remove(KEY_SOUND_ENABLED)
                putBoolean(KEY_SOUND_ENABLED, sharedPreferences.getBoolean(KEY_SOUND_ENABLED, true))
                remove(KEY_TIME_LIMIT)
                putBoolean(KEY_TIME_LIMIT, sharedPreferences.getBoolean(KEY_TIME_LIMIT, false))
                remove(KEY_DELETE_DATABASE)
                putBoolean(KEY_DELETE_DATABASE, false)
                apply()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsDeleteDbFragment())
            .commit()

    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AudioPresenter.soundBackClick()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        when(sharedPreferences.getBoolean(key, false)){
            true ->{
                AudioPresenter.soundOn()
            }
            false ->{
                AudioPresenter.soundOff()
            }
        }

        when(key) {
            KEY_PROPAGATION -> {
                changeSettings(sharedPreferences)
            }
            KEY_GIF -> {
                changeSettings(sharedPreferences)
            }
            KEY_SOUND_ENABLED -> {
                AudioPresenter.refreshAudioFlag(applicationContext)
                changeSettings(sharedPreferences)
                AudioPresenter.refreshAudioFlag(applicationContext)
            }
            KEY_TIME_LIMIT -> {
                changeSettings(sharedPreferences)
            }
            KEY_DELETE_DATABASE -> {
                changeSettings(sharedPreferences)
            }
        }

    }

}