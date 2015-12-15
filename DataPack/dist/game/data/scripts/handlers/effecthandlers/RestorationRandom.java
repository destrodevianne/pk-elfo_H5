package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import pk.elfo.Config;
import pk.elfo.gameserver.model.L2ExtractableProductItem;
import pk.elfo.gameserver.model.L2ExtractableSkill;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class RestorationRandom extends L2Effect
{
	public RestorationRandom(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffector() == null) || (getEffected() == null) || !getEffector().isPlayer() || !getEffected().isPlayer())
		{
			return false;
		}
		
		final L2ExtractableSkill exSkill = getSkill().getExtractableSkill();
		if (exSkill == null)
		{
			return false;
		}
		
		if (exSkill.getProductItems().isEmpty())
		{
			_log.warning("Habilidade extraivel sem dados, provavelmente errado / mesa vazia em Skill Id: " + getSkill().getId());
			return false;
		}
		
		final double rndNum = 100 * Rnd.nextDouble();
		double chance = 0;
		double chanceFrom = 0;
		final List<ItemHolder> creationList = new ArrayList<>();
		
		// Explanation for future changes:
		// You get one chance for the current skill, then you can fall into
		// one of the "areas" like in a roulette.
		// Example: for an item like Id1,A1,30;Id2,A2,50;Id3,A3,20;
		// #---#-----#--#
		// 0--30----80-100
		// If you get chance equal 45% you fall into the second zone 30-80.
		// Meaning you get the second production list.
		// Calculate extraction
		for (L2ExtractableProductItem expi : exSkill.getProductItems())
		{
			chance = expi.getChance();
			if ((rndNum >= chanceFrom) && (rndNum <= (chance + chanceFrom)))
			{
				creationList.addAll(expi.getItems());
				break;
			}
			chanceFrom += chance;
		}
		
		final L2PcInstance player = getEffected().getActingPlayer();
		if (creationList.isEmpty())
		{
			player.sendPacket(SystemMessageId.NOTHING_INSIDE_THAT);
			return false;
		}
		
		for (ItemHolder item : creationList)
		{
			if ((item.getId() <= 0) || (item.getCount() <= 0))
			{
				continue;
			}
			player.addItem("Extract", item.getId(), (long) (item.getCount() * Config.RATE_EXTRACTABLE), getEffector(), true);
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.RESTORATION_RANDOM;
	}
}