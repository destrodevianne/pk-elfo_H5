package ai.individual;

import java.util.Iterator;
import java.util.Map;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.util.Util;
import ai.npc.AbstractNpcAI;
import javolution.util.FastMap;

public class SelfExplosiveKamikaze extends AbstractNpcAI
{
	private static final Map<Integer, SkillHolder> MONSTERS = new FastMap<>();
	public SelfExplosiveKamikaze(String name, String descr)
		{
			super(name, descr);
			for (Iterator<Integer> i$ = MONSTERS.keySet().iterator(); i$.hasNext(); ) { int npcId = i$.next().intValue();
			addAttackId(npcId);
			addSpellFinishedId(npcId);
		}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skil)
	{
		if (player != null)
		{
			if ((MONSTERS.containsKey(Integer.valueOf(npc.getNpcId()))) && (!npc.isDead()) && (Util.checkIfInRange(MONSTERS.get(Integer.valueOf(npc.getNpcId())).getSkill().getSkillRadius(), player, npc, true)))
			npc.doCast(MONSTERS.get(Integer.valueOf(npc.getNpcId())).getSkill());
		}
		return super.onAttack(npc, player, damage, isPet, skil);
	}

	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, L2Skill skill)
	{
		{
			if ((MONSTERS.containsKey(Integer.valueOf(npc.getNpcId()))) && (!npc.isDead()) && ((skill.getId() == 4614) || (skill.getId() == 5376))) {
				npc.doDie(null);
		}
		return super.onSpellFinished(npc, player, skill);
	}
			static
		{
			MONSTERS.put(Integer.valueOf(18817), new SkillHolder(5376, 4));
			MONSTERS.put(Integer.valueOf(18818), new SkillHolder(5376, 4));
			MONSTERS.put(Integer.valueOf(18821), new SkillHolder(5376, 5));
			MONSTERS.put(Integer.valueOf(21666), new SkillHolder(4614, 3));
			MONSTERS.put(Integer.valueOf(21689), new SkillHolder(4614, 4));
			MONSTERS.put(Integer.valueOf(21712), new SkillHolder(4614, 5));
			MONSTERS.put(Integer.valueOf(21735), new SkillHolder(4614, 6));
			MONSTERS.put(Integer.valueOf(21758), new SkillHolder(4614, 7));
			MONSTERS.put(Integer.valueOf(21781), new SkillHolder(4614, 9));
		}

	public static void main(String[] args)
	{
		new SelfExplosiveKamikaze(SelfExplosiveKamikaze.class.getSimpleName(), "ai");
	}
}