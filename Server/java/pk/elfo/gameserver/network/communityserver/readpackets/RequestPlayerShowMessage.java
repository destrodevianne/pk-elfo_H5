package pk.elfo.gameserver.network.communityserver.readpackets;

import java.util.logging.Logger;

import org.netcon.BaseReadPacket;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExMailArrived;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

public final class RequestPlayerShowMessage extends BaseReadPacket
{
	private static final Logger _log = Logger.getLogger(RequestPlayerShowMessage.class.getName());
	
	public RequestPlayerShowMessage(final byte[] data)
	{
		super(data);
	}
	
	@Override
	public final void run()
	{
		final int playerObjId = super.readD();
		final int type = super.readD();
		
		L2PcInstance player = L2World.getInstance().getPlayer(playerObjId);
		if (player == null)
		{
			return;
		}
		
		switch (type)
		{
			case -1: // mail arrived
				player.sendPacket(ExMailArrived.STATIC_PACKET);
				break;
			case 0: // text message
				player.sendMessage(super.readS());
				break;
			case 236:
				player.sendPacket(SystemMessageId.ONLY_THE_CLAN_LEADER_IS_ENABLED);
				break;
			case 1050:
				player.sendPacket(SystemMessageId.NO_CB_IN_MY_CLAN);
				break;
			case 1070:
				player.sendPacket(SystemMessageId.NO_READ_PERMISSION);
				break;
			case 1071:
				player.sendPacket(SystemMessageId.NO_WRITE_PERMISSION);
				break;
			case 1205:
				player.sendPacket(SystemMessageId.MAILBOX_FULL);
				break;
			case 1206:
				player.sendPacket(SystemMessageId.MEMOBOX_FULL);
				break;
			case 1227:
				try
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_UNREAD_MESSAGES);
					final int number = super.readD();
					sm.addNumber(number);
					player.sendPacket(sm);
				}
				catch (Exception e)
				{
					_log.info("Incorrect packet from CBserver!");
				}
				break;
			case 1228:
				try
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_BLOCKED_YOU_CANNOT_MAIL);
					final String name = super.readS();
					sm.addString(name);
					player.sendPacket(sm);
				}
				catch (Exception e)
				{
					_log.info("Incorrect packet from CBserver!");
				}
				break;
			case 1229:
				player.sendPacket(SystemMessageId.NO_MORE_MESSAGES_TODAY);
				break;
			case 1230:
				player.sendPacket(SystemMessageId.ONLY_FIVE_RECIPIENTS);
				break;
			case 1231:
				player.sendPacket(SystemMessageId.SENT_MAIL);
				break;
			case 1232:
				player.sendPacket(SystemMessageId.MESSAGE_NOT_SENT);
				break;
			case 1233:
				player.sendPacket(SystemMessageId.NEW_MAIL);
				break;
			case 1234:
				player.sendPacket(SystemMessageId.MAIL_STORED_IN_MAILBOX);
				break;
			case 1238:
				player.sendPacket(SystemMessageId.TEMP_MAILBOX_FULL);
				break;
			case 1370:
				try
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CANNOT_MAIL_GM_C1);
					final String name = super.readS();
					sm.addString(name);
					player.sendPacket(sm);
				}
				catch (Exception e)
				{
					_log.info("Incorrect packet from CBserver!");
				}
				break;
			default:
				_log.info("error: Unknown message request from CB server: " + type);
		}
	}
}