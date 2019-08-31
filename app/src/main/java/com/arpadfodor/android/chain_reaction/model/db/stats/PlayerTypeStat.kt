package com.arpadfodor.android.chain_reaction.model.db.stats

import androidx.room.*
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