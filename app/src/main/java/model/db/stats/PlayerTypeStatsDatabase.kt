package model.db.stats

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import model.db.stats.PlayerTypeStat
import model.db.stats.PlayerTypeStatDAO

@Database(entities = [PlayerTypeStat::class], version = 1, exportSchema = false)
abstract class PlayerTypeStatsDatabase : RoomDatabase() {
    abstract fun playerTypeStatDAO(): PlayerTypeStatDAO
}