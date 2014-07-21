package pk.elfo.gameserver.model.zone.type;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.zone.L2ZoneType;
import pk.elfo.gameserver.model.zone.ZoneId;

/**
 * PkElfo
 */

public class L2TownZone extends L2ZoneType
{
	private int _townId;
	private int _taxById;
	private boolean _isTWZone = false;
	
	public L2TownZone(int id)
	{
		super(id);
		
		_taxById = 0;
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("townId"))
		{
			_townId = Integer.parseInt(value);
		}
		else if (name.equals("taxById"))
		{
			_taxById = Integer.parseInt(value);
		}
		else
		{
			super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		if (_isTWZone)
		{
			character.setInTownWarEvent(true);
			character.sendMessage("Voce entrou em uma zona do evento Town War.");
		}
		character.setInsideZone(ZoneId.TOWN, true);
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		if (_isTWZone)
		{
			character.setInTownWarEvent(false);
			character.sendMessage("Voce deixou uma zona do evento Town War.");
		}
		
		character.setInsideZone(ZoneId.TOWN, false);
	}
	
	public void onUpdate(L2Character character)
	{
		if (_isTWZone)
		{
			character.setInTownWarEvent(true);
			character.sendMessage("Voce entrou em uma zona do evento Town War.");
		}
		else
		{
			character.setInTownWarEvent(false);
			character.sendMessage("Voce deixou uma zona do evento Town War.");
		}
	}
	
	@Override
	public void onDieInside(L2Character character)
	{
	}
	
	@Override
	public void onReviveInside(L2Character character)
	{
	}
	
	public void updateForCharactersInside()
	{
		for (L2Character character : getCharactersInside())
		{
			if (character != null)
			{
				onUpdate(character);
			}
		}
	}
	
	/**
	 * Returns this zones town id (if any)
	 * @return
	 */
	public int getTownId()
	{
		return _townId;
	}
	
	/**
	 * Returns this town zones castle id
	 * @return
	 */
	public final int getTaxById()
	{
		return _taxById;
	}
	
	public final void setIsTWZone(boolean value)
	{
		_isTWZone = value;
	}
}
