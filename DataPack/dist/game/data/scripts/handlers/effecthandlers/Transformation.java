package handlers.effecthandlers;

import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class Transformation extends L2Effect
{
	public Transformation(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	// Special constructor to steal this effect
	public Transformation(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.TRANSFORMATION;
	}
	
	@Override
	public boolean onStart()
	{
		if (!getEffected().isPlayer())
		{
			return false;
		}
		
		L2PcInstance trg = getEffected().getActingPlayer();
		if (trg == null)
		{
			return false;
		}
		
		if (trg.isAlikeDead() || trg.isCursedWeaponEquipped())
		{
			return false;
		}
		
		if (trg.isSitting())
		{
			trg.sendPacket(SystemMessageId.CANNOT_TRANSFORM_WHILE_SITTING);
			return false;
		}
		
		if (trg.isTransformed() || trg.isInStance())
		{
			trg.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
			return false;
		}
		
		if (trg.isInWater())
		{
			trg.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER);
			return false;
		}
		
		if (trg.isFlyingMounted() || trg.isMounted() || trg.isRidingStrider())
		{
			trg.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET);
			return false;
		}
		
		TransformationManager.getInstance().transformPlayer(getSkill().getTransformId(), trg);
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopTransformation(false);
	}
}