package pk.elfo.gameserver.events.model;

import pk.elfo.gameserver.events.AbstractEvent;
import javolution.text.TextBuilder;

public abstract class EventStatus
{
	protected AbstractEvent event;
	protected TextBuilder sb = new TextBuilder();
	
	public abstract String generateList();
}