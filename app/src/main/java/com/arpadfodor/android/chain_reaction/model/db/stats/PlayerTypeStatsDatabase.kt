package com.arpadfodor.android.chain_reaction.model.db.stats

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlayerTypeStat::class], version = 1, exportSchema = false)
abstract class PlayerTypeStatsDatabase : RoomDatabase() {
    abstract fun playerTypeStatDAO(): PlayerTypeStatDAO
}