package ai.npc.Teleports.ElrokiTeleporters;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class ElrokiTeleporters extends AbstractNpcAI
{
	// NPCs
	private static final int ORAHOCHIN = 32111;
	private static final int GARIACHIN = 32112;
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		switch (npc.getNpcId())
		{
			case ORAHOCHIN:
			{
				if (player.isInCombat())
				{
					return "32111-no.htm";
				}
				player.teleToLocation(4990, -1879, -3178);
				break;
			}
			case GARIACHIN:
			{
				player.teleToLocation(7557, -5513, -3221);
				break;
			}
		}
		return super.onTalk(npc, player);
	}
	
	private ElrokiTeleporters()
	{
		super(ElrokiTeleporters.class.getSimpleName(), "ai/npc/Teleports/");
		addStartNpc(ORAHOCHIN, GARIACHIN);
		addTalkId(ORAHOCHIN, GARIACHIN);
	}
	
	public static void main(String[] args)
	{
		new ElrokiTeleporters();
	}
}
