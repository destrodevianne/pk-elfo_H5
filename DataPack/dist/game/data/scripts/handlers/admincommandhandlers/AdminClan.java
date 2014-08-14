package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.ClanHallManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.instancemanager.SiegeManager;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.L2ClanMember;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.communityserver.CommunityServerThread;
import pk.elfo.gameserver.network.communityserver.writepackets.WorldInfo;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.util.Util;

/**
 * PkElfo
 */

public class AdminClan implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_clan_info",
		"admin_clan_changeleader"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String cmd = st.nextToken();
		if (cmd.startsWith("admin_clan_info"))
		{
			String val;
			L2PcInstance player = null;
			if (st.hasMoreTokens())
			{
				val = st.nextToken();
				// From the HTML we receive player's object Id.
				if (Util.isDigit(val))
				{
					player = L2World.getInstance().getPlayer(Integer.parseInt(val));
					if (player == null)
					{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
						return false;
					}
				}
				else
				{
					player = L2World.getInstance().getPlayer(val);
					if (player == null)
					{
						activeChar.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
						return false;
					}
				}
			}
			else
			{
				L2Object targetObj = activeChar.getTarget();
				if (targetObj instanceof L2PcInstance)
				{
					player = targetObj.getActingPlayer();
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
					return false;
				}
			}
			
			final L2Clan clan = player.getClan();
			if (clan == null)
			{
				activeChar.sendPacket(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
				return false;
			}
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			final String htm = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/admin/claninfo.htm");
			html.setHtml(htm.toString());
			html.replace("%clan_name%", clan.getName());
			html.replace("%clan_leader%", clan.getLeaderName());
			html.replace("%clan_level%", String.valueOf(clan.getLevel()));
			html.replace("%clan_has_castle%", clan.getCastleId() > 0 ? CastleManager.getInstance().getCastleById(clan.getCastleId()).getName() : "No");
			html.replace("%clan_has_clanhall%", clan.getHideoutId() > 0 ? ClanHallManager.getInstance().getClanHallById(clan.getHideoutId()).getName() : "No");
			html.replace("%clan_has_fortress%", clan.getFortId() > 0 ? FortManager.getInstance().getFortById(clan.getFortId()).getName() : "No");
			html.replace("%clan_points%", String.valueOf(clan.getReputationScore()));
			html.replace("%clan_players_count%", String.valueOf(clan.getMembersCount()));
			html.replace("%clan_ally%", clan.getAllyId() > 0 ? clan.getAllyName() : "Not in ally");
			html.replace("%current_player_objectId%", String.valueOf(player.getObjectId()));
			html.replace("%current_player_name%", player.getName());
			activeChar.sendPacket(html);
		}
		else if (cmd.startsWith("admin_clan_changeleader"))
		{
			String val;
			L2PcInstance player = null;
			if (st.hasMoreTokens())
			{
				val = st.nextToken();
				// From the HTML we receive player's object Id.
				if (Util.isDigit(val))
				{
					player = L2World.getInstance().getPlayer(Integer.parseInt(val));
					if (player == null)
					{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
						return false;
					}
				}
				else
				{
					player = L2World.getInstance().getPlayer(val);
					if (player == null)
					{
						activeChar.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
						return false;
					}
				}
			}
			else
			{
				L2Object targetObj = activeChar.getTarget();
				if (targetObj instanceof L2PcInstance)
				{
					player = targetObj.getActingPlayer();
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
					return false;
				}
			}
			
			final L2Clan clan = player.getClan();
			if (clan == null)
			{
				activeChar.sendPacket(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
				return false;
			}
			
			final L2ClanMember member = clan.getClanMember(player.getObjectId());
			if (member != null)
			{
				if ((clan.getLeader() != null) && (clan.getLeader().getPlayerInstance() != null))
				{
					final L2PcInstance exLeader = clan.getLeader().getPlayerInstance();
					SiegeManager.getInstance().removeSiegeSkills(exLeader);
					exLeader.setClan(clan);
					exLeader.setClanPrivileges(L2Clan.CP_NOTHING);
					exLeader.broadcastUserInfo();
					exLeader.setPledgeClass(L2ClanMember.calculatePledgeClass(exLeader));
					exLeader.broadcastUserInfo();
					exLeader.checkItemRestriction();
				}
				else if (clan.getLeaderId() > 0)
				{
					try (Connection con = L2DatabaseFactory.getInstance().getConnection())
					{
						PreparedStatement statement = con.prepareStatement("UPDATE characters SET clan_privs = ? WHERE charId = ?");
						statement.setInt(1, L2Clan.CP_NOTHING);
						statement.setInt(2, clan.getLeaderId());
						statement.execute();
						
						if (statement.getUpdateCount() == 0)
						{
							activeChar.sendPacket(SystemMessageId.ID_NOT_EXIST);
						}
						statement.close();
					}
					catch (Exception e)
					{
						activeChar.sendPacket(SystemMessageId.NOT_WORKING_PLEASE_TRY_AGAIN_LATER);
					}
				}
				
				clan.setLeader(member);
				clan.updateClanInDB();
				
				player.setClan(clan);
				player.setPledgeClass(L2ClanMember.calculatePledgeClass(player));
				player.setClanPrivileges(L2Clan.CP_ALL);
				
				if (clan.getLevel() >= SiegeManager.getInstance().getSiegeClanMinLevel())
				{
					SiegeManager.getInstance().addSiegeSkills(player);
				}
				
				player.broadcastUserInfo();
				clan.broadcastClanStatus();
				
				final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_LEADER_PRIVILEGES_HAVE_BEEN_TRANSFERRED_TO_C1);
				sm.addString(player.getName());
				clan.broadcastToOnlineMembers(sm);
				activeChar.sendPacket(sm);
				CommunityServerThread.getInstance().sendPacket(new WorldInfo(null, clan, WorldInfo.TYPE_UPDATE_CLAN_DATA));
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
