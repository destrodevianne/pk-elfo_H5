package ai.group_template;

import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;

public final class FrozenLabyrinth extends AbstractNpcAI
{
	// Monsters
	private static final int PRONGHORN_SPIRIT = 22087;
	private static final int PRONGHORN = 22088;
	private static final int LOST_BUFFALO = 22093;
	private static final int FROST_BUFFALO = 22094;
	
	private FrozenLabyrinth(String name, String descr)
	{
		super(name, descr);
		addAttackId(PRONGHORN, FROST_BUFFALO);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, L2Skill skill)
	{
		if ((skill != null) && !skill.isMagic())
		{
			int spawnId = LOST_BUFFALO;
			if (npc.getNpcId() == PRONGHORN)
			{
				spawnId = PRONGHORN_SPIRIT;
			}
			
			int diff = 0;
			for (int i = 0; i < 6; i++)
			{
				int x = diff < 60 ? npc.getX() + diff : npc.getX();
				int y = diff >= 60 ? npc.getY() + (diff - 40) : npc.getY();
				
				final L2Attackable monster = (L2Attackable) addSpawn(spawnId, x, y, npc.getZ(), npc.getHeading(), false, 0);
				attackPlayer(monster, attacker);
				diff += 20;
			}
			npc.deleteMe();
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	public static void main(String[] args)
	{
		new FrozenLabyrinth(FrozenLabyrinth.class.getSimpleName(), "ai/group_template");
	}
}