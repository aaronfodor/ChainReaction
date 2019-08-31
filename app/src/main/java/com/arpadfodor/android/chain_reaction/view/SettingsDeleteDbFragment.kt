package com.arpadfodor.android.chain_reaction.view

import androidx.room.Room
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arpadfodor.android.chain_reaction.R
import com.arpadfodor.android.chain_reaction.model.db.DbDefaults
import com.arpadfodor.android.chain_reaction.model.db.challenge.ChallengeDatabase
import com.arpadfodor.android.chain_reaction.model.db.stats.PlayerTypeStatsDatabase
import com.arpadfodor.android.chain_reaction.presenter.AudioPresenter
import com.arpadfodor.android.chain_reaction.view.subclass.BaseDialog

class SettingsDeleteDbFragment : PreferenceFragmentCompat() {

    private var deleteDialog: BaseDialog? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, key: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onResume() {

        super.onResume()

        val myPref = findPreference("delete_database") as Preference
        myPref.setOnPreferenceClickListener {
            AudioPresenter.soundButtonClick()
            deleteDialog()
            true
        }

    }

    private fun deleteDialog(){

        if(deleteDialog == null) {

            deleteDialog = BaseDialog(this.requireContext(), getString(R.string.confirm_delete), getString(R.string.confirm_delete_database),  resources.getDrawable(R.drawable.warning))

            deleteDialog!!.setPositiveButton {

                val dbStats = Room.databaseBuilder(this.requireContext(), PlayerTypeStatsDatabase::class.java, "db_stats").build()
                val dbChallenge = Room.databaseBuilder(this.requireContext(), ChallengeDatabase::class.java, "db_challenge").build()

                Thread {

                    dbStats.playerTypeStatDAO().deleteAll()
                    val defaultStats = DbDefaults.statsDatabaseDefaults()
                    for(stat in defaultStats){
                        dbStats.playerTypeStatDAO().insert(stat)
                    }
                    dbStats.close()

                    dbChallenge.challengeLevelsDAO().deleteAll()
                    val defaultChallengeStates = DbDefaults.challengeDatabaseDefaults()
                    for(level in defaultChallengeStates){
                        dbChallenge.challengeLevelsDAO().insert(level)
                    }
                    dbChallenge.close()

                }.start()

                deleteDialog = null
                Snackbar.make(this.activity?.findViewById(android.R.id.content)!!, "Statistics are cleared", Snackbar.LENGTH_SHORT).show()

            }

            deleteDialog!!.setNegativeButton {
                deleteDialog = null
            }

            deleteDialog!!.show()

        }

    }

}