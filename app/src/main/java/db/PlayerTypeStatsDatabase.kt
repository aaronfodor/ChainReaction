package db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [PlayerTypeStat::class], version = 1, exportSchema = false)
abstract class PlayerTypeStatsDatabase : RoomDatabase() {
    abstract fun playerTypeStatDAO(): PlayerTypeStatDAO
}