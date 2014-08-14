package pk.elfo.gameserver.network.clientpackets;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.SevenSignsFestival;
import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.RandomFight;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.L2Event;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone1;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone2;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone3;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone4;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone5;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * This class ...
 * @version $Revision: 1.9.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class Logout extends L2GameClientPacket
{
	private static final String _C__00_LOGOUT = "[C] 00 Logout";
	protected static final Logger _logAccounting = Logger.getLogger("accounting");
	
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	protected void runImpl()
	{
		// Don't allow leaving if player is fighting
		final L2PcInstance player = getClient().getActiveChar();
		
		if (player == null)
		{
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
		
		
		if ((player.getActiveEnchantItem() != null) || (player.getActiveEnchantAttrItem() != null))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		if (player.isLocked())
		{
			_log.warning("Player " + player.getName() + " tried to logout during class change.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Random Fight Event
		if(RandomFight.players.contains(player))
		{
			player.sendMessage("Voce nao pode sair estando participando do Evento Random Fight.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player))
		{
			if (player.isGM() && Config.GM_RESTART_FIGHTING)
			{
				return;
			}
			
			if (Config.DEBUG)
			{
				_log.fine("Player " + player.getName() + " tried to logout while fighting");
			}
			
			player.sendPacket(SystemMessageId.CANT_LOGOUT_WHILE_FIGHTING);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (L2Event.isParticipant(player))
		{
			player.sendMessage("A superior power doesn't allow you to leave the event.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (EventsInterface.logout(player.getObjectId()))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (Config.ANNOUNCE_AIOX_DESCONECT)
		{
			if (player.isAio())
			{
				Announcements.getInstance().announceToAll("O AIO " + player.getName() + " acabou de deslogar.");
			}
		}
		
		if (Config.ANNOUNCE_VIP_DESCONECT)
		{
			if (player.isVip())
			{
				Announcements.getInstance().announceToAll("O AIO " + player.getName() + " acabou de deslogar.");
			}
		}
		
		if (Config.ANNOUNCE_HERO_DESCONECT)
		{
			if (player.isHero())
			{
				Announcements.getInstance().announceToAll("Hero: " + player.getName() + " acabou de deslogar.");
			}
		}
		
		// MultiFunction Zone inicio
		if (player.isInsideZone(ZoneId.MULTI_FUNCTION) && !L2MultiFunctionZone.logout_zone)
		{
			player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInsideZone(ZoneId.MULTI_FUNCTION1) && !L2MultiFunctionZone1.logout_zone1)
        {
            player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION2) && !L2MultiFunctionZone2.logout_zone2)
        {
            player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION3) && !L2MultiFunctionZone3.logout_zone3)
        {
            player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION4) && !L2MultiFunctionZone4.logout_zone4)
        {
            player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.isInsideZone(ZoneId.MULTI_FUNCTION5) && !L2MultiFunctionZone5.logout_zone5)
        {
            player.sendMessage("Voce nao pode sair enquanto estiver dentro de uma Multifunction zone.");
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }
		// MultiFunction Zone fim
		
		// Prevent player from logging out if they are a festival participant
		// and it is in progress, otherwise notify party members that the player
		// is not longer a participant.
		if (player.isFestivalParticipant())
		{
			if (SevenSignsFestival.getInstance().isFestivalInitialized())
			{
				player.sendMessage("You cannot log out while you are a participant in a Festival.");
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			final L2Party playerParty = player.getParty();
			
			if (playerParty != null)
			{
				player.getParty().broadcastPacket(SystemMessage.sendString(player.getName() + " has been removed from the upcoming Festival."));
			}
		}
		
		// Remove player from Boss Zone
		player.removeFromBossZone();
		
		LogRecord record = new LogRecord(Level.INFO, "Disconnected");
		record.setParameters(new Object[]
		{
			getClient()
		});
		_logAccounting.log(record);
		
		player.logout();
	}
	
	@Override
	public String getType()
	{
		return _C__00_LOGOUT;
	}
}