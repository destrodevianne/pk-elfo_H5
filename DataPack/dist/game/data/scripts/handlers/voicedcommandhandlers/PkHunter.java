package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.PkHunterEvent;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillTeleport;

public class Voiced_PkHunter implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS =
    {
        "gopk",
        "pkinfo"
    };

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (command.toLowerCase().equals("gopk"))
        {
            if (activeChar.isFestivalParticipant() || activeChar.isInJail() || activeChar.isInDuel() || activeChar.isInOlympiadMode() || activeChar.inObserverMode() || PkHunterEvent.isPk(activeChar))
            {
                activeChar.sendMessage("Voce nao pode usar este comando durante a sua participacao no Festival!");
                return false;
            }

            if (PkHunterEvent.isActive())
            {
                StatsSet set = new StatsSet();
                set.set("skill_id", "2525");
                set.set("level", "1");
                set.set("target", "TARGET_SELF");
                set.set("name", "Desloque-se para mover para a area Pk Hunter Evento");
                set.set("isMagic", "2");
                set.set("itemConsumeCount", "0");
                set.set("hitTime", "10000");
                set.set("operateType", "A1");
                set.set("skillType", "TELEPORT");
                activeChar.teleToLocation(PkHunterEvent.getPkLocation()[0], PkHunterEvent.getPkLocation()[1], PkHunterEvent.getPkLocation()[2]);
                activeChar.useMagic(new L2SkillTeleport(set), false, true);
                activeChar.sendMessage("Movendo-se para a area do Pk Hunter Evento ...");
            }
            else
            {
                activeChar.sendMessage("Evento Pk Hunter nao esta ativo no momento.");
                return false;
            }
        }
        else if (command.toLowerCase().equals("pkinfo"))
        {
            PkHunterEvent.sendLocationMessage(activeChar);
        }
        return true;
    }

    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}