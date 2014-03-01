package handlers.effecthandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.instance.L2DefenderInstance;
import pk.elfo.gameserver.model.actor.instance.L2FortCommanderInstance;
import pk.elfo.gameserver.model.actor.instance.L2GrandBossInstance;
import pk.elfo.gameserver.model.actor.instance.L2NpcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PetInstance;
import pk.elfo.gameserver.model.actor.instance.L2RaidBossInstance;
import pk.elfo.gameserver.model.actor.instance.L2SiegeFlagInstance;
import pk.elfo.gameserver.model.actor.instance.L2SiegeSummonInstance;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */

public class Fear extends L2Effect
{
	public static final int FEAR_RANGE = 500;
	
	private int _dX = -1;
	private int _dY = -1;
	
	public Fear(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.FEAR;
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffected() instanceof L2NpcInstance) || (getEffected() instanceof L2DefenderInstance) || (getEffected() instanceof L2FortCommanderInstance) || (getEffected() instanceof L2GrandBossInstance) || (getEffected() instanceof L2RaidBossInstance) || (getEffected() instanceof L2SiegeFlagInstance) || (getEffected() instanceof L2SiegeSummonInstance))
		{
			return false;
		}
		
		if (!getEffected().isAfraid())
		{
			if (getEffected().isCastingNow() && getEffected().canAbortCast())
			{
				getEffected().abortCast();
			}
			
			if (getEffected().getX() > getEffector().getX())
			{
				_dX = 1;
			}
			if (getEffected().getY() > getEffector().getY())
			{
				_dY = 1;
			}
			
			getEffected().startFear();
			onActionTime();
			return true;
		}
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopFear(false);
	}
	
	@Override
	public boolean onActionTime()
	{
		int posX = getEffected().getX();
		int posY = getEffected().getY();
		int posZ = getEffected().getZ();
		
		if (getEffected().getX() > getEffector().getX())
		{
			_dX = 1;
		}
		if (getEffected().getY() > getEffector().getY())
		{
			_dY = 1;
		}
		
		posX += _dX * FEAR_RANGE;
		posY += _dY * FEAR_RANGE;
		
		if (Config.GEODATA > 0)
		{
			Location destiny = GeoData.getInstance().moveCheck(getEffected().getX(), getEffected().getY(), getEffected().getZ(), posX, posY, posZ, getEffected().getInstanceId());
			posX = destiny.getX();
			posY = destiny.getY();
		}
		
		if (!(getEffected() instanceof L2PetInstance))
		{
			getEffected().setRunning();
		}
		
		getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(posX, posY, posZ, 0));
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.FEAR.getMask();
	}
}