package handlers.effecthandlers;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2EffectPointInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSignet;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSignetCasttime;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */
 
public class Signet extends L2Effect
{
	private L2Skill _skill;
	private L2EffectPointInstance _actor;
	private boolean _srcInArena;
	
	public Signet(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SIGNET_EFFECT;
	}
	
	@Override
	public boolean onStart()
	{
		if (getSkill() instanceof L2SkillSignet)
		{
			_skill = SkillTable.getInstance().getInfo(getSkill().getEffectId(), getLevel());
		}
		else if (getSkill() instanceof L2SkillSignetCasttime)
		{
			_skill = SkillTable.getInstance().getInfo(getSkill().getEffectId(), getLevel());
		}
		_actor = (L2EffectPointInstance) getEffected();
		_srcInArena = (getEffector().isInsideZone(ZoneId.PVP) && !getEffector().isInsideZone(ZoneId.SIEGE));
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (_skill == null)
		{
			return true;
		}
		int mpConsume = _skill.getMpConsume();
		
		if (mpConsume > getEffector().getCurrentMp())
		{
			getEffector().sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
			return false;
		}
		
		getEffector().reduceCurrentMp(mpConsume);
		FastList<L2Character> targets = FastList.newInstance();
		for (L2Character cha : _actor.getKnownList().getKnownCharactersInRadius(getSkill().getSkillRadius()))
		{
			if (cha == null)
			{
				continue;
			}
			
			if (_skill.isOffensive() && !L2Skill.checkForAreaOffensiveSkills(getEffector(), cha, _skill, _srcInArena))
			{
				continue;
			}
			
			// there doesn't seem to be a visible effect with MagicSkillLaunched packet...
			_actor.broadcastPacket(new MagicSkillUse(_actor, cha, _skill.getId(), _skill.getLevel(), 0, 0));
			targets.add(cha);
		}
		
		if (!targets.isEmpty())
		{
			getEffector().callSkill(_skill, targets.toArray(new L2Character[targets.size()]));
		}
		FastList.recycle(targets);
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