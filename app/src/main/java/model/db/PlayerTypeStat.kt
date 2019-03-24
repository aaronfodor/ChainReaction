package model.db

import android.arch.persistence.room.*

@Entity(tableName = "player_type_statistics")
data class PlayerTypeStat(@PrimaryKey(autoGenerate = true) var TypeId: Long?,
                          @ColumnInfo(name = "typename") var TypeName: String,
                          @ColumnInfo(name = "numberofvictories") var NumberOfVictories: Int

)