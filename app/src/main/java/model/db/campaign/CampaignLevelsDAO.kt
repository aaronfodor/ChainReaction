package model.db.campaign

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface CampaignLevelsDAO {

    @Query("SELECT * FROM CampaignLevel")
    fun getAll(): List<CampaignLevel>

    @Query("DELETE FROM CampaignLevel")
    fun deleteAll()

    @Insert
    fun insert(vararg campaign_level: CampaignLevel)

    @Query("UPDATE CampaignLevel SET Completed = :completed WHERE Id = :Id")
    fun completeLevel(completed: Boolean, Id: Long)

    @Query("UPDATE CampaignLevel SET Playable = :isUnlocked WHERE Id = :Id")
    fun unlockLevel(isUnlocked: Boolean, Id: Long)

}