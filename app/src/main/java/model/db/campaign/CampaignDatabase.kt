package model.db.campaign

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [CampaignLevel::class], version = 1, exportSchema = false)
abstract class CampaignDatabase : RoomDatabase() {
    abstract fun campaignLevelsDAO(): CampaignLevelsDAO
}