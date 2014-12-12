package ai.npc.Teleports.StrongholdsTeleports;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class StrongholdsTeleports extends AbstractNpcAI
{
	// NPCs
	private final static int[] NPCs =
	{
		32163,
		32181,
		32184,
		32186
	};
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return player.getLevel() < 20 ? String.valueOf(npc.getNpcId()) + ".htm" : String.valueOf(npc.getNpcId()) + "-no.htm";
	}
	
	private StrongholdsTeleports(String name, String descr)
	{
		super(name, descr);
		addFirstTalkId(NPCs);
	}
	
	public static void main(String[] args)
	{
		new StrongholdsTeleports(StrongholdsTeleports.class.getSimpleName(), "ai/npc/Teleports/");
	}
}