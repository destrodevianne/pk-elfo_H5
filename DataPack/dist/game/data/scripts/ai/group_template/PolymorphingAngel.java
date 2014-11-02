package ai.group_template;

import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;

/**
 * Angel spawns...when one of the angels in the keys dies, the other angel will spawn.
 */
public class PolymorphingAngel extends AbstractNpcAI
{
	private static final Map<Integer, Integer> ANGELSPAWNS = new FastMap<>();
	static
	{
		ANGELSPAWNS.put(20830, 20859);
		ANGELSPAWNS.put(21067, 21068);
		ANGELSPAWNS.put(21062, 21063);
		ANGELSPAWNS.put(20831, 20860);
		ANGELSPAWNS.put(21070, 21071);
	}
	
	private PolymorphingAngel(String name, String descr)
	{
		super(name, descr);
		addKillId(ANGELSPAWNS.keySet());
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		L2Attackable newNpc = (L2Attackable) addSpawn(ANGELSPAWNS.get(npc.getNpcId()), npc);
		newNpc.setRunning();
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new PolymorphingAngel(PolymorphingAngel.class.getSimpleName(), "ai/group_template");
	}
}