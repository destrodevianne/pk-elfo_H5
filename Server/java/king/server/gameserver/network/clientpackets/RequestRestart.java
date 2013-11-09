/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.network.clientpackets;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javolution.util.FastList;

import king.server.Config;
import king.server.gameserver.SevenSignsFestival;
import king.server.gameserver.events.EventsInterface;
import king.server.gameserver.instancemanager.AntiFeedManager;
import king.server.gameserver.model.L2Party;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.L2GameClient;
import king.server.gameserver.network.L2GameClient.GameClientState;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.CharSelectionInfo;
import king.server.gameserver.network.serverpackets.RestartResponse;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerDespawnListener;
import king.server.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * This class ...
 * @version $Revision: 1.11.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestRestart extends L2GameClientPacket
{
	private static final String _C__57_REQUESTRESTART = "[C] 57 RequestRestart";
	protected static final Logger _logAccounting = Logger.getLogger("accounting");
	private static List<PlayerDespawnListener> despawnListeners = new FastList<>();
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		if ((player.getActiveEnchantItem() != null) || (player.getActiveEnchantAttrItem() != null))
		{
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (L2PcInstance._isoneffect == true)
		{
			L2PcInstance._isoneffect = false;
		}
      
		if (L2PcInstance._istraderefusal == true)
		{
			L2PcInstance._istraderefusal = false;
		}
	              
		if (L2PcInstance._ispmrefusal == true)
		{
			L2PcInstance._ispmrefusal = false;
		}
	             
		if (L2PcInstance._isexpsprefusal == true)
		{
			L2PcInstance._isexpsprefusal = false;
		}
			
		if (player.isLocked())
		{
			_log.warning("Player " + player.getName() + " tried to restart during class change.");
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (player.getPrivateStoreType() != L2PcInstance.STORE_PRIVATE_NONE)
		{
			player.sendMessage("Cannot restart while trading");
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) && !(player.isGM() && Config.GM_RESTART_FIGHTING))
		{
			if (Config.DEBUG)
			{
				_log.fine("Player " + player.getName() + " tried to logout while fighting.");
			}
			
			player.sendPacket(SystemMessageId.CANT_RESTART_WHILE_FIGHTING);
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (EventsInterface.logout(player.getObjectId()))
		{
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Prevent player from restarting if they are a festival participant
		// and it is in progress, otherwise notify party members that the player
		// is not longer a participant.
		if (player.isFestivalParticipant())
		{
			if (SevenSignsFestival.getInstance().isFestivalInitialized())
			{
				player.sendMessage("You cannot restart while you are a participant in a festival.");
				sendPacket(RestartResponse.valueOf(false));
				return;
			}
			
			final L2Party playerParty = player.getParty();
			
			if (playerParty != null)
			{
				player.getParty().broadcastString(player.getName() + " has been removed from the upcoming festival.");
			}
		}
		
		for (PlayerDespawnListener listener : despawnListeners)
		{
			listener.onDespawn(player);
		}
		
		// Remove player from Boss Zone
		player.removeFromBossZone();
		
		final L2GameClient client = getClient();
		
		LogRecord record = new LogRecord(Level.INFO, "Logged out");
		record.setParameters(new Object[]
		{
			client
		});
		_logAccounting.log(record);
		
		// detach the client from the char so that the connection isnt closed in the deleteMe
		player.setClient(null);
		
		player.deleteMe();
		
		client.setActiveChar(null);
		AntiFeedManager.getInstance().onDisconnect(client);
		
		// return the client to the authed status
		client.setState(GameClientState.AUTHED);
		
		sendPacket(RestartResponse.valueOf(true));
		
		// send char list
		final CharSelectionInfo cl = new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1);
		sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
	}
	
	@Override
	public String getType()
	{
		return _C__57_REQUESTRESTART;
	}
	
	// Listeners
	/**
	 * Adds a despawn listener which will get triggered when a player despawns
	 * @param listener
	 */
	public static void addDespawnListener(PlayerDespawnListener listener)
	{
		if (!despawnListeners.contains(listener))
		{
			despawnListeners.add(listener);
		}
	}
	
	/**
	 * Removes a despawn listener
	 * @param listener
	 */
	public static void removeDespawnListener(PlayerDespawnListener listener)
	{
		despawnListeners.remove(listener);
	}
}