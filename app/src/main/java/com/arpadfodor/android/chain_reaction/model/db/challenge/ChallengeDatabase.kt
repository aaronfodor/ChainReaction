package com.arpadfodor.android.chain_reaction.model.db.challenge

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChallengeLevel::class], version = 1, exportSchema = false)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeLevelsDAO(): ChallengeLevelsDAO
}