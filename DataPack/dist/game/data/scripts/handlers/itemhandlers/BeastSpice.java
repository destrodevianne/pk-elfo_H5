package handlers.itemhandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2FeedableBeastInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class BeastSpice implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		final SkillHolder[] skills = item.getItem().getSkills();
		
		if (skills == null)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": esta ausente as habilidades!");
			return false;
		}
		
		if (!(activeChar.getTarget() instanceof L2FeedableBeastInstance))
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return false;
		}
		
		for (SkillHolder sk : skills)
		{
			activeChar.useMagic(sk.getSkill(), false, false);
		}
		return true;
	}
}