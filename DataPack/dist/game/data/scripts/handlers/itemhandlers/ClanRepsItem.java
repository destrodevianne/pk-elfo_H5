package handlers.itemhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;

/**
 * PkElfo
 */
 
public class ClanRepsItem implements IItemHandler
{
    private static final int ITEM_IDS[] = 
    { 
    	Config.CR_ITEM_REPS_ITEM_ID
    };

	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}

            L2PcInstance activeChar = (L2PcInstance)playable;

            if (!activeChar.isClanLeader())
            {
                activeChar.sendMessage("Isso pode ser usado apenas por Lideres de Clan!");
                return false;
            }
        	 
            else if (!(activeChar.getClan().getLevel() >= Config.CR_ITEM_MIN_CLAN_LVL))
            {
            	activeChar.sendMessage("O nivel do seu Clan nao esta o suficiente para usar este item!");
            	return false;
            }
            else
            {
            	//activeChar.getClan().setReputationScore(activeChar.getClan().getReputationScore()+Config.CR_ITEM_REPS_TO_BE_AWARDED, true);
            	activeChar.getClan().addReputationScore(Config.CR_ITEM_REPS_TO_BE_AWARDED, true);
            	activeChar.sendMessage("Seu clan ganhou "+ Config.CR_ITEM_REPS_TO_BE_AWARDED +" pontos de reputacao!");
            	MagicSkillUse  MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
            	activeChar.broadcastPacket(MSU);
              playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
            }
			return false;
        }

    public int[] getItemIds()
    {
        return ITEM_IDS;
   }
}