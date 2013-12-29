package pk.elfo.gameserver.events;

import pk.elfo.gameserver.events.functions.Buffer;
import pk.elfo.gameserver.events.functions.Scheduler;
import pk.elfo.gameserver.events.functions.Vote;
import pk.elfo.gameserver.events.io.Out;

public class Events
{
	public static void eventStart()
	{
		Config.getInstance();
		
		if (Config.getInstance().getBoolean(0, "voteEnabled"))
		{
			Vote.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "schedulerEnabled"))
		{
			Scheduler.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "eventBufferEnabled"))
		{
			Buffer.getInstance();
		}
		
		Out.registerHandlers();
	}
}