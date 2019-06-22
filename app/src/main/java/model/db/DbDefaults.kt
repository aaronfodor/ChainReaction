package model.db

import model.db.campaign.CampaignLevel
import model.db.stats.PlayerTypeStat

/**
 * Database default values
 */
object DbDefaults {

    /**
     * Campaign database defaults in a mutable list
     *
     * @return  MutableList<CampaignLevel>     The mutable list values to fill the default campaign database with
     */
    fun campaignDatabaseDefaults() : MutableList<CampaignLevel>{
        val campaignLevels = mutableListOf<CampaignLevel>()
        campaignLevels.add(CampaignLevel(1, "level 1", true, false, 1, 3, 3, 2, 1))
        campaignLevels.add(CampaignLevel(2, "level 2", false, false, 1, 6, 4, 3, 2))
        campaignLevels.add(CampaignLevel(3, "level 3", false, false, 2, 7, 5, 8, 8))
        return campaignLevels
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