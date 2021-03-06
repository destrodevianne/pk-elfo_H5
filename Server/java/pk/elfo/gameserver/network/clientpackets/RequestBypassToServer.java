package pk.elfo.gameserver.network.clientpackets;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.communitybbs.CommunityBoard;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.datatables.AIOItemTable;
import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.handler.AdminCommandHandler;
import pk.elfo.gameserver.handler.BypassHandler;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.masteriopack.rankpvpsystem.RPSBypass;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MerchantSummonInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Hero;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.communityserver.CommunityServerThread;
import pk.elfo.gameserver.network.communityserver.writepackets.RequestShowCommunityBoard;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.ConfirmDlg;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.scripting.scriptengine.events.RequestBypassToServerEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.talk.RequestBypassToServerListener;
import pk.elfo.gameserver.util.GMAudit;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

/**
 * PkElfo
 */

public final class RequestBypassToServer extends L2GameClientPacket
{
	private static final String _C__23_REQUESTBYPASSTOSERVER = "[C] 23 RequestBypassToServer";
	private static final List<RequestBypassToServerListener> _listeners = new FastList<RequestBypassToServerListener>().shared();
	
	// S
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getServerBypass().tryPerformAction(_command))
		{
			return;
		}
		
		if (_command.isEmpty())
		{
			_log.info(activeChar.getName() + " send empty requestbypass");
			activeChar.logout();
			return;
		}
		
		try
		{
			if (_command.startsWith("admin_"))
			{
				String command = _command.split(" ")[0];
				
				IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(command);
				
				if (ach == null)
				{
					if (activeChar.isGM())
					{
						activeChar.sendMessage("The command " + command.substring(6) + " does not exist!");
					}
					_log.warning(activeChar + " requested not registered admin command '" + command + "'");
					return;
				}
				
				if (!AdminTable.getInstance().hasAccess(command, activeChar.getAccessLevel()))
				{
					activeChar.sendMessage("You don't have the access rights to use this command!");
					_log.warning("Character " + activeChar.getName() + " tried to use admin command " + command + ", without proper access level!");
					return;
				}
				
				if (AdminTable.getInstance().requireConfirm(command))
				{
					activeChar.setAdminConfirmCmd(_command);
					ConfirmDlg dlg = new ConfirmDlg(SystemMessageId.S1);
					dlg.addString("Are you sure you want execute command " + _command.substring(6) + " ?");
					activeChar.sendPacket(dlg);
				}
				else
				{
					if (Config.GMAUDIT)
					{
						GMAudit.auditGMAction(activeChar.getName() + " [" + activeChar.getObjectId() + "]", _command, (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target"));
					}
					
					ach.useAdminCommand(_command, activeChar);
				}
			}
			else if (_command.equals("come_here") && activeChar.isGM())
			{
				comeHere(activeChar);
			}
			else if (_command.startsWith("npc_"))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
				{
					id = _command.substring(4, endOfId);
				}
				else
				{
					id = _command.substring(4);
				}
				if (Util.isDigit(id))
				{
					L2Object object = L2World.getInstance().findObject(Integer.parseInt(id));
					
					if ((object != null) && object.isNpc() && (endOfId > 0) && activeChar.isInsideRadius(object, L2Npc.INTERACTION_DISTANCE, false, false))
					{
						((L2Npc) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					}
				}
				
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			}
			else if (_command.startsWith("item_"))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
				{
					id = _command.substring(5, endOfId);
				}
				else
				{
					id = _command.substring(5);
				}
				try
				{
					L2ItemInstance item = activeChar.getInventory().getItemByObjectId(Integer.parseInt(id));
					
					if ((item != null) && (endOfId > 0))
					{
						item.onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					}
					
					activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				}
				catch (NumberFormatException nfe)
				{
					_log.log(Level.WARNING, "NFE for command [" + _command + "]", nfe);
				}
			}
			else if (_command.startsWith("summon_"))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				int endOfId = _command.indexOf('_', 8);
				String id;
				
				if (endOfId > 0)
				{
					id = _command.substring(7, endOfId);
				}
				else
				{
					id = _command.substring(7);
				}
				
				if (Util.isDigit(id))
				{
					L2Object object = L2World.getInstance().findObject(Integer.parseInt(id));
					
					if ((object instanceof L2MerchantSummonInstance) && (endOfId > 0) && activeChar.isInsideRadius(object, L2Npc.INTERACTION_DISTANCE, false, false))
					{
						((L2MerchantSummonInstance) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					}
				}
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			}
			// Navigate through Manor windows
			else if (_command.startsWith("manor_menu_select"))
			{
				final IBypassHandler manor = BypassHandler.getInstance().getHandler("manor_menu_select");
				if (manor != null)
				{
					manor.useBypass(_command, activeChar, null);
				}
			}
			else if (_command.startsWith("_bbs"))
			{
				if (Config.ENABLE_COMMUNITY_BOARD)
				{
					if (!CommunityServerThread.getInstance().sendPacket(new RequestShowCommunityBoard(activeChar.getObjectId(), _command)))
					{
						activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
					}
				}
				else
				{
					CommunityBoard.getInstance().handleCommands(getClient(), _command);
				}
			}
			else if (_command.startsWith("eventmanager "))
			{
				EventsInterface.bypass(activeChar.getObjectId(), _command.substring(13));
			}
			else if (_command.startsWith("bbs"))
			{
				if (Config.ENABLE_COMMUNITY_BOARD)
				{
					if (!CommunityServerThread.getInstance().sendPacket(new RequestShowCommunityBoard(activeChar.getObjectId(), _command)))
					{
						activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
					}
				}
				else
				{
					CommunityBoard.getInstance().handleCommands(getClient(), _command);
				}
			}
			else if (_command.startsWith("_mail"))
			{
				if (!CommunityServerThread.getInstance().sendPacket(new RequestShowCommunityBoard(activeChar.getObjectId(), "_bbsmail")))
				{
					activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
				}
			}
			else if (_command.startsWith("_friend"))
			{
				if (!CommunityServerThread.getInstance().sendPacket(new RequestShowCommunityBoard(activeChar.getObjectId(), "_bbsfriend")))
				{
					activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
				}
			}
			else if (_command.startsWith("Quest "))
			{
				if (!activeChar.validateBypass(_command))
				{
					return;
				}
				
				String p = _command.substring(6).trim();
				int idx = p.indexOf(' ');
				if (idx < 0)
				{
					activeChar.processQuestEvent(p, "");
				}
				else
				{
					activeChar.processQuestEvent(p.substring(0, idx), p.substring(idx).trim());
				}
			}
			else if (_command.startsWith("_match"))
			{
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
				{
					Hero.getInstance().showHeroFights(activeChar, heroclass, heroid, heropage);
				}
			}
			else if (_command.startsWith("_diary"))
			{
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
				{
					Hero.getInstance().showHeroDiary(activeChar, heroclass, heroid, heropage);
				}
			}
			else if (_command.startsWith("_olympiad?command"))
			{
				int arenaId = Integer.parseInt(_command.split("=")[2]);
				final IBypassHandler handler = BypassHandler.getInstance().getHandler("arenachange");
				if (handler != null)
				{
					handler.useBypass("arenachange " + (arenaId - 1), activeChar, null);
				}
			}
			
			else if (_command.startsWith("RPS."))
			{
				RPSBypass.executeCommand(activeChar, _command);
			}
			
			else if (_command.startsWith("Aioitem"))
			{
				AIOItemTable.getInstance().handleBypass(activeChar, _command);
			}
			else
			{
				final IBypassHandler handler = BypassHandler.getInstance().getHandler(_command);
				if (handler != null)
				{
					handler.useBypass(_command, activeChar, null);
				}
				else
				{
					_log.log(Level.WARNING, getClient() + " sent not handled RequestBypassToServer: [" + _command + "]");
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, getClient() + " sent bad RequestBypassToServer: \"" + _command + "\"", e);
			if (activeChar.isGM())
			{
				StringBuilder sb = new StringBuilder(200);
				sb.append("<html><body>");
				sb.append("Bypass error: " + e + "<br1>");
				sb.append("Bypass command: " + _command + "<br1>");
				sb.append("StackTrace:<br1>");
				for (StackTraceElement ste : e.getStackTrace())
				{
					sb.append(ste.toString() + "<br1>");
				}
				sb.append("</body></html>");
				// item html
				NpcHtmlMessage msg = new NpcHtmlMessage(0, 12807);
				msg.setHtml(sb.toString());
				msg.disableValidation();
				activeChar.sendPacket(msg);
			}
		}
		
		fireBypassListeners();
	}
	
	/**
	 * @param activeChar
	 */
	private static void comeHere(L2PcInstance activeChar)
	{
		L2Object obj = activeChar.getTarget();
		if (obj == null)
		{
			return;
		}
		if (obj instanceof L2Npc)
		{
			L2Npc temp = (L2Npc) obj;
			temp.setTarget(activeChar);
			temp.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(activeChar.getX(), activeChar.getY(), activeChar.getZ(), 0));
		}
	}
	
	/**
	 * Fires the event when packet arrived.
	 */
	private void fireBypassListeners()
	{
		RequestBypassToServerEvent event = new RequestBypassToServerEvent();
		event.setActiveChar(getActiveChar());
		event.setCommand(_command);
		
		for (RequestBypassToServerListener listener : _listeners)
		{
			listener.onRequestBypassToServer(event);
		}
	}
	
	/**
	 * @param listener
	 */
	public static void addBypassListener(RequestBypassToServerListener listener)
	{
		if (!_listeners.contains(listener))
		{
			_listeners.add(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public static void removeBypassListener(RequestBypassToServerListener listener)
	{
		if (_listeners.contains(listener))
		{
			_listeners.remove(listener);
		}
	}
	
	@Override
	public String getType()
	{
		return _C__23_REQUESTBYPASSTOSERVER;
	}
}
