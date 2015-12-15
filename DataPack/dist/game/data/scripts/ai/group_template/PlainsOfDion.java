package ai.group_template;

import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.util.Util;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public final class PlainsOfDion extends AbstractNpcAI
{
	private static final int MONSTERS[] =
	{
		21104, // Delu Lizardman Supplier
		21105, // Delu Lizardman Special Agent
		21107, // Delu Lizardman Commander
	};
	
	private static final NpcStringId[] MONSTERS_MSG =
	{
		NpcStringId.S1_HOW_DARE_YOU_INTERRUPT_OUR_FIGHT_HEY_GUYS_HELP,
		NpcStringId.S1_HEY_WERE_HAVING_A_DUEL_HERE,
		NpcStringId.THE_DUEL_IS_OVER_ATTACK,
		NpcStringId.FOUL_KILL_THE_COWARD,
		NpcStringId.HOW_DARE_YOU_INTERRUPT_A_SACRED_DUEL_YOU_MUST_BE_TAUGHT_A_LESSON
	};
	
	private static final NpcStringId[] MONSTERS_ASSIST_MSG =
	{
		NpcStringId.DIE_YOU_COWARD,
		NpcStringId.KILL_THE_COWARD,
		NpcStringId.WHAT_ARE_YOU_LOOKING_AT
	};
	
	private PlainsOfDion(String name, String descr)
	{
		super(name, descr);
		addAttackId(MONSTERS);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isSummon)
	{
		if (npc.isScriptValue(0))
		{
			int i = getRandom(5);
			if (i < 2)
			{
				broadcastNpcSay(npc, Say2.NPC_ALL, MONSTERS_MSG[i], player.getName());
			}
			else
			{
				broadcastNpcSay(npc, Say2.NPC_ALL, MONSTERS_MSG[i]);
			}
			
			for (L2Character obj : npc.getKnownList().getKnownCharactersInRadius(npc.getFactionRange()))
			{
				if (obj.isMonster() && Util.contains(MONSTERS, ((L2MonsterInstance) obj).getNpcId()) && !obj.isAttackingNow() && !obj.isDead() && GeoData.getInstance().canSeeTarget(npc, obj))
				{
					final L2MonsterInstance monster = (L2MonsterInstance) obj;
					attackPlayer(monster, player);
					broadcastNpcSay(monster, Say2.NPC_ALL, MONSTERS_ASSIST_MSG[getRandom(3)]);
				}
			}
			npc.setScriptValue(1);
		}
		return super.onAttack(npc, player, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new PlainsOfDion(PlainsOfDion.class.getSimpleName(), "ai");
	}
}