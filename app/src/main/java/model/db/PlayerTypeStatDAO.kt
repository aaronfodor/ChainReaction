package model.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PlayerTypeStatDAO {

    @Query("SELECT * FROM PlayerTypeStat")
    fun getAll(): List<PlayerTypeStat>

    @Query("DELETE FROM PlayerTypeStat")
    fun deleteAll()

    @Insert
    fun insert(vararg player_type_stat: PlayerTypeStat)

    @Query("UPDATE PlayerTypeStat SET number_of_victories=:num WHERE type_name = :name")
    fun update(num: Int, name: String)

}