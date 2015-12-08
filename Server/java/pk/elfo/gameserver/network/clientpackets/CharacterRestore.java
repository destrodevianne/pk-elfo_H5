package pk.elfo.gameserver.network.clientpackets;

import java.util.List;

import pk.elfo.gameserver.network.serverpackets.CharSelectionInfo;
import pk.elfo.gameserver.scripting.scriptengine.events.PlayerEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.player.PlayerListener;
import javolution.util.FastList;

/**
 * This class ...
 * @version $Revision: 1.4.2.1.2.2 $ $Date: 2005/03/27 15:29:29 $
 */
public final class CharacterRestore extends L2GameClientPacket
{
	private static final String _C__7B_CHARACTERRESTORE = "[C] 7B CharacterRestore";
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
		if (!getClient().getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterRestore"))
		{
			return;
		}
		
		getClient().markRestoredChar(_charSlot);
		CharSelectionInfo cl = new CharSelectionInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1, 0);
		sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
		PlayerEvent event = new PlayerEvent();
		event.setClient(getClient());
		event.setObjectId(getClient().getCharSelection(_charSlot).getObjectId());
		event.setName(getClient().getCharSelection(_charSlot).getName());
		firePlayerListener(event);
	}
	
	private void firePlayerListener(PlayerEvent event)
	{
		for (PlayerListener listener : _listeners)
		{
			listener.onCharRestore(event);
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
		return _C__7B_CHARACTERRESTORE;
	}
}