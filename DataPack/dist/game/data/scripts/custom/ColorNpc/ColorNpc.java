package custom.ColorNpc;

import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

public class ColorNpc extends Quest
{
	// Npc
	private static final int NpcId = 49295; // Custom Npc
	// Item
	private static final int ItemId = 6673; // Festival Adena

	public ColorNpc(int questId, String name, String descr)
    {
        super(questId, name, descr);
        addStartNpc(NpcId);
        addFirstTalkId(NpcId);
        addTalkId(NpcId);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        String htmltext = event;
        final QuestState st = player.getQuestState(getName());

        // Green
        if (event.equalsIgnoreCase("1"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x009900);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Blue
        else if (event.equalsIgnoreCase("2"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0xff7f00);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Purple
        else if (event.equalsIgnoreCase("3"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0xff00ff);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Yellow
        else if (event.equalsIgnoreCase("4"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x00ffff);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Red
        else if (event.equalsIgnoreCase("5"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x0000ff);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Orange
        else if (event.equalsIgnoreCase("6"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x0099ff);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        //  Light Green
        else if (event.equalsIgnoreCase("7"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x70db93);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Gray
        else if (event.equalsIgnoreCase("8"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0x9f9f9f);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        // Aqua
        else if (event.equalsIgnoreCase("9"))
        {
            if(st.getQuestItemsCount(ItemId) >= 1)
            {
                st.takeItems(ItemId,1);
                st.getPlayer().getAppearance().setTitleColor(0xffff00);
                player.sendPacket(new UserInfo(player));
                player.sendMessage("A cor do seu titulo foi alterada!");
                return null;
            }
            else
            {
                return "49295-no.htm";
            }
        }
        return htmltext;
    }

    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player)
    {
        QuestState st = player.getQuestState(getName());
        if (st == null)
        {
            Quest q = QuestManager.getInstance().getQuest(getName());
            st = q.newQuestState(player);
        }
        return "49295.htm";
    }

    public static void main(String[] args)
    {
        new ColorNpc(-1, "ColorNpc", "custom");
        _log.info("ColorNpc: Iniciado com sucesso.");
    }
}