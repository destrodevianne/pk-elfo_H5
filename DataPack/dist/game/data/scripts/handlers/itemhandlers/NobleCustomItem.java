package handlers.itemhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.SocialAction;

/**
 * PkElfo
 */
 
public class NobleCustomItem implements IItemHandler
{

	public NobleCustomItem()
	{
	//null
	}

	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if(Config.NOBLE_CUSTOM_ITEMS)
		{
			if(!(playable instanceof L2PcInstance))
				return false;

			L2PcInstance activeChar = (L2PcInstance) playable;

			if(activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("Este item nao pode ser usado nos jogos da Olimpiada.");
			}

			if(activeChar.isNoble())
			{
				activeChar.sendMessage("Voce ja tem o estatus de Nobre!.");
			}
			else
			{
				activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
				activeChar.setNoble(true);
				activeChar.sendMessage("Agora voce e um Nobre,lhe foi condecido o estatus de Nobre e as habilidades de Nobre.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
				activeChar.getInventory().addItem("Tiara", 7694, 1, activeChar, null);
			}
			activeChar = null;
		}
		return false;
	}

	public int[] getItemIds()
	{
		return ITEM_IDS;
	}

	private static final int ITEM_IDS[] =
	{
		Config.NOOBLE_CUSTOM_ITEM_ID
	};

}