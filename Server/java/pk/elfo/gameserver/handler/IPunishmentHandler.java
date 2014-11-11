package pk.elfo.gameserver.handler;

import java.util.logging.Logger;

import pk.elfo.gameserver.model.punishment.PunishmentTask;
import pk.elfo.gameserver.model.punishment.PunishmentType;

public interface IPunishmentHandler
{
	static final Logger _log = Logger.getLogger(IPunishmentHandler.class.getName());
	
	public void onStart(PunishmentTask task);
	
	public void onEnd(PunishmentTask task);
	
	public PunishmentType getType();
}