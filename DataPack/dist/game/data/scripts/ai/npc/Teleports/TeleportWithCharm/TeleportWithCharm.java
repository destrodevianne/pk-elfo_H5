package ai.npc.Teleports.TeleportWithCharm;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class TeleportWithCharm extends AbstractNpcAI
{
	// NPCs
	private final static int WHIRPY = 30540;
	private final static int TAMIL = 30576;
	// Items
	private final static int ORC_GATEKEEPER_CHARM = 1658;
	private final static int DWARF_GATEKEEPER_TOKEN = 1659;
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		switch (npc.getNpcId())
		{
			case WHIRPY:
			{
				if (hasQuestItems(player, DWARF_GATEKEEPER_TOKEN))
				{
					takeItems(player, DWARF_GATEKEEPER_TOKEN, 1);
					player.teleToLocation(-80826, 149775, -3043);
				}
				else
				{
					return "30540-01.htm";
				}
				break;
			}
			case TAMIL:
			{
				if (hasQuestItems(player, ORC_GATEKEEPER_CHARM))
				{
					takeItems(player, ORC_GATEKEEPER_CHARM, 1);
					player.teleToLocation(-80826, 149775, -3043);
				}
				else
				{
					return "30576-01.htm";
				}
				break;
			}
		}
		return super.onTalk(npc, player);
	}
	
	private TeleportWithCharm(String name, String descr)
	{
		super(name, descr);
		addStartNpc(WHIRPY, TAMIL);
		addTalkId(WHIRPY, TAMIL);
	}
	
	public static void main(String[] args)
	{
		new TeleportWithCharm(TeleportWithCharm.class.getSimpleName(), "ai/npc/Teleports/");
	}
}