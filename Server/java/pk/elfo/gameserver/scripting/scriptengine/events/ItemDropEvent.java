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
package pk.elfo.gameserver.scripting.scriptengine.events;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.scripting.scriptengine.events.impl.L2Event;

/**
 * @author TheOne
 */
public class ItemDropEvent implements L2Event
{
	private L2ItemInstance item;
	private L2Character dropper;
	private Location loc;
	
	/**
	 * @return the item
	 */
	public L2ItemInstance getItem()
	{
		return item;
	}
	
	/**
	 * @param i the item to set
	 */
	public void setItem(L2ItemInstance i)
	{
		item = i;
	}
	
	/**
	 * @return the dropper
	 */
	public L2Character getDropper()
	{
		return dropper;
	}
	
	/**
	 * @param d the dropper to set
	 */
	public void setDropper(L2Character d)
	{
		dropper = d;
	}
	
	/**
	 * @return the location where the item was dropped.
	 */
	public Location getLocation()
	{
		return loc;
	}
	
	/**
	 * @param l the location to to set
	 */
	public void setLocation(Location l)
	{
		loc = l;
	}
}
