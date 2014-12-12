package ai.npc.Teleports.PaganTeleporters;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class PaganTeleporters extends AbstractNpcAI
{
	// NPCs
	private static final int TRIOLS_MIRROR_1 = 32039;
	private static final int TRIOLS_MIRROR_2 = 32040;
	// @formatter:off
	private static final int[] NPCS =
	{
		32034, 32035, 32036, 32037, 32039, 32040
	};
	// @formatter:on
	// Items
	private static final int VISITORS_MARK = 8064;
	private static final int FADED_VISITORS_MARK = 8065;
	private static final int PAGANS_MARK = 8067;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "Close_Door1":
			{
				closeDoor(19160001, 0);
				break;
			}
			case "Close_Door2":
			{
				closeDoor(19160010, 0);
				closeDoor(19160011, 0);
				break;
			}
		}
		return "";
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		switch (npc.getNpcId())
		{
			case TRIOLS_MIRROR_1:
			{
				player.teleToLocation(-12766, -35840, -10856);
				break;
			}
			case TRIOLS_MIRROR_2:
			{
				player.teleToLocation(36640, -51218, 718);
				break;
			}
		}
		return "";
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		switch (npc.getNpcId())
		{
			case 32034:
			{
				if (!hasAtLeastOneQuestItem(player, VISITORS_MARK, FADED_VISITORS_MARK, PAGANS_MARK))
				{
					return "noItem.htm";
				}
				openDoor(19160001, 0);
				startQuestTimer("Close_Door1", 10000, null, null);
				return "FadedMark.htm";
			}
			case 32035:
			{
				openDoor(19160001, 0);
				startQuestTimer("Close_Door1", 10000, null, null);
				return "FadedMark.htm";
			}
			case 32036:
			{
				if (!hasQuestItems(player, PAGANS_MARK))
				{
					return "noMark.htm";
				}
				startQuestTimer("Close_Door2", 10000, null, null);
				openDoor(19160010, 0);
				openDoor(19160011, 0);
				return "openDoor.htm";
			}
			case 32037:
			{
				openDoor(19160010, 0);
				openDoor(19160011, 0);
				startQuestTimer("Close_Door2", 10000, null, null);
				return "FadedMark.htm";
			}
		}
		return super.onTalk(npc, player);
	}
	
	private PaganTeleporters()
	{
		super(PaganTeleporters.class.getSimpleName(), "ai/npc/Teleports/");
		addStartNpc(NPCS);
		addTalkId(NPCS);
		addFirstTalkId(TRIOLS_MIRROR_1, TRIOLS_MIRROR_2);
	}
	
	public static void main(String[] args)
	{
		new PaganTeleporters();
	}
}