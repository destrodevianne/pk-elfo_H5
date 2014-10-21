package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.handler.IUserCommandHandler;
import pk.elfo.gameserver.handler.UserCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class BypassUserCmd extends L2GameClientPacket
{
	private static final String _C__B3_BYPASSUSERCMD = "[C] B3 BypassUserCmd";
	
	private int _command;
	
	@Override
	protected void readImpl()
	{
		_command = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		IUserCommandHandler handler = UserCommandHandler.getInstance().getHandler(_command);
		
		if (handler == null)
		{
			if (player.isGM())
			{
				player.sendMessage("User commandID " + _command + " not implemented yet.");
			}
		}
		else
		{
			handler.useUserCommand(_command, getClient().getActiveChar());
		}
	}
	
	@Override
	public String getType()
	{
		return _C__B3_BYPASSUSERCMD;
	}
}