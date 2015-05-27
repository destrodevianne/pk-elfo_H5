package custom.LuckyPig;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javolution.util.FastList;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */

public class LuckyPig extends AbstractNpcAI
{
	private final int LUCKY_PIG_NPC = 18666;
	private final int LUCKY_PIG_MOB_PINK = 2502;
	private final int LUCKY_PIG_MOB_YELLOW = 2503;
	
	private final Map<Integer, List<Long>> _ADENAS;
	
	private final int[] _MOBS =
	{
		// TODO: Add Correct Monsters
		22862, 22823
	};
	
	public LuckyPig(int questId, String name, String descr)
	{
		super(name, descr);
		
		_ADENAS = new FastMap<Integer, List<Long>>().shared();
		
		registerMobs(_MOBS, QuestEventType.ON_KILL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ("checkForAdena".equalsIgnoreCase(event))
		{
			try
			{
				for (L2Object object : L2World.getInstance().getVisibleObjects(npc, 500))
				{
					if (!(object instanceof L2ItemInstance))
					{
						continue;
					}
					L2ItemInstance item = (L2ItemInstance) object;
					if ((item != null) && (item.getItemId() == PcInventory.ADENA_ID))
					{
						npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(item.getX(), item.getY(), item.getZ(), 0));
						L2World.getInstance().removeVisibleObject(item, item.getWorldRegion());
						L2World.getInstance().removeObject(item);
						if (_ADENAS.containsKey(npc.getObjectId()))
						{
							_ADENAS.get(npc.getObjectId()).add(item.getCount());
							
							if (_ADENAS.get(npc.getObjectId()).size() > 9)
							{
								long totalAdena = 0;
								for (long adena : _ADENAS.get(npc.getObjectId()))
								{
									totalAdena += adena;
								}
								
								if (totalAdena < 10000000)
								{
									npc.deleteMe();
								}
								else if (totalAdena < 100000000)
								{
									int x = npc.getX();
									int y = npc.getY();
									int z = npc.getZ();
									npc.deleteMe();
									addSpawn(LUCKY_PIG_MOB_PINK, x, y, z, 0, true, 5 * 60 * 1000, true);
								}
								else if (totalAdena >= 100000000)
								{
									int x = npc.getX();
									int y = npc.getY();
									int z = npc.getZ();
									npc.deleteMe();
									addSpawn(LUCKY_PIG_MOB_YELLOW, x, y, z, 0, true, 5 * 60 * 1000, true);
								}
								cancelQuestTimer("checkForAdena", npc, null);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (Util.contains(_MOBS, npc.getNpcId()))
		{
			if (Rnd.getChance(18))
			{
				L2Npc mob = addSpawn(LUCKY_PIG_NPC, npc.getX() + 18, npc.getY() + 18, npc.getZ(), npc.getHeading(), true, 10 * 60 * 1000, true);
				onSpawn(mob);
			}
		}
		return super.onKill(npc, player, isPet);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		switch (npc.getNpcId())
		{
			case LUCKY_PIG_NPC:
			{
				List<Long> _adena = new FastList<Long>();
				_ADENAS.put(npc.getObjectId(), _adena);
				startQuestTimer("checkForAdena", 1000, npc, null, true);
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "I am hungry please give me some adenas!"));
				break;
			}
			case LUCKY_PIG_MOB_PINK:
			case LUCKY_PIG_MOB_YELLOW:
			{
				npc.setIsInvul(true);
			}
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new LuckyPig(-1, LuckyPig.class.getSimpleName(), "custom");
	}
}