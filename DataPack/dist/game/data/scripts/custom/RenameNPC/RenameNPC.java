package custom.RenameNPC;

import pk.elfo.Config;
import pk.elfo.gameserver.communitybbs.Manager.RegionBBSManager;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.serverpackets.PartySmallWindowAll;
import pk.elfo.gameserver.network.serverpackets.PartySmallWindowDeleteAll;
import pk.elfo.gameserver.util.Util;

public class RenameNPC extends Quest
{
	private final static int NPC = Config.RENAME_NPC_ID;

	public RenameNPC(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(NPC);
		addStartNpc(NPC);
		addTalkId(NPC);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "Novo Nome:<br1><edit var=\"newname\" width=120 height=18>";
		String eventSplit[] = event.split(" ");
		QuestState st = player.getQuestState(getName());
		
		if (eventSplit[0].equalsIgnoreCase("rename"))
		{
			st.getPlayer().setTarget(st.getPlayer());
			if (eventSplit.length != 2)
			{
				htmltext = "Digite um novo nome ou remova o espaco entre os nomes.";
			}
			else if (st.getPlayer().getLevel() < Config.RENAME_NPC_MIN_LEVEL)
			{
				htmltext = "Level minimo: " + String.valueOf(Config.RENAME_NPC_MIN_LEVEL);
			}
			else if (validItemFee(st))
			{
				htmltext = "Voce nao tem moedas suficientes para trocar seu nome.";
			}
			else if (eventSplit[1].length() < 1 || eventSplit[1].length() > 16)
			{
				htmltext = "Numero maximo de caracteres: 16";
			}
			else if (!Util.isAlphaNumeric(eventSplit[1]))
			{
				htmltext = "O nome deve conter apenas caracteres alfanumericos.";
			}
			else if (CharNameTable.getInstance().doesCharNameExist(eventSplit[1]))
			{
				htmltext = "O nome escolhido ja esta em uso. Escolha outro nome.";
			}
			else
			{
				try
				{
					L2World.getInstance().removeFromAllPlayers(player);
					player.setName(eventSplit[1]);
					player.store();
					L2World.getInstance().addToAllPlayers(player);
					htmltext = "Seu nome foi alterado com sucesso.";
					player.broadcastUserInfo();
					
					String itemFeeSplit[] = Config.RENAME_NPC_FEE.split("\\;");
                    for (String anItemFeeSplit : itemFeeSplit)
                    {
                        String item[] = anItemFeeSplit.split("\\,");
                        st.takeItems(Integer.parseInt(item[0]), Integer.parseInt(item[1]));
                    }
			
					if (player.isInParty())
					{
						player.getParty().broadcastToPartyMembers(player, new PartySmallWindowDeleteAll());
						for (L2PcInstance member : player.getParty().getPartyMembers())
						{
							if (member != player)
							{
								member.sendPacket(new PartySmallWindowAll(member, player.getParty()));
							}
						}
					}
					if (player.getClan() != null)
						player.getClan().broadcastClanStatus();
					RegionBBSManager.getInstance().changeCommunityBoard();
				}
				catch (StringIndexOutOfBoundsException e)
				{ 
					htmltext = "Servico nao disponivel!";
				}
			}
			return (page(htmltext,1));
		}
		return (page(htmltext,0));
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			Quest q = QuestManager.getInstance().getQuest(getName());
			st = q.newQuestState(player);
		}
		htmltext = page("Novo Nome:<br1><edit var=\"newname\" width=70 height=10>",0);
		return htmltext;
	}
	
	public String page(String msg, int t)
	{
		String htmltext = "";
		htmltext += htmlPage("Title");
		htmltext += "Ola eu estou aqui para ajuda-lo a mudar o seu nome.<br>" + "Digite o novo nome, mas certifique-se que voce tem moedas suficiente para a troca:<br1>";
		String itemFeeSplit[] = Config.RENAME_NPC_FEE.split("\\;");
        for (String anItemFeeSplit : itemFeeSplit)
        {
            String item[] = anItemFeeSplit.split("\\,");
            htmltext += "<font color=\"LEVEL\">" + item[1] + " " + ItemTable.getInstance().getTemplate(Integer.parseInt(item[0])).getName() + "</font><br1>";
        }
		if (t == 0)
		{
			htmltext += "<br><font color=\"339966\">" + msg + "</font>";
			htmltext += "<br><center>" + button("Rename", "rename $newname", 70, 23) + "</center>";
		}		
		else
		{
			htmltext += "<br><font color=\"FF0000\">" + msg + "</font>";
			htmltext += "<br><center>" + button("Back", "begin", 70, 23) + "</center>";
		}
		htmltext += htmlPage("Footer");
		return htmltext;
	}
	public Boolean validItemFee(QuestState st)
	{
		String itemFeeSplit[] = Config.RENAME_NPC_FEE.split("\\;");
        for (String anItemFeeSplit : itemFeeSplit)
        {
            String item[] = anItemFeeSplit.split("\\,");
            if (st.getQuestItemsCount(Integer.parseInt(item[0])) < Integer.parseInt(item[1]))
                return true;
        }
		return false;
	}
	
	public String htmlPage(String op)
	{
		String texto = "";
		if (op.equals("Title"))
		{
			texto += "<html><body><title>Gerente do servico</title><center><br>" + "<b><font color=ffcc00>Informacao sobre o servico</font></b>" + "<br><img src=\"L2UI_CH3.herotower_deco\" width=\"256\" height=\"32\"><br></center>";
		}
		else if (op.equals("Footer"))
		{
			texto += "<br><center><img src=\"L2UI_CH3.herotower_deco\" width=\"256\" height=\"32\"><br>" + "<br><font color=\"303030\">---</font></center></body></html>";
		}
		else
		{
			texto = "Nao encontrado!";
		}
		return texto;
	}

	public String button(String name, String event, int w, int h)
	{
		return "<button value=\"" + name + "\" action=\"bypass -h Quest RenameNPC " + event + "\" " + "width=\"" + Integer.toString(w) + "\" height=\"" + Integer.toString(h) + "\" " + "back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	}

	public String link(String name, String event, String color)
	{
		return "<a action=\"bypass -h Quest RenameNPC " + event + "\">" + "<font color=\"" + color + "\">" + name + "</font></a>";
	}	
	
	public static void main(String[] args)
	{
		new RenameNPC(-1, "RenameNPC", "custom");
	}
}