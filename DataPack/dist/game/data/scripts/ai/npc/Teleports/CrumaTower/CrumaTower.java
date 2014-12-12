package ai.npc.Teleports.CrumaTower;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class CrumaTower extends AbstractNpcAI
{
	// NPC
	private static final int MOZELLA = 30483;
	// Misc
	private static final int MAX_LEVEL = 55;
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		if (player.getLevel() <= MAX_LEVEL)
		{
			player.teleToLocation(17724, 114004, -11672);
			return null;
		}
		return "30483-1.htm";
	}
	
	private CrumaTower(String name, String descr)
	{
		super(name, descr);
		addStartNpc(MOZELLA);
		addTalkId(MOZELLA);
	}
	
	public static void main(String[] args)
	{
		new CrumaTower(CrumaTower.class.getSimpleName(), "ai/npc/Teleports/");
	}
}
