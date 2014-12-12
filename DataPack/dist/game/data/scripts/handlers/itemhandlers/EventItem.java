package handlers.itemhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.HandysBlockCheckerManager;
import pk.elfo.gameserver.instancemanager.HandysBlockCheckerManager.ArenaParticipantsHolder;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2BlockInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class EventItem implements IItemHandler
{
	private static final Logger _log = Logger.getLogger(EventItem.class.getName());
	
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		boolean used = false;
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		
		final int itemId = item.getItemId();
		switch (itemId)
		{
			case 13787: // Handy's Block Checker Bond
				used = useBlockCheckerItem(activeChar, item);
				break;
			case 13788: // Handy's Block Checker Land Mine
				used = useBlockCheckerItem(activeChar, item);
				break;
			default:
				_log.warning("EventItemHandler: Item com id: " + itemId + " nao e handled");
		}
		return used;
	}
	
	private final boolean useBlockCheckerItem(final L2PcInstance castor, L2ItemInstance item)
	{
		final int blockCheckerArena = castor.getBlockCheckerArena();
		if (blockCheckerArena == -1)
		{
			SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			msg.addItemName(item);
			castor.sendPacket(msg);
			return false;
		}
		
		final L2Skill sk = item.getEtcItem().getSkills()[0].getSkill();
		if (sk == null)
		{
			return false;
		}
		
		if (!castor.destroyItem("Consume", item, 1, castor, true))
		{
			return false;
		}
		
		final L2BlockInstance block = (L2BlockInstance) castor.getTarget();
		
		final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(blockCheckerArena);
		if (holder != null)
		{
			final int team = holder.getPlayerTeam(castor);
			for (final L2PcInstance pc : block.getKnownList().getKnownPlayersInRadius(sk.getEffectRange()))
			{
				final int enemyTeam = holder.getPlayerTeam(pc);
				if ((enemyTeam != -1) && (enemyTeam != team))
				{
					sk.getEffects(castor, pc);
				}
			}
			return true;
		}
		_log.warning("Char: " + castor.getName() + "[" + castor.getObjectId() + "] tem desconhecido o bloco da arena");
		return false;
	}
}