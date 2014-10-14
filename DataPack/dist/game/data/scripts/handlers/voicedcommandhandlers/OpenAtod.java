package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;

/**
 * PkElfo
 */

public class OpenAtod implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands =
    {
        "openatod"
    };

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
    {
        if (command.equalsIgnoreCase("openatod"))
        {
            if (params == null)
            {
                activeChar.sendMessage("Use: .openatod [num]");
            }
            else
            {
                int num = 0;
                try {num = Integer.parseInt(params);}
                catch (NumberFormatException nfe)
                {
                    activeChar.sendMessage("Voce deve digitar um numero. Use: .openatod [num]");
                    return false;
                }

                if (num == 0)
                {
                    return false;
                }
                else if (activeChar.getInventory().getInventoryItemCount(9599, 0) >= num)
                {
                    int a=0, b=0, c=0, rnd;
                    for (int i=0; i<num;i++)
                    {
                        rnd = Rnd.get(100);
                        if ((rnd <= 100) && (rnd > 44))
                        {
                            a++;
                        }
                        else if ((rnd <= 44) && (rnd > 14))
                        {
                            b++;
                        }
                        else if (rnd <= 14)
                        {
                            c++;
                        }
                    }
                    if (activeChar.destroyItemByItemId("ATOD", 9599, a+b+c, null, true))
                    {
                        if (a>0)
                        {
                            activeChar.addItem("ATOD", 9600, a, null, true);
                        }
                        if (b>0)
                        {
                            activeChar.addItem("ATOD", 9601, b, null, true);
                        }
                        if (c>0)
                        {
                            activeChar.addItem("ATOD", 9602, c, null, true);
                        }
                    }
                    else
                    {
                        activeChar.sendMessage("Voce nao tem numero suficiente de vezes.");
                    }
                }
                else
                {
                    activeChar.sendMessage("Voce nao tem numero suficiente de vezes.");
                }
            }
        }
        return false;
    }

    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}