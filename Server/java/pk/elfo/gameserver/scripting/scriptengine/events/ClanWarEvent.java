package pk.elfo.gameserver.scripting.scriptengine.events;

import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.scripting.scriptengine.events.impl.L2Event;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2Script.EventStage;

/**
 * @author TheOne
 */
public class ClanWarEvent implements L2Event
{
	private L2Clan clan1;
	private L2Clan clan2;
	private EventStage stage;
	
	/**
	 * @return the clan1
	 */
	public L2Clan getClan1()
	{
		return clan1;
	}
	
	/**
	 * @param clan1 the clan1 to set
	 */
	public void setClan1(L2Clan clan1)
	{
		this.clan1 = clan1;
	}
	
	/**
	 * @return the clan2
	 */
	public L2Clan getClan2()
	{
		return clan2;
	}
	
	/**
	 * @param clan2 the clan2 to set
	 */
	public void setClan2(L2Clan clan2)
	{
		this.clan2 = clan2;
	}
	
	/**
	 * @return the stage
	 */
	public EventStage getStage()
	{
		return stage;
	}
	
	/**
	 * @param stage the stage to set
	 */
	public void setStage(EventStage stage)
	{
		this.stage = stage;
	}
}
