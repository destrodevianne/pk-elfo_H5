package ai.individual;

import pk.elfo.gameserver.instancemanager.ZoneManager;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.zone.type.L2EffectZone;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class QueenShyeed extends AbstractNpcAI
{
	// NPC
	private static final int SHYEED = 25671;
	private static final Location SHYEED_LOC = new Location(79634, -55428, -6104, 0);
	
	// Respawn
	private static final int RESPAWN = 86400000; // 24 h
	private static final int RANDOM_RESPAWN = 43200000; // 12 h
	
	// Zones
	private static final L2EffectZone MOB_BUFF_ZONE = ZoneManager.getInstance().getZoneById(200103, L2EffectZone.class);
	private static final L2EffectZone MOB_BUFF_DISPLAY_ZONE = ZoneManager.getInstance().getZoneById(200104, L2EffectZone.class);
	private static final L2EffectZone PC_BUFF_ZONE = ZoneManager.getInstance().getZoneById(200105, L2EffectZone.class);
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "respawn":
				spawnShyeed();
				break;
			case "despawn":
				if (!npc.isDead())
				{
					npc.deleteMe();
					startRespawn();
				}
				break;
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		broadcastNpcSay(npc, Say2.NPC_ALL, NpcStringId.SHYEEDS_CRY_IS_STEADILY_DYING_DOWN);
		startRespawn();
		PC_BUFF_ZONE.setZoneEnabled(true);
		return super.onKill(npc, killer, isSummon);
	}
	
	public QueenShyeed(String name, String descr)
	{
		super(name, descr);
		addKillId(SHYEED);
		spawnShyeed();
	}
	
	private void spawnShyeed()
	{
		String respawn = loadGlobalQuestVar("Respawn");
		long remain = (!respawn.isEmpty()) ? Long.parseLong(respawn) - System.currentTimeMillis() : 0;
		if (remain > 0)
		{
			startQuestTimer("respawn", remain, null, null);
			return;
		}
		final L2Npc npc = addSpawn(SHYEED, SHYEED_LOC, false, 0);
		startQuestTimer("despawn", 10800000, npc, null);
		PC_BUFF_ZONE.setZoneEnabled(false);
		MOB_BUFF_ZONE.setZoneEnabled(true);
		MOB_BUFF_DISPLAY_ZONE.setZoneEnabled(true);
	}
	
	private void startRespawn()
	{
		int respawnTime = RESPAWN - getRandom(RANDOM_RESPAWN);
		saveGlobalQuestVar("Respawn", Long.toString(System.currentTimeMillis() + respawnTime));
		startQuestTimer("respawn", respawnTime, null, null);
		MOB_BUFF_ZONE.setZoneEnabled(false);
		MOB_BUFF_DISPLAY_ZONE.setZoneEnabled(true);
	}
	
	public static void main(String[] args)
	{
		new QueenShyeed(QueenShyeed.class.getSimpleName(), "ai");
	}
}