package ai.npc.Teleports.Warpgate;

import pk.elfo.Config;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.zone.L2ZoneType;
import quests.Q00130_PathToHellbound.Q00130_PathToHellbound;
import quests.Q00133_ThatsBloodyHot.Q00133_ThatsBloodyHot;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Warpgate extends AbstractNpcAI
{
	// Misc
	private static final int MAP = 9994;
	private static final int ZONE = 40101;
	// Teleports
	private static final int[] WARPGATES =
	{
		32314,
		32315,
		32316,
		32317,
		32318,
		32319
	};
	
	private static final boolean canEnter(L2PcInstance player)
	{
		if (Config.HB_NEEDS_QUEST)
		{
			
			return true;
		}
		
		if (player.isFlying())
		{
			return false;
		}
		
		QuestState st;
		if (!HellboundManager.getInstance().isLocked())
		{
			st = player.getQuestState(Q00130_PathToHellbound.class.getSimpleName());
			if ((st != null) && st.isCompleted())
			{
				return true;
			}
		}
		st = player.getQuestState(Q00133_ThatsBloodyHot.class.getSimpleName());
		return ((st != null) && st.isCompleted());
	}
	
	@Override
	public final String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (!canEnter(player))
		{
			if (HellboundManager.getInstance().isLocked())
			{
				return "warpgate-locked.htm";
			}
		}
		return npc.getNpcId() + ".htm";
	}
	
	@Override
	public final String onTalk(L2Npc npc, L2PcInstance player)
	{
		if (!canEnter(player))
		{
			return "warpgate-no.htm";
		}
		player.teleToLocation(-11272, 236464, -3248, true);
		HellboundManager.getInstance().unlock();
		return null;
	}
	
	@Override
	public final String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (character.isPlayer())
		{
			if (!canEnter(character.getActingPlayer()) && !character.canOverrideCond(PcCondOverride.ZONE_CONDITIONS))
			{
				ThreadPoolManager.getInstance().scheduleGeneral(new Teleport(character), 1000);
			}
			else if (!character.getActingPlayer().isMinimapAllowed())
			{
				if (character.getInventory().getItemByItemId(MAP) != null)
				{
					character.getActingPlayer().setMinimapAllowed(true);
				}
			}
		}
		return null;
	}
	
	private static final class Teleport implements Runnable
	{
		private final L2Character _char;
		
		public Teleport(L2Character c)
		{
			_char = c;
		}
		
		@Override
		public void run()
		{
			try
			{
				_char.teleToLocation(-16555, 209375, -3670, true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private Warpgate(String name, String descr)
	{
		super(name, descr);
		addStartNpc(WARPGATES);
		addFirstTalkId(WARPGATES);
		addTalkId(WARPGATES);
		addEnterZoneId(ZONE);
	}
	
	public static void main(String[] args)
	{
		new Warpgate(Warpgate.class.getSimpleName(), "ai/npc/Teleports/");
	}
}