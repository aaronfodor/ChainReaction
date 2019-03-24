package Model.DB

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PlayerTypeStatDAO {

    @Query("SELECT * FROM player_type_statistics")
    fun getAllStats(): List<PlayerTypeStat>

    @Insert
    fun insertStats(vararg player_type_stats: PlayerTypeStat)

    @Delete
    fun deleteStat(player_type_stat: PlayerTypeStat)

}