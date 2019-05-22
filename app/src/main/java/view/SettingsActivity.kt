package view

import android.arch.persistence.room.Room
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.WindowManager
import model.db.PlayerTypeStatsDatabase
import hu.bme.aut.android.chainreaction.R

/**
 * Activity of settings
 */
class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        const val KEY_PROPAGATION = "show_propagation"
        const val KEY_TIME_LIMIT = "time_limit"
        const val KEY_DELETE_DATABASE = "delete_database"

        fun changeSettings(sharedPreferences: SharedPreferences) {
            with (sharedPreferences.edit()) {
                remove(KEY_PROPAGATION)
                putBoolean(KEY_PROPAGATION, sharedPreferences.getBoolean(KEY_PROPAGATION, true))
                remove(KEY_TIME_LIMIT)
                putBoolean(KEY_TIME_LIMIT, sharedPreferences.getBoolean(KEY_TIME_LIMIT, false))
                remove(KEY_DELETE_DATABASE)
                putBoolean(KEY_DELETE_DATABASE, false)
                apply()
            }
        }

    }

    private var deleteDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, FragmentSettingsBasic())
            .commit()

    }

    private fun deleteDialog(){

        if(deleteDialog == null) {

            deleteDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_delete_database))
                .setPositiveButton(android.R.string.yes) { dialog, which ->

                    val db = Room.databaseBuilder(applicationContext, PlayerTypeStatsDatabase::class.java, "db").build()
                    Thread {
                        db.playerTypeStatDAO().deleteAll()
                    }.start()
                    deleteDialog = null
                    Snackbar.make(findViewById(android.R.id.content), "Statistics are cleared", Snackbar.LENGTH_SHORT).show()

                }
                .setNegativeButton(android.R.string.no) {dialog, which ->
                    deleteDialog = null
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

        }

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
                changeSettings(sharedPreferences)
            }
            KEY_TIME_LIMIT -> {
                changeSettings(sharedPreferences)
            }
            KEY_DELETE_DATABASE -> {
                changeSettings(sharedPreferences)
                deleteDialog()
            }
        }

    }

    class FragmentSettingsBasic : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, key: String?) {
            addPreferencesFromResource(R.xml.preferences)
        }

    }

}