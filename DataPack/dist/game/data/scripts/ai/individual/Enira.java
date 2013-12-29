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
package ai.individual;

import java.util.Calendar;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class Enira extends AbstractNpcAI
{
	private static final int ENIRA = 25625;
	
	public Enira(String name, String descr)
	{
		super(name, descr);
		
		eniraSpawn();
	}
	
	private void eniraSpawn()
	{
		Calendar _date = Calendar.getInstance();
		int newSecond = _date.get(13);
		int newMinute = _date.get(12);
		int newHour = _date.get(10);
		
		int targetHour = (24 - newHour) * 60 * 60 * 1000;
		int extraMinutesAndSeconds = (((60 - newMinute) * 60) + (60 - newSecond)) * 1000;
		int timerDuration = targetHour + extraMinutesAndSeconds;
		
		startQuestTimer("enira_spawn", timerDuration, null, null);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("enira_spawn"))
		{
			if (Rnd.get(100) <= 40)
			{
				L2Npc eniraSpawn = SpawnTable.getInstance().getFirstSpawn(ENIRA).getLastSpawn();
				if (eniraSpawn == null)
				{
					addSpawn(ENIRA, -181989, 208968, 4030, 0, false, 3600000L);
				}
			}
			eniraSpawn();
		}
		
		return null;
	}
	
	public static void main(String[] args)
	{
		new Enira(Enira.class.getSimpleName(), "ai");
	}
}
