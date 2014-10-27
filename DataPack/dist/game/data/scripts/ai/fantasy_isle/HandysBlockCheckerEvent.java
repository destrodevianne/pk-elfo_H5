package ai.fantasy_isle;

import pk.elfo.Config;
import pk.elfo.gameserver.instancemanager.HandysBlockCheckerManager;
import pk.elfo.gameserver.instancemanager.HandysBlockCheckerManager.ArenaParticipantsHolder;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExCubeGameChangeTimeToStart;
import pk.elfo.gameserver.network.serverpackets.ExCubeGameRequestReady;
import pk.elfo.gameserver.network.serverpackets.ExCubeGameTeamList;

public class HandysBlockCheckerEvent extends Quest
{
	// Arena Managers
	private static final int A_MANAGER_1 = 32521;
	private static final int A_MANAGER_2 = 32522;
	private static final int A_MANAGER_3 = 32523;
	private static final int A_MANAGER_4 = 32524;
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if ((npc == null) || (player == null))
		{
			return null;
		}
		
		final int arena = npc.getNpcId() - A_MANAGER_1;
		if (eventIsFull(arena))
		{
			player.sendPacket(SystemMessageId.CANNOT_REGISTER_CAUSE_QUEUE_FULL);
			return null;
		}
		
		if (HandysBlockCheckerManager.getInstance().arenaIsBeingUsed(arena))
		{
			player.sendPacket(SystemMessageId.MATCH_BEING_PREPARED_TRY_LATER);
			return null;
		}
		
		if (HandysBlockCheckerManager.getInstance().addPlayerToArena(player, arena))
		{
			ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
			
			final ExCubeGameTeamList tl = new ExCubeGameTeamList(holder.getRedPlayers(), holder.getBluePlayers(), arena);
			
			player.sendPacket(tl);
			
			int countBlue = holder.getBlueTeamSize();
			int countRed = holder.getRedTeamSize();
			int minMembers = Config.MIN_BLOCK_CHECKER_TEAM_MEMBERS;
			
			if ((countBlue >= minMembers) && (countRed >= minMembers))
			{
				holder.updateEvent();
				holder.broadCastPacketToTeam(new ExCubeGameRequestReady());
				holder.broadCastPacketToTeam(new ExCubeGameChangeTimeToStart(10));
			}
		}
		return null;
	}
	
	private boolean eventIsFull(int arena)
	{
		if (HandysBlockCheckerManager.getInstance().getHolder(arena).getAllPlayers().size() == 12)
		{
			return true;
		}
		return false;
	}
	
	public HandysBlockCheckerEvent(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(A_MANAGER_1, A_MANAGER_2, A_MANAGER_3, A_MANAGER_4);
	}
	
	public static void main(String[] args)
	{
		if (!Config.ENABLE_BLOCK_CHECKER_EVENT)
		{
			_log.info("Handy's Block Checker Event is disabled");
		}
		else
		{
			new HandysBlockCheckerEvent(-1, HandysBlockCheckerEvent.class.getSimpleName(), "Handy's Block Checker Event");
			HandysBlockCheckerManager.getInstance().startUpParticipantsQueue();
			_log.info("Handy's Block Checker Event is enabled");
		}
	}
}