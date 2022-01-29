package com.aaronfodor.android.chain_reaction.model.db.challenge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChallengeLevelsDAO {

    @Query("SELECT * FROM ChallengeLevel")
    fun getAll(): List<ChallengeLevel>

    @Query("DELETE FROM ChallengeLevel")
    fun deleteAll()

    @Insert
    fun insert(vararg challenge_level: ChallengeLevel)

    @Query("UPDATE ChallengeLevel SET Completed = :completed WHERE Id = :Id")
    fun completeLevel(completed: Boolean, Id: Long)

    @Query("UPDATE ChallengeLevel SET Playable = :isUnlocked WHERE Id = :Id")
    fun unlockLevel(isUnlocked: Boolean, Id: Long)

}