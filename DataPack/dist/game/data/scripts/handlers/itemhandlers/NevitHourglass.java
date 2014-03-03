package handlers.itemhandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
 
public class NevitHourglass extends ItemSkills {
        
        @Override
        public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
        {
                if (!(playable instanceof L2PcInstance))
                {
                        playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ITEM_NOT_FOR_PETS));
                        return false;
                }
                
                L2PcInstance activeChar = (L2PcInstance)playable;
                if( activeChar.RecoBonusActive() )
                {
                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
                        sm.addItemName(item.getItemId());
                        activeChar.sendPacket(sm);
                        return false;
                }
                return super.useItem(playable, item, forceUse);
        }
}