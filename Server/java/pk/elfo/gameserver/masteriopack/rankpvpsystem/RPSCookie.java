package pk.elfo.gameserver.masteriopack.rankpvpsystem;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Class used in L2PcInstance. Contains some system variables used in game.
 * From Killer side.
 * PkElfo
 */
public class RPSCookie 
{
	private RPSHtmlDeathStatus _deathStatus = null;
	private RPSHtmlComboKill _comboKill = null;
	
	private L2PcInstance _target = null;
	
	public void runPvpTask(L2PcInstance player, L2Character target)
	{
		if(RPSConfig.RANK_PVP_SYSTEM_ENABLED)
		{
			if(player != null && target != null && target instanceof L2PcInstance)
			{
				// set Victim handler for Killer
				//setTarget((L2PcInstance)target);	// [not required]
				
				// set Killer handler for Victim
				((L2PcInstance)target).getRPSCookie().setTarget(player);
				
				ThreadPoolManager.getInstance().executeTask(new RankPvpSystemPvpTask(player, (L2PcInstance)target));
			}
		}
	}

	public class RankPvpSystemPvpTask implements Runnable
	{
		private L2PcInstance _killer = null;
		private L2PcInstance _victim = null;
		
		public RankPvpSystemPvpTask(L2PcInstance killer, L2PcInstance victim)
		{
			_killer = killer;
			_victim = victim;
		}
		
		@Override
		public void run() 
		{
			RankPvpSystem rps = new RankPvpSystem(_killer, _victim);
				
			rps.doPvp();
		}
	}

	public RPSHtmlDeathStatus getDeathStatus()
	{
		return _deathStatus;
	}
	
	public boolean isDeathStatusActive()
	{
		if(_deathStatus == null)
			return false;
		
		return true;
	}

	public void setDeathStatus(RPSHtmlDeathStatus deathStatus)
	{
		_deathStatus = deathStatus;
	}

	public RPSHtmlComboKill getComboKill()
	{
		return _comboKill;
	}
	
	public boolean isComboKillActive()
	{
		if(_comboKill == null)
			return false;
		
		return true;
	}

	public void setComboKill(RPSHtmlComboKill comboKill) 
	{
		_comboKill = comboKill;
	}

	/**
	 * The player's Target.
	 * @return
	 */
	public L2PcInstance getTarget() 
	{
		return _target;
	}

	/** 
	 * The player's Target.
	 * @param target
	 */
	public void setTarget(L2PcInstance target) 
	{
		_target = target;
	}
}
