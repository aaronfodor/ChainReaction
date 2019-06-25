package model.db.challenge

import android.arch.persistence.room.*
import java.io.Serializable

@Entity
data class ChallengeLevel(
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0,
    @ColumnInfo(name = "level_name")
    var LevelName: String = "",
    @ColumnInfo(name = "playable")
    var Playable: Boolean = false,
    @ColumnInfo(name = "completed")
    var Completed: Boolean = false,
    @ColumnInfo(name = "game_type")
    var GameType: Int = 1,
    @ColumnInfo(name = "height")
    var Height: Int = 3,
    @ColumnInfo(name = "width")
    var Width: Int = 3,
    @ColumnInfo(name = "player_number")
    var PlayerNumber: Int = 2,
    @ColumnInfo(name = "human_player_order")
    var HumanPlayerOrder: Int = 1

) : Serializable