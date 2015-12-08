package pk.elfo.gameserver.masteriopack.rankpvpsystem;

import java.util.Map;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.zone.ZoneId;

/**
 * Class contains some check methods. 
 * Each L2j pack use difference variables or method names, so this class is dedicated for each pack.
 * PkElfo
 */
public class RPSProtection
{
	/**
	 * No farming detected if return TRUE.<BR>
	 * Checking: Party, Clan/Ally, IP, self-kill.
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static final boolean antiFarmCheck(L2PcInstance player1, L2PcInstance player2)
	{
		
		if(player1 == null || player2 == null)
			return true;
		
		if(player1.equals(player2))
			return false;

		//Anti FARM Clan - Ally
		if(RPSConfig.ANTI_FARM_CLAN_ALLY_ENABLED && checkClan(player1, player2) && checkAlly(player1, player2))
		{
			player1.sendMessage("PvP Farm is not allowed!");
			return false;
		}

		//Anti FARM Party   
		if(RPSConfig.ANTI_FARM_PARTY_ENABLED && checkParty(player1, player2))
		{
			player1.sendMessage("PvP Farm is not allowed!");   
			return false;
		}      
		
		//Anti FARM same IP
		if(RPSConfig.ANTI_FARM_IP_ENABLED && checkIP(player1, player2))
		{
			player1.sendMessage("PvP Farm is not allowed!");
		    return false;
		}

		return true;
	}
	
	/**
	 * If player on the event or on olympiad, return TRUE.
	 * This method can be difference for any l2jServer revisions.
	 * @param player 
	 * @return
	 */
	public static final boolean checkEvent(L2PcInstance player)
	{
		
		if(player.isInOlympiadMode() || player.isOlympiadStart())
			return true;
		
		if(player.getEventStatus() != null)
			return true;
		
		return false;
	}
	
	/**
	 * If player1 and player2 are in the same clan, return TRUE.
	 * @param player1
	 * @param player2
	 * @return
	 */
	public final static boolean checkClan(L2PcInstance player1, L2PcInstance player2)
	{
		if(player1.getClanId() > 0 && player2.getClanId() > 0 && player1.getClanId() == player2.getClanId())
			return true;
		
		return false;
	}
	
	/**
	 * If player1 and player2 in the same ally, return TRUE.
	 * @param player1
	 * @param player2
	 * @return
	 */
	public final static boolean checkAlly(L2PcInstance player1, L2PcInstance player2)
	{
		if(player1.getAllyId() > 0 && player2.getAllyId() > 0 && player1.getAllyId() == player2.getAllyId())
			return true;
		
		return false;
	}
	
	/**
	 * If player1 and player2 clans have a war, return TRUE.
	 * @param player1
	 * @param player2
	 * @return
	 */
	public final static boolean checkWar(L2PcInstance player1, L2PcInstance player2)
	{
		if(player1.getClanId() > 0 && player2.getClanId() > 0 && player1.getClan() != null && player2.getClan() != null && player1.getClan().isAtWarWith(player2.getClan()))
			return true;
		
		return false;
	}
	
	/**
	 * If player1 and player2 are in party return TRUE.
	 * @param player1
	 * @param player2
	 * @return
	 */
	public final static boolean checkParty(L2PcInstance player1, L2PcInstance player2)
	{
		if(player1.getParty() != null && player2.getParty() != null && player1.getParty().equals(player2.getParty()))
			return true;
		
		return false;
	}
	
	/**
	 *  If killer and victim have the same IP address return TRUE.
	 * @param killer
	 * @param victim
	 * @return
	 */
	public final static boolean checkIP(L2PcInstance killer, L2PcInstance victim)
	{
		if(killer.getClient() != null && victim.getClient() != null)
	    {
	        String ip1 = killer.getClient().getConnection().getInetAddress().getHostAddress();
	        String ip2 = victim.getClient().getConnection().getInetAddress().getHostAddress();
	 
	        if (ip1.equals(ip2))
		        return true;
	    }
		
		return false;
	}
	
	/**
	 * Returns true if character is in allowed zone.
	 * @param player
	 * @return
	 */
	public static final boolean isInPvpAllowedZone(L2PcInstance player)
	{
		if(RPSConfig.ALLOWED_ZONES_IDS.size() == 0)
			return true;
		
		for (Integer value : RPSConfig.ALLOWED_ZONES_IDS)
		{
			ZoneId zone = getZoneId(value);

			if (zone != null && player.isInsideZone(zone))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if character is in restricted zone.
	 * @param player
	 * @return
	 */
	public static final boolean isInPvpRestrictedZone(L2PcInstance player)
	{
		for (Integer value : RPSConfig.RESTRICTED_ZONES_IDS)
		{
			ZoneId zone = getZoneId(value);
			
			if (zone != null && player.isInsideZone(zone))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if character is in restricted zone for death manager.
	 * @param player
	 * @return
	 */
	public static final boolean isInDMRestrictedZone(L2PcInstance player)
	{
		for (Integer value : RPSConfig.DEATH_MANAGER_RESTRICTED_ZONES_IDS)
		{
			ZoneId zone = getZoneId(value);

			if (zone != null && player.isInsideZone(zone))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns 1.0 if character is not in Bonus Ratio zone, otherwise returns ratio from configuration file.
	 * @param player
	 * @return
	 */
	public static final double getZoneBonusRatio(L2PcInstance player)
	{
		for (Map.Entry<Integer, Double> e : RPSConfig.RANK_POINTS_BONUS_ZONES_IDS.entrySet())
		{
			ZoneId zone = getZoneId(e.getKey());
			
			if (zone != null && player.isInsideZone(zone))
				return e.getValue();
		}
		
		return 1.0;
	}
	
	/**
	 * Returns the ZoneId.<br>
	 * ZoneId not exists in lower revisions of l2jServer (H5), then this method can be removed.<br>
	 * <b>IMPORTANT:</b> L2jServer have difference zone id's for each L2 chronicle.
	 * @param zoneId
	 * @return
	 */
	private static final ZoneId getZoneId(int zoneId)
	{
		ZoneId zone = null;
		
		switch (zoneId)
		{
			case 0:
				return ZoneId.PVP;
			case 1:
				return ZoneId.PEACE;
			case 2:
				return ZoneId.SIEGE;
			case 3:
				return ZoneId.MOTHER_TREE;
			case 4:
				return ZoneId.CLAN_HALL;
			case 5:
				return ZoneId.LANDING;
			case 6:
				return ZoneId.NO_LANDING;
			case 7:
				return ZoneId.WATER;
			case 8:
				return ZoneId.JAIL;
			case 9:
				return ZoneId.MONSTER_TRACK;
			case 10:
				return ZoneId.CASTLE;
			case 11:
				return ZoneId.SWAMP;
			case 12:
				return ZoneId.NO_SUMMON_FRIEND;
			case 13:
				return ZoneId.FORT;
			case 14:
				return ZoneId.NO_STORE;
			case 15:
				return ZoneId.TOWN;
			case 16:
				return ZoneId.SCRIPT;
			case 17:
				return ZoneId.HQ;
			case 18:
				return ZoneId.DANGER_AREA;
			case 19:
				return ZoneId.ALTERED;
			case 20:
				return ZoneId.NO_BOOKMARK;
			case 21:
				return ZoneId.NO_ITEM_DROP;
			case 22:
				return ZoneId.NO_RESTART;
		}
		
		return zone;
	}
}
