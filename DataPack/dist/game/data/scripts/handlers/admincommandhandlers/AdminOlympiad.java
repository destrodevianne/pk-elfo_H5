package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */

public class AdminOlympiad implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_addolypoints",
		"admin_removeolypoints",
		"admin_setolypoints",
		"admin_getolypoints"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_addolypoints"))
		{
			try
			{
				String val = command.substring(19);
				L2Object target = activeChar.getTarget();
				L2PcInstance player = null;
				if (target instanceof L2PcInstance)
				{
					player = (L2PcInstance) target;
					if (player.isNoble())
					{
						StatsSet playerStat = Olympiad.getNobleStats(player.getObjectId());
						if (playerStat == null)
						{
							activeChar.sendMessage("This player hasn't played on Olympiad yet!");
							return false;
						}
						int oldpoints = Olympiad.getInstance().getNoblePoints(player.getObjectId());
						int points = oldpoints + Integer.parseInt(val);
						if (points > 1000)
						{
							activeChar.sendMessage("You can't set more than 1000 or less than 0 Olympiad points!");
							return false;
						}
						playerStat.set("olympiad_points", points);
						
						activeChar.sendMessage("Player " + player.getName() + " now has " + points + "Olympiad points.");
					}
					else
					{
						activeChar.sendMessage("This player is not noblesse!");
						return false;
					}
				}
				else
				{
					activeChar.sendMessage("Usage: target a player and write the amount of points you would like to add.");
					activeChar.sendMessage("Example: //addolypoints 10");
					activeChar.sendMessage("However, keep in mind that you can't have less than 0 or more than 1000 points.");
				}
				
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //addolypoints points");
			}
		}
		else if (command.startsWith("admin_removeolypoints"))
		{
			try
			{
				String val = command.substring(22);
				L2Object target = activeChar.getTarget();
				L2PcInstance player = null;
				if (target instanceof L2PcInstance)
				{
					player = (L2PcInstance) target;
					if (player.isNoble())
					{
						StatsSet playerStat = Olympiad.getNobleStats(player.getObjectId());
						if (playerStat == null)
						{
							activeChar.sendMessage("This player hasn't played on Olympiad yet!");
							return false;
						}
						int oldpoints = Olympiad.getInstance().getNoblePoints(player.getObjectId());
						int points = oldpoints - Integer.parseInt(val);
						if (points < 0)
						{
							points = 0;
						}
						
						playerStat.set("olympiad_points", points);
						
						activeChar.sendMessage("Player " + player.getName() + " now has " + points + " Olympiad points.");
					}
					else
					{
						activeChar.sendMessage("This player is not noblesse!");
						return false;
					}
				}
				else
				{
					activeChar.sendMessage("Usage: target a player and write the amount of points you would like to remove.");
					activeChar.sendMessage("Example: //removeolypoints 10");
					activeChar.sendMessage("However, keep in mind that you can't have less than 0 or more than 1000 points.");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //removeolypoints points");
			}
		}
		else if (command.startsWith("admin_setolypoints"))
		{
			try
			{
				String val = command.substring(19);
				L2Object target = activeChar.getTarget();
				L2PcInstance player = null;
				if (target instanceof L2PcInstance)
				{
					player = (L2PcInstance) target;
					if (player.isNoble())
					{
						StatsSet playerStat = Olympiad.getNobleStats(player.getObjectId());
						if (playerStat == null)
						{
							activeChar.sendMessage("This player hasn't played on Olympiad yet!");
							return false;
						}
						if ((Integer.parseInt(val) < 1) && (Integer.parseInt(val) > 1000))
						{
							activeChar.sendMessage("You can't set more than 1000 or less than 0 Olympiad points! or lower then 0");
							return false;
						}
						playerStat.set("olympiad_points", Integer.parseInt(val));
						activeChar.sendMessage("Player " + player.getName() + " now has " + Integer.parseInt(val) + " Olympiad points.");
					}
					else
					{
						activeChar.sendMessage("This player is not noblesse!");
						return false;
					}
				}
				else
				{
					activeChar.sendMessage("Usage: target a player and write the amount of points you would like to set.");
					activeChar.sendMessage("Example: //setolypoints 10");
					activeChar.sendMessage("However, keep in mind that you can't have less than 0 or more than 1000 points.");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //setolypoints points");
			}
		}
		else if (command.startsWith("admin_getolypoints"))
		{
			try
			{
				L2Object target = activeChar.getTarget();
				L2PcInstance player = null;
				if (target instanceof L2PcInstance)
				{
					player = (L2PcInstance) target;
					if (player.isNoble())
					{
						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.THE_CURRENT_RECORD_FOR_THIS_OLYMPIAD_SESSION_IS_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS);
						sm.addNumber(Olympiad.getInstance().getCompetitionDone(player.getObjectId()));
						sm.addNumber(Olympiad.getInstance().getCompetitionWon(player.getObjectId()));
						sm.addNumber(Olympiad.getInstance().getCompetitionLost(player.getObjectId()));
						sm.addNumber(Olympiad.getInstance().getNoblePoints(player.getObjectId()));
						activeChar.sendPacket(sm);
					}
					else
					{
						activeChar.sendMessage("This player is not noblesse!");
						return false;
					}
				}
				else
				{
					activeChar.sendMessage("You must target a player to use the command.");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //getolypoints");
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}