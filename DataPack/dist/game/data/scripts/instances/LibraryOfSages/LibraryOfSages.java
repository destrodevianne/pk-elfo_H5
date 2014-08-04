package instances.LibraryOfSages;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Instance;
import pk.elfo.gameserver.model.instancezone.InstanceWorld;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.Rnd;

import javolution.util.FastList;
import javolution.util.FastMap;

public class LibraryOfSages extends Quest
{
	private static final String qn = "LibraryOfSages";
	// Values
	private static final int INSTANCE_ID = 156;
	// NPC's
	private static final int Sophia = 32596;
	private static final int Sophia2 = 32861;
	private static final int Sophia3 = 32863;
	private static final int Elcadia_Support = 32785;
	// Teleports
	private static final int ENTER = 0;
	private static final int EXIT = 1;
	private static final int HidenRoom = 2;
	private static final int[][] TELEPORTS = { { 37063, -49813, -1128 }, { 37063, -49813, -1128 }, { 37355, -50065, -1127 } // books
	};
	
	private static final NpcStringId[] spam = {
		NpcStringId.I_MUST_ASK_LIBRARIAN_SOPHIA_ABOUT_THE_BOOK,
		NpcStringId.THIS_LIBRARY_ITS_HUGE_BUT_THERE_ARENT_MANY_USEFUL_BOOKS_RIGHT,
		NpcStringId.AN_UNDERGROUND_LIBRARY_I_HATE_DAMP_AND_SMELLY_PLACES,
		NpcStringId.THE_BOOK_THAT_WE_SEEK_IS_CERTAINLY_HERE_SEARCH_INCH_BY_INCH
	};
	private final FastMap<Integer, InstanceHolder> instanceWorlds = new FastMap<Integer, InstanceHolder>();
	
	private static class InstanceHolder
	{
		FastList<L2Npc> mobs = new FastList<L2Npc>();
	}
	
	private class LibraryOfSagesWorld extends InstanceWorld
	{
		public LibraryOfSagesWorld()
		{
		}
	}
	
	private void teleportPlayer(L2Npc npc, L2PcInstance player, int[] coords, int instanceId)
	{
		InstanceHolder holder = instanceWorlds.get(instanceId);
		if (holder == null && instanceId > 0)
		{
			holder = new InstanceHolder();
			instanceWorlds.put(instanceId, holder);
		}
		player.stopAllEffectsExceptThoseThatLastThroughDeath();
		player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		player.setInstanceId(instanceId);
		player.teleToLocation(coords[0], coords[1], coords[2], false);
		cancelQuestTimer("check_follow", npc, player);
		if (holder != null)
		{
			for(L2Npc h : holder.mobs)
			{
				h.deleteMe();
			}
			holder.mobs.clear();
		}
		if (instanceId > 0)
		{
			L2Npc support = addSpawn(Elcadia_Support, player.getX(), player.getY(),player.getZ(), 0, false, 0, false, player.getInstanceId());
			holder.mobs.add(support);
			startQuestTimer("check_follow", 3000, support, player);
		}
	}
	
	protected void enterInstance(L2Npc npc, L2PcInstance player)
	{
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		if (world != null)
		{
			if (!(world instanceof LibraryOfSagesWorld))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER));
				return;
			}
			Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
			if (inst != null)
			{
				teleportPlayer(npc, player, TELEPORTS[ENTER], world.getInstanceId());
			}
			return;
		}
		final int instanceId = InstanceManager.getInstance().createDynamicInstance("[020] Library Of Sages.xml");
		
		world = new LibraryOfSagesWorld();
		world.setInstanceId(instanceId);
		world.setTemplateId(INSTANCE_ID);
		world.setStatus(0);
		InstanceManager.getInstance().addWorld(world);
		
		world.addAllowed(player.getObjectId());
		
		teleportPlayer(npc, player, TELEPORTS[ENTER], instanceId);
		return;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{		
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(qn);
		if (st == null)
			st = newQuestState(player);
		
		if (event.equalsIgnoreCase("check_follow"))
		{
			cancelQuestTimer("check_follow", npc, player);
			npc.getAI().stopFollow();
			npc.setIsRunning(true);
			npc.getAI().startFollow(player);
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.ALL, npc.getNpcId(), spam[Rnd.get(0, spam.length - 1)]));
			startQuestTimer("check_follow", 20000, npc, player);
			return "";
		}
		else if (npc.getNpcId() == Sophia)
		{
			if (event.equalsIgnoreCase("tele1"))
			{
				enterInstance(npc, player);
				return null;
			}
		}
		else if (npc.getNpcId() == Sophia2)
		{
			if (event.equalsIgnoreCase("tele2"))
			{
				teleportPlayer(npc, player, TELEPORTS[HidenRoom], player.getInstanceId());
				return null;
			}
			else if (event.equalsIgnoreCase("tele3"))
			{
				InstanceHolder holder = instanceWorlds.get(player.getInstanceId());
				if (holder != null)
				{
					for(L2Npc h : holder.mobs)
					{
						h.deleteMe();
					}
					holder.mobs.clear();
				}
				teleportPlayer(npc, player, TELEPORTS[EXIT], 0);
				return null;
			}
		}
		else if (npc.getNpcId() == Sophia3)
		{
			if (event.equalsIgnoreCase("tele4"))
			{
				teleportPlayer(npc, player, TELEPORTS[ENTER], player.getInstanceId());
				return null;
			}
		}
		return htmltext;
	}
	
	public LibraryOfSages(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(Sophia);
		addStartNpc(Sophia2);
		addTalkId(Sophia);
		addTalkId(Sophia2);
		addTalkId(Sophia3);
		addTalkId(Elcadia_Support);
	}
	
	public static void main(String[] args)
	{
		new LibraryOfSages(-1, qn, "instances");
	}
}