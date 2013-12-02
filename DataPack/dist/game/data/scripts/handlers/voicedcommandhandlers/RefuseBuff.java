package handlers.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.clientpackets.Say2;
import king.server.gameserver.network.serverpackets.CreatureSay;


public class RefuseBuff implements IVoicedCommandHandler
{
	private String[] _voicedCommands =
	{
		"allowbuff",
		"refusebuff"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("allowbuff") && activeChar.isVip())
		{
			CreatureSay nm = new CreatureSay(0, Say2.TELL,"Server","Refuse Buff Mode ON.");
			activeChar.sendPacket(nm);
			activeChar.setisRefusingBuff(false);
		}
		else if (command.equals("refusebuff") && activeChar.isVip())
		{
			CreatureSay nm = new CreatureSay(0, Say2.TELL,"Server","Refuse Buff Mode OFF.");
			activeChar.sendPacket(nm);
			activeChar.setisRefusingBuff(true);
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
