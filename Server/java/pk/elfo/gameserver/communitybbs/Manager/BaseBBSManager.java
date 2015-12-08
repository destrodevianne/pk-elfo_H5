package pk.elfo.gameserver.communitybbs.Manager;

import java.util.List;

import javolution.util.FastList;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

public abstract class BaseBBSManager
{
	public abstract void parsecmd(String command, L2PcInstance activeChar);
	
	public abstract void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar);
	
	protected void separateAndSend(String html, L2PcInstance acha)
	{
		if (html == null)
		{
			return;
		}
		if (html.length() < 4096)
		{
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(new ShowBoard(null, "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
			
		}
		else if (html.length() < 8192)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 4096), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4096), "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
			
		}
		else if (html.length() < 16384)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 4096), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4096, 8192), "102"));
			acha.sendPacket(new ShowBoard(html.substring(8192), "103"));
		}
	}
	
	/**
	 * @param html
	 * @param acha
	 */
	protected void send1001(String html, L2PcInstance acha)
	{
		if (html.length() < 8192)
		{
			acha.sendPacket(new ShowBoard(html, "1001"));
		}
	}
	
	/**
	 * @param acha
	 */
	protected void send1002(L2PcInstance acha)
	{
		send1002(acha, " ", " ", "0");
	}
	
	/**
	 * @param activeChar
	 * @param string
	 * @param string2
	 * @param string3
	 */
	protected void send1002(L2PcInstance activeChar, String string, String string2, String string3)
	{
		List<String> _arg = new FastList<>();
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add(activeChar.getName());
		_arg.add(Integer.toString(activeChar.getObjectId()));
		_arg.add(activeChar.getAccountName());
		_arg.add("9");
		_arg.add(string2); // subject?
		_arg.add(string2); // subject?
		_arg.add(string); // text
		_arg.add(string3); // date?
		_arg.add(string3); // date?
		_arg.add("0");
		_arg.add("0");
		activeChar.sendPacket(new ShowBoard(_arg));
	}
}
