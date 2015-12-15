package ai.npc.Teleports.GatekeeperSpirit;

import pk.elfo.gameserver.SevenSigns;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class GatekeeperSpirit extends AbstractNpcAI
{
	// NPCs
	private final static int GATEKEEPER_SPIRIT_ENTER = 31111;
	private final static int GATEKEEPER_SPIRIT_EXIT = 31112;
	private final static int LILITH = 25283;
	private final static int ANAKIM = 25286;
	
	private GatekeeperSpirit()
	{
		super(GatekeeperSpirit.class.getSimpleName(), "ai/npc/Teleports/");
		addStartNpc(GATEKEEPER_SPIRIT_ENTER);
		addFirstTalkId(GATEKEEPER_SPIRIT_ENTER);
		addTalkId(GATEKEEPER_SPIRIT_ENTER);
		addKillId(LILITH, ANAKIM);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player.getObjectId());
		int sealAvariceOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE);
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		
		if ((playerCabal == sealAvariceOwner) && (playerCabal == compWinner))
		{
			switch (sealAvariceOwner)
			{
				case SevenSigns.CABAL_DAWN:
				{
					return "dawn.htm";
				}
				case SevenSigns.CABAL_DUSK:
				{
					return "dusk.htm";
				}
				case SevenSigns.CABAL_NULL:
				{
					npc.showChatWindow(player);
					break;
				}
			}
		}
		else
		{
			npc.showChatWindow(player);
		}
		return super.onFirstTalk(npc, player);
	}
	
	@Override
	/**
	 * TODO: Should be spawned 10 seconds after boss dead
	 */
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		int npcId = npc.getNpcId();
		if (npcId == LILITH)
		{
			// exit_necropolis_boss_lilith
			addSpawn(GATEKEEPER_SPIRIT_EXIT, 184410, -10111, -5488, 0, false, 900000);
		}
		else if (npcId == ANAKIM)
		{
			// exit_necropolis_boss_anakim
			addSpawn(GATEKEEPER_SPIRIT_EXIT, 184410, -13102, -5488, 0, false, 900000);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new GatekeeperSpirit();
	}
}