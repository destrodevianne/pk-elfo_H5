package ai.npc.Teleports.NewbieTravelToken;

import java.util.Map;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class NewbieTravelToken extends AbstractNpcAI
{
	// Item
	private static final int NEWBIE_TRAVEL_TOKEN = 8542;
	// NPC Id - Teleport Location
	private static final Map<Integer, Location> DATA = new FastMap<>();
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (Util.isDigit(event))
		{
			final int npcId = Integer.parseInt(event);
			if (DATA.keySet().contains(npcId))
			{
				if (hasQuestItems(player, NEWBIE_TRAVEL_TOKEN))
				{
					takeItems(player, NEWBIE_TRAVEL_TOKEN, 1);
					player.teleToLocation(DATA.get(npcId), false);
				}
				else
				{
					player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT);
				}
				return super.onAdvEvent(event, npc, player);
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		return player.getLevel() >= 20 ? "cant-travel.htm" : npc.getNpcId() + ".htm";
	}
	
	private NewbieTravelToken(String name, String descr)
	{
		super(name, descr);
		// Initialize Map
		DATA.put(30600, new Location(12160, 16554, -4583)); // DE
		DATA.put(30601, new Location(115594, -177993, -912)); // DW
		DATA.put(30599, new Location(45470, 48328, -3059)); // EV
		DATA.put(30602, new Location(-45067, -113563, -199)); // OV
		DATA.put(30598, new Location(-84053, 243343, -3729)); // TI
		DATA.put(32135, new Location(-119712, 44519, 368)); // SI
		
		for (int npcId : DATA.keySet())
		{
			addStartNpc(npcId);
			addTalkId(npcId);
		}
	}
	
	public static void main(String[] args)
	{
		new NewbieTravelToken(NewbieTravelToken.class.getSimpleName(), "ai/npc/Teleports/");
	}
}