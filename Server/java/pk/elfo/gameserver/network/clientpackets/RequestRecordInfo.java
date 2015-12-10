/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package pk.elfo.gameserver.network.clientpackets;

import java.util.Collection;

import pk.elfo.gameserver.TaskPriority;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExBrExtraUserInfo;
import pk.elfo.gameserver.network.serverpackets.SpawnItem;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

public class RequestRecordInfo extends L2GameClientPacket
{
	private static final String _C__6E_REQUEST_RECORD_INFO = "[C] 6E RequestRecordInfo";
	
	/**
	 * urgent messages, execute immediately
	 * @return
	 */
	public TaskPriority getPriority()
	{
		return TaskPriority.PR_NORMAL;
	}
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance _activeChar = getClient().getActiveChar();
		
		if (_activeChar == null)
		{
			return;
		}
		
		_activeChar.sendPacket(new UserInfo(_activeChar));
		_activeChar.sendPacket(new ExBrExtraUserInfo(_activeChar));
		
		Collection<L2Object> objs = _activeChar.getKnownList().getKnownObjects().values();
		for (L2Object object : objs)
		{
			if (object.getPoly().isMorphed() && object.getPoly().getPolyType().equals("item"))
			{
				_activeChar.sendPacket(new SpawnItem(object));
			}
			else
			{
				object.sendInfo(_activeChar);
				
				if (object instanceof L2Character)
				{
					// Update the state of the L2Character object client
					// side by sending Server->Client packet
					// MoveToPawn/CharMoveToLocation and AutoAttackStart to
					// the L2PcInstance
					L2Character obj = (L2Character) object;
					if (obj.getAI() != null)
					{
						obj.getAI().describeStateToPlayer(_activeChar);
					}
				}
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__6E_REQUEST_RECORD_INFO;
	}
}