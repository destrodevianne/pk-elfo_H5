package pk.elfo.gameserver.scripts.data;

import java.util.List;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.util.Rnd;
import javolution.util.FastList;

public class NevitsHerald extends Quest
{
	private static final List<L2Npc> spawns = new FastList<>();
	private static boolean isActive = false;
	
	private static final int NevitsHerald = 4326;
	private static final int[] Antharas =
	{
		29019,
		29066,
		29067,
		2906
	};
	private static final int Valakas = 29028;
	private static final NpcStringId[] spam =
	{
		NpcStringId.SHOW_RESPECT_TO_THE_HEROES_WHO_DEFEATED_THE_EVIL_DRAGON_AND_PROTECTED_THIS_ADEN_WORLD,
		NpcStringId.SHOUT_TO_CELEBRATE_THE_VICTORY_OF_THE_HEROES,
		NpcStringId.PRAISE_THE_ACHIEVEMENT_OF_THE_HEROES_AND_RECEIVE_NEVITS_BLESSING
	};
	
	private static final int[][] _spawns =
		{
        { 86979, -142785, -1341, 18259 },    // Town of Schuttgart
        { 44168, -48513, -801, 31924 },        // Rune Township
        { 148002, -55279, -2735, 44315 },    // Town of Goddard
        { 147953, 26656, -2205, 20352 },    // Town of Aden
        { 82313, 53280, -1496, 14791 },        // Town of Oren
        { 81918, 148305, -3471, 49151 },    // Town of Giran
        { 16286, 142805, -2706, 15689 },    // Town of Dion
        { -13968, 122050, -2990, 19497 },    // Town of Gludio
        { -83207, 150896, -3129, 30709 },    // Gludin Village
        { 116892, 77277, -2695, 45056 }        // Hunters Village
    };
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		return "4326.htm";
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		
		if (npc.getNpcId() == NevitsHerald)
		{
			if (event.equalsIgnoreCase("buff"))
			{
				if (player.getFirstEffect(L2EffectType.NEVIT_HOURGLASS) != null)
				{
					return "4326-1.htm";
				}
				npc.setTarget(player);
				npc.doCast(L2Skill.valueOf(23312, 1));
				return null;
			}
		}
		else if (event.equalsIgnoreCase("text_spam"))
		{
			cancelQuestTimer("text_spam", npc, player);
			npc.broadcastPacket(new NpcSay(NevitsHerald, Say2.SHOUT, NevitsHerald, spam[Rnd.get(0, spam.length - 1)]));
			startQuestTimer("text_spam", 60000, npc, player);
			return null;
		}
		else if (event.equalsIgnoreCase("despawn"))
		{
			despawnHeralds();
		}
		return htmltext;
	}
	
	private void despawnHeralds()
	{
		if (!spawns.isEmpty())
		{
			for (L2Npc npc : spawns)
			{
				npc.deleteMe();
			}
		}
		spawns.clear();
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		ExShowScreenMessage message = null;
		if (npc.getNpcId() == Valakas)
		{
			// message = new ExShowScreenMessage(1900151, 10000, null);
			message = new ExShowScreenMessage(NpcStringId.THE_EVIL_FIRE_DRAGON_VALAKAS_HAS_BEEN_DEFEATED, 2, 10000);
		}
		else
		{
			// message = new ExShowScreenMessage(1900150, 10000, null);
			message = new ExShowScreenMessage(NpcStringId.THE_EVIL_LAND_DRAGON_ANTHARAS_HAS_BEEN_DEFEATED, 2, 10000);
		}
		
		message.setUpperEffect(true);
		
		for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
		{
			if (onlinePlayer == null)
			{
				continue;
			}
			onlinePlayer.sendPacket(message);
		}
		
		if (!isActive)
		{
			isActive = true;
			
			spawns.clear();
			
			for (int[] _spawn : _spawns)
			{
				L2Npc herald = addSpawn(NevitsHerald, _spawn[0], _spawn[1], _spawn[2], _spawn[3], false, 0);
				if (herald != null)
				{
					spawns.add(herald);
				}
			}
			startQuestTimer("despawn", 14400000, npc, killer);
			startQuestTimer("text_spam", 3000, npc, killer);
		}
		return null;
	}
	
	public NevitsHerald(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addFirstTalkId(NevitsHerald);
		addStartNpc(NevitsHerald);
		addTalkId(NevitsHerald);
		for (int _npc : Antharas)
		{
			addKillId(_npc);
		}
		addKillId(Valakas);
	}
	
	public static void main(String[] args)
	{
		new NevitsHerald(-1, "NevitsHerald", "npc");
	}
}