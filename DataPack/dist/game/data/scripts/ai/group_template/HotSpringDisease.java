package ai.group_template;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class HotSpringDisease extends AbstractNpcAI
{
	static final int[] disease1mobs = { 21314, 21316, 21317, 21319, 21321, 21322 }; // Monsters which cast Hot Spring Malaria (4554)
	static final int[] disease2mobs = { 21317, 21322 }; // Monsters which cast Hot Springs Flu (4553)
	static final int[] disease3mobs = { 21316, 21319 }; // Monsters which cast Hot Springs Cholera (4552)
	static final int[] disease4mobs = { 21314, 21321 }; // Monsters which cast Hot Springs Rheumatism (4551)
	// Chance to get infected by disease
	private static final int DISEASE_CHANCE = 5;

	public HotSpringDisease(String name, String descr)
	{
		super(name, descr);
		registerMobs(disease1mobs);
		registerMobs(disease2mobs);
		registerMobs(disease3mobs);
		registerMobs(disease4mobs);
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (Util.contains(disease1mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4554, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease2mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4553, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease3mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4552, Rnd.get(10) + 1));
			}
		}
		if (Util.contains(disease4mobs, npc.getNpcId()))
		{
			if (Rnd.get(100) < DISEASE_CHANCE)
			{
				npc.setTarget(attacker);
				npc.doCast(SkillTable.getInstance().getInfo(4551, Rnd.get(10) + 1));
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}

	public static void main(String[] args)
	{
		new HotSpringDisease(HotSpringDisease.class.getSimpleName(), "ai/group_template");
	}
}