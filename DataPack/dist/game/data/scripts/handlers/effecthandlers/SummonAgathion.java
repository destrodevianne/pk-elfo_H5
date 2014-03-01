package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */
 
public class SummonAgathion extends L2Effect
{
	public SummonAgathion(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffector() == null) || (getEffected() == null) || !getEffector().isPlayer() || !getEffected().isPlayer() || getEffected().isAlikeDead())
		{
			return false;
		}
		
		final L2PcInstance player = getEffector().getActingPlayer();
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		
		setAgathionId(player);
		player.broadcastUserInfo();
		return true;
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
		final L2PcInstance player = getEffector().getActingPlayer();
		if (player != null)
		{
			player.setAgathionId(0);
			player.broadcastUserInfo();
		}
	}
	
	/**
	 * Set the player's agathion Id.
	 * @param player the player to set the agathion Id.
	 */
	protected void setAgathionId(L2PcInstance player)
	{
		player.setAgathionId((getSkill() == null) ? 0 : getSkill().getNpcId());
	}
	
	@Override
	public boolean onActionTime()
	{
		return true;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SUMMON_AGATHION;
	}
}