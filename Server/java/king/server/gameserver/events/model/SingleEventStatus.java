package king.server.gameserver.events.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import king.server.gameserver.events.Config;
import king.server.gameserver.events.container.EventContainer;

public class SingleEventStatus extends EventStatus
{
	private final List<EventPlayer> team;
	
	public SingleEventStatus(Integer eventContainerId)
	{
		event = EventContainer.getInstance().getEvent(eventContainerId);
		team = new LinkedList<>();
	}
	
	@Override
	public String generateList()
	{
		sb.clear();
		
		for (EventPlayer player : event.getPlayersOfTeam(1))
		{
			team.add(player);
		}
		
		Collections.sort(team);
		Collections.reverse(team);
		
		sb.append("<center><table width=270 bgcolor=5A5A5A><tr><td width=70>Corrente</td><td width=130><center>" + Config.getInstance().getString(event.getId(), "eventName") + "</td><td width=70>Tempo: " + event.getClock().getTimeInString() + "</td></tr></table>");
		sb.append("<br><table width=270>");
		
		for (EventPlayer p : team.subList(0, (team.size() < 10 ? team.size() : 10)))
		{
			sb.append("<tr><td>" + p.getName() + "</td><td>lvl " + p.getLevel() + "</td><td>" + p.getClassName() + "</td><td>" + p.getScore() + "</td></tr>");
		}
		
		sb.append("</table>");
		
		team.clear();
		
		return sb.toString();
	}
}