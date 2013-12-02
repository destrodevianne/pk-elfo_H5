/* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2, or (at your option)
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
* 02111-1307, USA.
*
* http://www.gnu.org/copyleft/gpl.html
*/
package king.server.gameserver.model.actor.instance;

import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.entity.Castle;
import king.server.gameserver.network.serverpackets.ActionFailed;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;
import king.server.gameserver.network.serverpackets.SiegeInfo;

public class L2CastleManageInstance extends L2NpcInstance
{	
	public L2CastleManageInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if(player == null )  return;
		if (!canTarget(player))
			return;
		
		else if (command.startsWith("siege_"))
		{
			int castleId = 0;
			
			if (command.startsWith("siege_gludio"))
				castleId = 1;
			else if (command.startsWith("siege_dion"))
				castleId = 2;
			else if (command.startsWith("siege_giran"))
				castleId = 3;
			else if (command.startsWith("siege_oren"))
				castleId = 4;
			else if (command.startsWith("siege_aden"))
				castleId = 5;
			else if (command.startsWith("siege_innadril"))
				castleId = 6;
			else if (command.startsWith("siege_goddard"))
				castleId = 7;
			else if (command.startsWith("siege_rune"))
				castleId = 8;
			else if (command.startsWith("siege_schuttgart"))
				castleId = 9;
			
			Castle castle = CastleManager.getInstance().getCastleById(castleId);
			if(castle != null && castleId != 0)
				player.sendPacket(new SiegeInfo(castle));
		}
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());	
		html.setFile(player.getHtmlPrefix(), "data/html/mods/CastleManager.htm");
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
		
	}
}