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
package pk.elfo.gameserver.events.functions;

import java.util.Map;

import pk.elfo.gameserver.events.AbstractEvent;
import pk.elfo.gameserver.events.Config;
import pk.elfo.gameserver.events.ManagerNpc;
import pk.elfo.gameserver.events.container.EventContainer;
import pk.elfo.gameserver.events.io.Out;
import pk.elfo.gameserver.events.model.Clock;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Rizel
 */
public class Vote
{
	
	private static class SingletonHolder
	{
		protected static final Vote _instance = new Vote();
	}
	
	private class VoteCore implements Runnable
	{
		
		@Override
		public void run()
		{
			switch (phase)
			{
				case VOTE:
					announce("A fase de votacao comecou! voce tem " + (Config.getInstance().getInt(0, "voteTime") / 60) + " minutos para votar!");
					voteCountdown = new VoteCountdown(Config.getInstance().getInt(0, "voteTime"));
					voteCountdown.start();
					break;
				
				case CHECK:
					if (votes.size() > 0)
					{
						setCurrentEvent(EventContainer.getInstance().createEvent(getVoteWinner()));
					}
					else
					{
						setCurrentEvent(EventContainer.getInstance().createRandomEvent());
					}
					
					setVotePhase(VotePhase.RUNNING);
					break;
			}
		}
	}
	
	public class VoteCountdown extends Clock
	{
		public VoteCountdown(int time)
		{
			super(time);
		}
		
		@Override
		public void clockBody()
		{
			if ((counter == Config.getInstance().getInt(0, "showVotePopupAt")) && Config.getInstance().getBoolean(0, "votePopupEnabled"))
			{
				for (Integer playerId : Out.getEveryPlayer())
				{
					if (!popupOffList.contains(playerId))
					{
						ManagerNpc.getInstance().showVoteList(playerId);
					}
				}
			}
			
			switch (counter)
			{
				case 1800:
				case 1200:
				case 600:
				case 300:
				case 60:
					announce("" + (counter / 60) + " minutos restantes para votar.");
					break;
				case 30:
				case 10:
				case 5:
					announce("" + counter + " segundos restantes para votar.");
					break;
			}
		}
		
		@Override
		protected void onZero()
		{
			setVotePhase(VotePhase.CHECK);
			voteSchedule(1);
			
		}
	}
	
	private enum VotePhase
	{
		VOTE,
		RUNNING,
		CHECK,
	}
	
	public static final Vote getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public FastMap<Integer, Integer> votes;
	private final FastList<Integer> bannedEvents;
	VoteCountdown voteCountdown;
	
	VotePhase phase;
	
	private final VoteCore voteCore;
	
	private AbstractEvent currentEvent;
	
	final FastList<Integer> popupOffList;
	
	@SuppressWarnings("synthetic-access")
	public Vote()
	{
		votes = new FastMap<>();
		voteCore = new VoteCore();
		bannedEvents = new FastList<>();
		popupOffList = new FastList<>();
		
		setVotePhase(VotePhase.VOTE);
		voteSchedule(1);
	}
	
	public boolean addVote(Integer player, int eventId)
	{
		if (votes.containsKey(player))
		{
			Out.sendMessage(player, "[Event Manager]: Voce ja votou em um evento!");
			return false;
		}
		Out.sendMessage(player, "[Event Manager]: Voce votou com sucesso para o evento");
		votes.put(player, eventId);
		return true;
	}
	
	void announce(String text)
	{
		Out.broadcastCreatureSay("[Event] " + text);
	}
	
	public void checkIfCurrent(AbstractEvent event)
	{
		if (getCurrentEvent() == event)
		{
			announce("Proximo evento em " + (Config.getInstance().getInt(0, "voteTime") / 60) + "minutos!");
			setVotePhase(VotePhase.VOTE);
			voteSchedule(1);
		}
	}
	
	public FastList<Integer> getBannedEvents()
	{
		return bannedEvents;
	}
	
	public AbstractEvent getCurrentEvent()
	{
		return currentEvent;
	}
	
	public int getVoteCount()
	{
		return votes.size();
	}
	
	public int getVoteCount(int event)
	{
		try
		{
			int count = 0;
			
			if (votes == null)
			{
				return 0;
			}
			
			if (votes.values() == null)
			{
				return 0;
			}
			
			for (int e : votes.values())
			{
				if (e == event)
				{
					count++;
				}
			}
			
			return count;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public FastMap<Integer, Integer> getVotes()
	{
		return votes;
	}
	
	public String getVoteTimeLeft()
	{
		return voteCountdown.getTimeInString();
	}
	
	int getVoteWinner()
	{
		int old = 0;
		FastMap<Integer, Integer> temp = new FastMap<>();
		
		for (int vote : votes.values())
		{
			if (!temp.containsKey(vote))
			{
				temp.put(vote, 1);
			}
			else
			{
				old = temp.get(vote);
				old++;
				temp.getEntry(vote).setValue(old);
			}
		}
		
		int max = temp.head().getNext().getValue();
		int result = temp.head().getNext().getKey();
		
		for (Map.Entry<Integer, Integer> entry : temp.entrySet())
		{
			if (entry.getValue() > max)
			{
				max = entry.getValue();
				result = entry.getKey();
			}
		}
		
		votes.clear();
		temp = null;
		return result;
		
	}
	
	void setCurrentEvent(AbstractEvent event)
	{
		currentEvent = event;
	}
	
	void setVotePhase(VotePhase p)
	{
		phase = p;
	}
	
	public void switchPopup(Integer player)
	{
		if (popupOffList.contains(player))
		{
			popupOffList.remove(player);
		}
		else
		{
			popupOffList.add(player);
		}
	}
	
	protected void voteSchedule(int time)
	{
		Out.tpmScheduleGeneral(voteCore, time);
	}
}
