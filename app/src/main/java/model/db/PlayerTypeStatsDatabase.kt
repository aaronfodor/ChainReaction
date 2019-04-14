package model.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(PlayerTypeStat::class), version = 1)
abstract class PlayerTypeStatsDatabase : RoomDatabase() {

    abstract fun PlayerTypeStatDao(): PlayerTypeStatDAO

    companion object {

        private var INSTANCE: PlayerTypeStatsDatabase? = null

        fun getInstance(context: Context): PlayerTypeStatsDatabase {

            if (INSTANCE == null) {
                INSTANCE =  Room.databaseBuilder(context.applicationContext,
                    PlayerTypeStatsDatabase::class.java, "player_type_statistics.db").build()
            }

            return INSTANCE!!

        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }

}