package view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import hu.bme.aut.android.chainreaction.R

/**
 * Activity of settings
 */
class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        const val KEY_PROPAGATION = "show_propagation"
        const val KEY_TIME_LIMIT = "time_limit"

        fun changeSettings(sharedPreferences: SharedPreferences, context: Context) {
            val showPropagation = sharedPreferences.getBoolean(KEY_PROPAGATION, true)
            val difficulty = sharedPreferences.getBoolean(KEY_TIME_LIMIT, true)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, FragmentSettingsBasic())
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        when(key) {
            KEY_PROPAGATION -> {
                changeSettings(sharedPreferences, applicationContext)
            }
            KEY_TIME_LIMIT -> {
                changeSettings(sharedPreferences, applicationContext)
            }
        }

    }

    class FragmentSettingsBasic : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, key: String?) {
            addPreferencesFromResource(R.xml.preferences)
        }

    }

}