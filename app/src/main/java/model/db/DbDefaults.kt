package model.db

import model.db.challenge.ChallengeLevel
import model.db.stats.PlayerTypeStat

/**
 * Database default values
 */
object DbDefaults {

    /**
     * Campaign database defaults in a mutable list
     *
     * @return  MutableList<ChallengeLevel>     The mutable list values to fill the default campaign database with
     */
    fun challengeDatabaseDefaults() : MutableList<ChallengeLevel>{
        val challengeLevels = mutableListOf<ChallengeLevel>()
        challengeLevels.add(ChallengeLevel(1, "level 1", true, false, 1, 3, 3, 2, 1))
        challengeLevels.add(ChallengeLevel(2, "level 2", false, false, 1, 5, 4, 2, 1))
        challengeLevels.add(ChallengeLevel(3, "level 3", false, false, 1, 7, 5, 3, 2))
        challengeLevels.add(ChallengeLevel(4, "level 4", false, false, 1, 10, 8, 2, 2))
        challengeLevels.add(ChallengeLevel(5, "level 5", false, false, 1, 3, 3, 4, 1))
        challengeLevels.add(ChallengeLevel(6, "level 6", false, false, 1, 5, 4, 5, 3))
        challengeLevels.add(ChallengeLevel(7, "level 7", false, false, 1, 7, 5, 6, 5))
        challengeLevels.add(ChallengeLevel(8, "level 8", false, false, 1, 3, 3, 8, 1))
        challengeLevels.add(ChallengeLevel(9, "level 9", false, false, 1, 6, 4, 8, 2))
        challengeLevels.add(ChallengeLevel(10, "level 10", false, false, 1, 8, 5, 8, 3))
        challengeLevels.add(ChallengeLevel(11, "level 11", false, false, 1, 10, 7, 8, 4))
        challengeLevels.add(ChallengeLevel(12, "level 12", false, false, 1, 12, 9, 8, 8))
        return challengeLevels
    }

    /**
     * Statistics database defaults in a mutable list
     *
     * @return  MutableList<PlayerTypeStat>     The mutable list values to fill the default stats database with
     */
    fun statsDatabaseDefaults() : MutableList<PlayerTypeStat>{
        val playerTypeStats = mutableListOf<PlayerTypeStat>()
        playerTypeStats.add(PlayerTypeStat(1, "human", 0))
        playerTypeStats.add(PlayerTypeStat(2, "ai", 0))
        playerTypeStats.add(PlayerTypeStat(3, "all_games", 0))
        return playerTypeStats
    }

}