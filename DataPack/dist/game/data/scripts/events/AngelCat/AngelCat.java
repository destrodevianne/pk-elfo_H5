package events.AngelCat;

import pk.elfo.gameserver.instancemanager.GlobalVariablesManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
 
/**
 * Projeto PkElfo
 */

public class AngelCat extends Quest
{
	private final int _angelCat = 4308;
	private final int _gift = 21726;
	private final int _amount = 1;
	
	private final int[][] _spawns =
	{
		{ 82920, 147851, -3469, 0 },
	};
	
	public AngelCat(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(_angelCat);
		addFirstTalkId(_angelCat);
		addTalkId(_angelCat);
		
		for (int[] spawn : _spawns)
		{
			addSpawn(_angelCat, spawn[0], spawn[1], spawn[2], spawn[3], false, 0);
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String var = GlobalVariablesManager.getInstance().getStoredVariable("AngelCatGift-" + player.getAccountName());
		if (var == null)
		{
			player.addItem("AngelCat-Gift", _gift, _amount, npc, true);
			GlobalVariablesManager.getInstance().storeVariable("AngelCatGift-" + player.getAccountName(), String.valueOf(System.currentTimeMillis()));
		}
		else
		{
			player.sendPacket(SystemMessage.getSystemMessage(3289));
		}
		return super.onTalk(npc, player);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return npc.getNpcId() + ".htm";
	}
	
	public static void main(String[] args)
	{
		new AngelCat(-1, AngelCat.class.getSimpleName(), "events");
	}
}