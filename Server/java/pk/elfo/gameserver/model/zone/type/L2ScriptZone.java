package pk.elfo.gameserver.model.zone.type;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.zone.L2ZoneType;
import pk.elfo.gameserver.model.zone.ZoneId;

public class L2ScriptZone extends L2ZoneType
{
	public L2ScriptZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.SCRIPT, true);
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.SCRIPT, false);
	}
	
	@Override
	public void onDieInside(L2Character character)
	{
	}
	
	@Override
	public void onReviveInside(L2Character character)
	{
	}
}
