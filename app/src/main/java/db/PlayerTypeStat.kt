package db

import android.arch.persistence.room.*
import java.io.Serializable

@Entity
data class PlayerTypeStat(
    @PrimaryKey(autoGenerate = true)
    var TypeId: Long = 0,
    @ColumnInfo(name = "type_name")
    var TypeName: String = "",
    @ColumnInfo(name = "number_of_victories")
    var NumberOfVictories: Int = 0

) : Serializable