package pk.elfo.gameserver.network.clientpackets;

import java.util.List;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.network.serverpackets.CharDeleteFail;
import pk.elfo.gameserver.network.serverpackets.CharDeleteSuccess;
import pk.elfo.gameserver.network.serverpackets.CharSelectionInfo;
import pk.elfo.gameserver.scripting.scriptengine.events.PlayerEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.player.PlayerListener;
import javolution.util.FastList;

/**
 * This class ...
 * @version $Revision: 1.8.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class CharacterDelete extends L2GameClientPacket
{
	private static final String _C__0C_CHARACTERDELETE = "[C] 0D CharacterDelete";
	private static final List<PlayerListener> _listeners = new FastList<PlayerListener>().shared();
	
	// cd
	private int _charSlot;
	
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (!getClient().getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterDelete"))
		{
			sendPacket(new CharDeleteFail(CharDeleteFail.REASON_DELETION_FAILED));
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.fine("deleting slot:" + _charSlot);
		}
		
		try
		{
			byte answer = getClient().markToDeleteChar(_charSlot);
			
			switch (answer)
			{
				default:
				case -1: // Error
					break;
				case 0: // Success!
					sendPacket(new CharDeleteSuccess());
					PlayerEvent event = new PlayerEvent();
					event.setClient(getClient());
					event.setObjectId(getClient().getCharSelection(_charSlot).getObjectId());
					event.setName(getClient().getCharSelection(_charSlot).getName());
					firePlayerListener(event);
					break;
				case 1:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER));
					break;
				case 2:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED));
					break;
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error:", e);
		}
		CharSelectionInfo cl = new CharSelectionInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1, 0);
		sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
	
	private void firePlayerListener(PlayerEvent event)
	{
		for (PlayerListener listener : _listeners)
		{
			listener.onCharDelete(event);
		}
	}
	
	public static void addPlayerListener(PlayerListener listener)
	{
		if (!_listeners.contains(listener))
		{
			_listeners.add(listener);
		}
	}
	
	public static void removePlayerListener(PlayerListener listener)
	{
		_listeners.remove(listener);
	}
	
	@Override
	public String getType()
	{
		return _C__0C_CHARACTERDELETE;
	}
}