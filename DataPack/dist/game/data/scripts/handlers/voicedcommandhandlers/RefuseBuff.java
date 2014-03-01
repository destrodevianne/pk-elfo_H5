package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;

/**
 * PkElfo
 */

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