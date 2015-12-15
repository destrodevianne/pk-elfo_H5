package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import pk.elfo.gameserver.ai.CtrlEvent;
import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.idfactory.IdFactory;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2EffectPointInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSignetCasttime;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillLaunched;
import pk.elfo.gameserver.util.Point3D;

/**
 * Projeto PkElfo
 */
 
public class SignetMDam extends L2Effect
{
	private L2EffectPointInstance _actor;
	
	public SignetMDam(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SIGNET_GROUND;
	}
	
	@Override
	public boolean onStart()
	{
		L2NpcTemplate template;
		if (getSkill() instanceof L2SkillSignetCasttime)
		{
			template = NpcTable.getInstance().getTemplate(getSkill().getNpcId());
		}
		else
		{
			return false;
		}
		
		final L2EffectPointInstance effectPoint = new L2EffectPointInstance(IdFactory.getInstance().getNextId(), template, getEffector());
		effectPoint.setCurrentHp(effectPoint.getMaxHp());
		effectPoint.setCurrentMp(effectPoint.getMaxMp());
		
		int x = getEffector().getX();
		int y = getEffector().getY();
		int z = getEffector().getZ();
		
		if (getEffector().isPlayer() && (getSkill().getTargetType() == L2TargetType.TARGET_GROUND))
		{
			final Point3D wordPosition = getEffector().getActingPlayer().getCurrentSkillWorldPosition();
			
			if (wordPosition != null)
			{
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}
		effectPoint.setIsInvul(true);
		effectPoint.spawnMe(x, y, z);
		
		_actor = effectPoint;
		return true;
		
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getCount() >= (getTotalCount() - 2))
		{
			return true; // do nothing first 2 times
		}
		int mpConsume = getSkill().getMpConsume();
		
		final L2PcInstance activeChar = getEffector().getActingPlayer();
		
		activeChar.rechargeShots(getSkill().useSoulShot(), getSkill().useSpiritShot());
		
		boolean sps = getSkill().useSpiritShot() && getEffector().isChargedShot(ShotType.SPIRITSHOTS);
		boolean bss = getSkill().useSpiritShot() && getEffector().isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		List<L2Character> targets = new ArrayList<>();
		
		for (L2Character cha : _actor.getKnownList().getKnownCharactersInRadius(getSkill().getSkillRadius()))
		{
			if ((cha == null) || (cha == activeChar))
			{
				continue;
			}
			
			if (cha.isL2Attackable() || cha.isPlayable())
			{
				if (cha.isAlikeDead())
				{
					continue;
				}
				
				if (mpConsume > activeChar.getCurrentMp())
				{
					activeChar.sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
					return false;
				}
				
				activeChar.reduceCurrentMp(mpConsume);
				if (cha.isPlayable())
				{
					if (activeChar.canAttackCharacter(cha))
					{
						targets.add(cha);
						activeChar.updatePvPStatus(cha);
					}
				}
				else
				{
					targets.add(cha);
				}
			}
		}
		
		if (!targets.isEmpty())
		{
			activeChar.broadcastPacket(new MagicSkillLaunched(activeChar, getSkill().getId(), getSkill().getLevel(), targets.toArray(new L2Character[targets.size()])));
			for (L2Character target : targets)
			{
				final boolean mcrit = Formulas.calcMCrit(activeChar.getMCriticalHit(target, getSkill()));
				final byte shld = Formulas.calcShldUse(activeChar, target, getSkill());
				final int mdam = (int) Formulas.calcMagicDam(activeChar, target, getSkill(), shld, sps, bss, mcrit);
				
				if (target.isSummon())
				{
					target.broadcastStatusUpdate();
				}
				
				if (mdam > 0)
				{
					if (!target.isRaid() && Formulas.calcAtkBreak(target, mdam))
					{
						target.breakAttack();
						target.breakCast();
					}
					activeChar.sendDamageMessage(target, mdam, mcrit, false, false);
					target.reduceCurrentHp(mdam, activeChar, getSkill());
				}
				target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
			}
		}
		activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (_actor != null)
		{
			_actor.deleteMe();
		}
	}
}