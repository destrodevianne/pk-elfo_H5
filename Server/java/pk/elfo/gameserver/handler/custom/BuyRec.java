package pk.elfo.gameserver.handler.custom;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class BuyRec implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS =
    {
        "buyrec"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (command.equalsIgnoreCase("buyrec") && activeChar.isVip())
        {
            if(Config.REC_BUY)
            {
                if(activeChar.getInventory().getItemByItemId(Config.REC_ITEM_ID) != null && activeChar.getInventory().getItemByItemId(Config.REC_ITEM_ID).getCount() >= Config.REC_PRICE)
                {
                    activeChar.getInventory().destroyItemByItemId("Rec", Config.REC_ITEM_ID, Config.REC_PRICE, activeChar, activeChar.getTarget());
                    activeChar.setRecomHave(activeChar.getRecomHave() + Config.REC_REWARD);
                    activeChar.sendMessage("Voce ganhou "+Config.REC_REWARD+" Recomends.");
                    activeChar.broadcastUserInfo();
                }
                else
                {
                    activeChar.sendMessage("You don't have enought items");
                    return true;
                }
            }
            else
            {
                activeChar.sendMessage("Command Disable By Admin");
            }
        }
        return false;
    }
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}