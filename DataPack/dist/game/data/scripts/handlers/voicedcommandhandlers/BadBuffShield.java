/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * 
 * @author B1ggBoss
 *
 */
public class BadBuffShield implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"shieldon",
		"shieldoff"
	};
	
	/**
	 * 
	 * @see king.server.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, king.server.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("shieldon"))
		{
			activeChar.sendMessage("You are now under the grief-buff protection");
			activeChar.setProtectedPlayer(true);
		}
		else if (command.equalsIgnoreCase("shieldoff"))
		{
			activeChar.sendMessage("The grief-buff protection is now desactivated");
			activeChar.setProtectedPlayer(false);
		}
		return true;
	}
	
	/**
	 * 
	 * @see king.server.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
