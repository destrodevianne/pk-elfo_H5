package pk.elfo.gameserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;

public class FortUpdater implements Runnable
{
	protected static Logger _log = Logger.getLogger(FortUpdater.class.getName());
	private final L2Clan _clan;
	private final Fort _fort;
	private int _runCount;
	private final UpdaterType _updaterType;
	
	public enum UpdaterType
	{
		MAX_OWN_TIME, // gives fort back to NPC clan
		PERIODIC_UPDATE // raise blood oath/supply level
	}
	
	public FortUpdater(Fort fort, L2Clan clan, int runCount, UpdaterType ut)
	{
		_fort = fort;
		_clan = clan;
		_runCount = runCount;
		_updaterType = ut;
	}
	
	@Override
	public void run()
	{
		try
		{
			switch (_updaterType)
			{
				case PERIODIC_UPDATE:
					_runCount++;
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan))
					{
						return;
					}
					_fort.getOwnerClan().increaseBloodOathCount();
					
					if (_fort.getFortState() == 2)
					{
						if (_clan.getWarehouse().getAdena() >= Config.FS_FEE_FOR_CASTLE)
						{
							_clan.getWarehouse().destroyItemByItemId("FS_fee_for_Castle", PcInventory.ADENA_ID, Config.FS_FEE_FOR_CASTLE, null, null);
							CastleManager.getInstance().getCastleById(_fort.getCastleId()).addToTreasuryNoTax(Config.FS_FEE_FOR_CASTLE);
							_fort.raiseSupplyLvL();
						}
						else
						{
							_fort.setFortState(1, 0);
						}
					}
					_fort.saveFortVariables();
					break;
				case MAX_OWN_TIME:
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan))
					{
						return;
					}
					if (_fort.getOwnedTime() > (Config.FS_MAX_OWN_TIME * 3600))
					{
						_fort.removeOwner(true);
						_fort.setFortState(0, 0);
					}
					break;
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "", e);
		}
	}
	
	public int getRunCount()
	{
		return _runCount;
	}
}