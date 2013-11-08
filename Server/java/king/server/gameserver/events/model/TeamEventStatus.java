package king.server.gameserver.events.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import king.server.gameserver.events.Config;
import king.server.gameserver.events.container.EventContainer;

public class TeamEventStatus extends EventStatus
{
	private final List<EventPlayer> team1;
	private final List<EventPlayer> team2;
	
	public TeamEventStatus(Integer eventContainerId)
	{
		event = EventContainer.getInstance().getEvent(eventContainerId);
		team1 = new LinkedList<>();
		team2 = new LinkedList<>();
	}
	
	@Override
	public String generateList()
	{
		sb.clear();
		
		for (EventPlayer player : event.getPlayersOfTeam(1))
		{
			team1.add(player);
		}
		
		Collections.sort(team1);
		Collections.reverse(team1);
		
		for (EventPlayer player : event.getPlayersOfTeam(2))
		{
			team2.add(player);
		}
		
		Collections.sort(team2);
		Collections.reverse(team2);
		
		sb.append("<center><table width=270 bgcolor=5A5A5A><tr><td width=70>Corrente</td><td width=130><center>" + Config.getInstance().getString(event.getId(), "eventName") + "</td><td width=70>Time: " + event.getClock().getTimeInString() + "</td></tr></table>");
		sb.append("<center><table width=270><tr><td><center><font color=" + event.getTeam(1).getHexaColor() + ">" + event.getTeam(1).getScore() + "</font> - " + "<font color=" + event.getTeam(2).getHexaColor() + ">" + event.getTeam(2).getScore() + "</font></td></tr></table>");
		sb.append("<br><table width=270>");
		
		sb.append("<tr><td><font color=" + event.getTeam(1).getHexaColor() + ">" + event.getTeam(1).getName() + "</font> equipe</td><td></td><td></td><td></td></tr>");
		for (EventPlayer p : team1.subList(0, (team1.size() < 10 ? team1.size() : 10)))
		{
			sb.append("<tr><td>" + p.getName() + "</td><td>lvl " + p.getLevel() + "</td><td>" + p.getClassName() + "</td><td>" + p.getScore() + "</td></tr>");
		}
		
		sb.append("<tr><td><font color=" + event.getTeam(2).getHexaColor() + ">" + event.getTeam(2).getName() + "</font> equipe</td><td></td><td></td><td></td></tr>");
		for (EventPlayer p : team2.subList(0, (team2.size() < 10 ? team2.size() : 10)))
		{
			sb.append("<tr><td>" + p.getName() + "</td><td>lvl " + p.getLevel() + "</td><td>" + p.getClassName() + "</td><td>" + p.getScore() + "</td></tr>");
		}
		
		sb.append("</table>");
		
		team1.clear();
		team2.clear();
		
		return sb.toString();
	}
}