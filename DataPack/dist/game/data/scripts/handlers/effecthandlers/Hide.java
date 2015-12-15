package handlers.effecthandlers;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.AbnormalEffect;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.serverpackets.DeleteObject;
import pk.elfo.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Projeto PkElfo
 */
 
public class Hide extends L2Effect
{
	public Hide(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	public Hide(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.HIDE;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isPlayer())
		{
			L2PcInstance activeChar = getEffected().getActingPlayer();
			activeChar.getAppearance().setInvisible();
			activeChar.startAbnormalEffect(AbnormalEffect.STEALTH);
			
			if ((activeChar.getAI().getNextIntention() != null) && (activeChar.getAI().getNextIntention().getCtrlIntention() == CtrlIntention.AI_INTENTION_ATTACK))
			{
				activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			}
			
			L2GameServerPacket del = new DeleteObject(activeChar);
			for (L2Character target : activeChar.getKnownList().getKnownCharacters())
			{
				try
				{
					if (target.getTarget() == activeChar)
					{
						target.setTarget(null);
						target.abortAttack();
						target.abortCast();
						target.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					}
					
					if (target.isPlayer())
					{
						target.sendPacket(del);
					}
				}
				catch (NullPointerException e)
				{
				}
			}
		}
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected().isPlayer())
		{
			L2PcInstance activeChar = getEffected().getActingPlayer();
			if (!activeChar.inObserverMode())
			{
				activeChar.getAppearance().setVisible();
			}
			activeChar.stopAbnormalEffect(AbnormalEffect.STEALTH);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}