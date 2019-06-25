package model.db.challenge

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [ChallengeLevel::class], version = 1, exportSchema = false)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeLevelsDAO(): ChallengeLevelsDAO
}