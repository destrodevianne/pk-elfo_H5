package ai.npc.Teleports.SteelCitadelTeleport;

import pk.elfo.Config;
import pk.elfo.gameserver.instancemanager.GrandBossManager;
import pk.elfo.gameserver.instancemanager.ZoneManager;
import pk.elfo.gameserver.model.L2CommandChannel;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.zone.type.L2BossZone;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class SteelCitadelTeleport extends AbstractNpcAI
{
	// NPCs
	private static final int BELETH = 29118;
	private static final int NAIA_CUBE = 32376;
	
	private SteelCitadelTeleport()
	{
		super(SteelCitadelTeleport.class.getSimpleName(), "ai/npc/Teleports/");
		addStartNpc(NAIA_CUBE);
		addTalkId(NAIA_CUBE);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final int belethStatus = GrandBossManager.getInstance().getBossStatus(BELETH);
		if (belethStatus == 3)
		{
			return "32376-02.htm";
		}
		
		if (belethStatus > 0)
		{
			return "32376-03.htm";
		}
		
		final L2CommandChannel channel = player.getParty() == null ? null : player.getParty().getCommandChannel();
		if ((channel == null) || (channel.getLeader().getObjectId() != player.getObjectId()) || (channel.getMemberCount() < Config.BELETH_MIN_PLAYERS))
		{
			return "32376-02a.htm";
		}
		
		final L2BossZone zone = (L2BossZone) ZoneManager.getInstance().getZoneById(12018);
		if (zone != null)
		{
			GrandBossManager.getInstance().setBossStatus(BELETH, 1);
			
			for (L2Party party : channel.getPartys())
			{
				if (party == null)
				{
					continue;
				}
				
				for (L2PcInstance pl : party.getMembers())
				{
					if (pl.isInsideRadius(npc.getX(), npc.getY(), npc.getZ(), 3000, true, false))
					{
						zone.allowPlayerEntry(pl, 30);
						pl.teleToLocation(16342, 209557, -9352, true);
					}
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new SteelCitadelTeleport();
	}
}
