package custom.RaidbossInfo;

import java.util.Map;

import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastMap;

/**
 * Update To High Five
 */
public class RaidbossInfo extends Quest
{
	private static final String qn = "RaidbossInfo";

	private static final int[] NPC =
	{
		31729,
		31730,
		31731,
		31732,
		31733,
		31734,
		31735,
		31736,
		31737,
		31738,
		31775, 
		31776,
		31777,
		31778,
		31779,
		31780,
		31781,
		31782,
		31783,
		31784,
		31785,
		31786, 
		31787,
		31788,
		31789,
		31790,
		31791,
		31792,
		31793,
		31794,
		31795,
		31796,
		31797, 
		31798,
		31799,
		31800,
		31801,
		31802,
		31803,
		31804,
		31805,
		31806,
		31807,
		31808, 
		31809,
		31810, 
		31811,
		31812,
		31813,
		31814,
		31815,
		31816,
		31817,
		31818,
		31819, 
		31820,
		31821,
		31822,
		31823,
		31824,
		31825,
		31826,
		31827,
		31828,
		31829,
		31830, 
		31831,
		31832,
		31833,
		31834,
		31835, 
		31836,
		31837,
		31838,
		31839,
		31840,
		31841, 
		31991,
		31992,
		31993, 
		31994,
		31995,
		32337,
		32338, 
		32339,
		32340
	};

	private static final Map<Integer, Location> RADAR = new FastMap<>();

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;

		QuestState st = player.getQuestState(qn);

		if (st == null)
		{
			return htmltext;
		}

		if (Util.isDigit(event))
		{
			htmltext = null;
			int rbid = Integer.parseInt(event);

			if (RADAR.containsKey(rbid))
			{
				Location loc = RADAR.get(rbid);
				st.addRadar(loc.getX(), loc.getY(), loc.getZ());
			}
			st.exitQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		return "info.htm";
	}

	public RaidbossInfo(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(NPC);
		addTalkId(NPC);
		
		// Add all Raid Bosses to RAIDS list
		for (L2NpcTemplate raid : NpcTable.getInstance().getAllNpcOfClassType("L2RaidBoss"))
		{
			int x = 0, y = 0, z = 0;
			final L2Spawn spawn = SpawnTable.getInstance().getFirstSpawn(raid.getNpcId());
			if (spawn != null)
			{
				x = spawn.getLocx();
				y = spawn.getLocy();
				z = spawn.getLocz();
			}
			RADAR.put(raid.getNpcId(), new Location(x, y, z));
		}
	}

	public static void main(String args[])
	{
		new RaidbossInfo(-1, qn, "custom");
	}
}