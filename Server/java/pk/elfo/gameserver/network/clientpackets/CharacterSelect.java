package pk.elfo.gameserver.network.clientpackets;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.gameserver.instancemanager.AntiFeedManager;
import pk.elfo.gameserver.model.CharSelectInfoPackage;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.L2GameClient.GameClientState;
import pk.elfo.gameserver.network.serverpackets.CharSelected;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SSQInfo;
import pk.elfo.gameserver.network.serverpackets.ServerClose;
import pk.elfo.gameserver.scripting.scriptengine.events.PlayerEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.player.PlayerListener;
import javolution.util.FastList;

/**
 * This class ...
 * @version $Revision: 1.5.2.1.2.5 $ $Date: 2005/03/27 15:29:30 $
 */
public class CharacterSelect extends L2GameClientPacket
{
	private static final String _C__12_CHARACTERSELECT = "[C] 12 CharacterSelect";
	protected static final Logger _logAccounting = Logger.getLogger("accounting");
	private static final List<PlayerListener> _listeners = new FastList<PlayerListener>().shared();
	
	// cd
	private int _charSlot;
	
	@SuppressWarnings("unused")
	private int _unk1; // new in C4
	@SuppressWarnings("unused")
	private int _unk2; // new in C4
	@SuppressWarnings("unused")
	private int _unk3; // new in C4
	@SuppressWarnings("unused")
	private int _unk4; // new in C4
	
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
		_unk1 = readH();
		_unk2 = readD();
		_unk3 = readD();
		_unk4 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2GameClient client = getClient();
		if (!client.getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterSelect"))
		{
			return;
		}
		
		if (Config.SECOND_AUTH_ENABLED && !client.getSecondaryAuth().isAuthed())
		{
			client.getSecondaryAuth().openDialog();
			return;
		}
		
		// We should always be able to acquire the lock
		// But if we can't lock then nothing should be done (i.e. repeated packet)
		if (client.getActiveCharLock().tryLock())
		{
			try
			{
				// should always be null
				// but if not then this is repeated packet and nothing should be done here
				if (client.getActiveChar() == null)
				{
					final CharSelectInfoPackage info = client.getCharSelection(_charSlot);
					if (info == null)
					{
						return;
					}
					
					// Selected character is banned.
					if (info.getAccessLevel() < 0)
					{
						client.close(ServerClose.STATIC_PACKET);
						return;
					}
					if ((Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP > 0) && !AntiFeedManager.getInstance().tryAddClient(AntiFeedManager.GAME_ID, client, Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP))
					{
						final NpcHtmlMessage msg = new NpcHtmlMessage(0);
						msg.setFile(info.getHtmlPrefix(), "data/html/mods/IPRestriction.htm");
						msg.replace("%max%", String.valueOf(AntiFeedManager.getInstance().getLimit(client, Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP)));
						client.sendPacket(msg);
						return;
					}
					
					// The L2PcInstance must be created here, so that it can be attached to the L2GameClient
					if (Config.DEBUG)
					{
						_log.fine("selected slot:" + _charSlot);
					}
					
					PlayerEvent event = new PlayerEvent();
					event.setClient(client);
					event.setObjectId(client.getCharSelection(_charSlot).getObjectId());
					event.setName(client.getCharSelection(_charSlot).getName());
					firePlayerListener(event);
					
					// load up character from disk
					final L2PcInstance cha = client.loadCharFromDisk(_charSlot);
					if (cha == null)
					{
						return; // handled in L2GameClient
					}
					
					CharNameTable.getInstance().addName(cha);
					
					cha.setClient(client);
					client.setActiveChar(cha);
					cha.setOnlineStatus(true, true);
					
					sendPacket(new SSQInfo());
					
					client.setState(GameClientState.IN_GAME);
					CharSelected cs = new CharSelected(cha, client.getSessionId().playOkID1);
					sendPacket(cs);
				}
			}
			finally
			{
				client.getActiveCharLock().unlock();
			}
			
			LogRecord record = new LogRecord(Level.INFO, "Logged in");
			record.setParameters(new Object[]
			{
				client
			});
			_logAccounting.log(record);
		}
	}
	
	private void firePlayerListener(PlayerEvent event)
	{
		for (PlayerListener listener : _listeners)
		{
			listener.onCharSelect(event);
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
		return _C__12_CHARACTERSELECT;
	}
}