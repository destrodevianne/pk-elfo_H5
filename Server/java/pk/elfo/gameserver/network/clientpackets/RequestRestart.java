package pk.elfo.gameserver.network.clientpackets;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.SevenSignsFestival;
import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.instancemanager.AntiFeedManager;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.PkHunterEvent;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone1;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone2;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone3;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone4;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone5;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.L2GameClient.GameClientState;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.CharSelectionInfo;
import pk.elfo.gameserver.network.serverpackets.RestartResponse;
import pk.elfo.gameserver.scripting.scriptengine.listeners.player.PlayerDespawnListener;
import pk.elfo.gameserver.taskmanager.AttackStanceTaskManager;
import javolution.util.FastList;

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
		if (L2PcInstance._isbuffrefusal == true)
		{
			L2PcInstance._isbuffrefusal = false;
		}
		
		if (PkHunterEvent.isPk(player))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			sendPacket(RestartResponse.valueOf(false));
			return;
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
		
		// MultiFunction Zone inicio
		if (player.isInsideZone(ZoneId.MULTI_FUNCTION) && !L2MultiFunctionZone.restart_zone)
		{
			player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
			sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (player.isInsideZone(ZoneId.MULTI_FUNCTION1) && !L2MultiFunctionZone1.restart_zone1)
        {
            player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
            sendPacket(RestartResponse.valueOf(false));
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION2) && !L2MultiFunctionZone2.restart_zone2)
        {
            player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
            sendPacket(RestartResponse.valueOf(false));
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION3) && !L2MultiFunctionZone3.restart_zone3)
        {
            player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
            sendPacket(RestartResponse.valueOf(false));
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION4) && !L2MultiFunctionZone4.restart_zone4)
        {
            player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
            sendPacket(RestartResponse.valueOf(false));
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION5) && !L2MultiFunctionZone5.restart_zone5)
        {
            player.sendMessage("Voce nao pode reiniciar enquanto estiver dentro de uma Multifunction zone.");
            sendPacket(RestartResponse.valueOf(false));
            return;
        }
		// MultiFunction Zone fim
		
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