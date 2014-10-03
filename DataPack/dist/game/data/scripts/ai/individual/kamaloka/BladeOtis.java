package ai.individual.kamaloka;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
import javolution.util.FastMap;

public class BladeOtis extends AbstractNpcAI
{
	private static final int BLADEO = 18562;
	private static final int GUARD = 18563;
	private Map<Integer,Integer> _guardSpawns = new HashMap<>();
	private FastMap<L2Npc, L2Npc> _guardMaster = new FastMap<>();
	public BladeOtis(String name, String descr)
	{
		super(name, descr);
		addAttackId(BLADEO);
		addKillId(BLADEO);
		addKillId(GUARD);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int objId = npc.getObjectId();
		int x = player.getX();
		int y = player.getY();
		if ((_guardSpawns.get(objId) != null) && (_guardSpawns.get(objId) >= 6))
			(
					return null;
			)
		if (event.equalsIgnoreCase("time_to_spawn"))
		{
			L2Npc guard = addSpawn(GUARD, x+Rnd.get(-20,50), y+Rnd.get(-20,50), npc.getZ(), 0, false, 0, false, npc.getInstanceId());
			if (_guardSpawns.get(objId) != null)
				(
						_guardSpawns.put(objId, _guardSpawns.get(objId)+1);
				)
			else
				(
						_guardSpawns.put(objId, 1);
				)
			_guardMaster.put(guard, npc);
			guard.setTarget(player);
			((L2Attackable)npc).addDamageHate(player,0,999);
		}
		return super.onAdvEvent(event, npc, player);
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		int objId = npc.getObjectId();
		int maxHp = npc.getMaxHp();
		double nowHp = npc.getStatus().getCurrentHp();
		if (npcId == BLADEO)
			(
					if (nowHp < maxHp*0.5)
					{
						if (_guardSpawns.get(objId) == null || _guardSpawns.get(objId) == 0)
							(
									this.startQuestTimer("time_to_spawn", 1, npc, player);
							)
						else if (_guardSpawns.get(objId) < 6)
							(
									this.startQuestTimer("time_to_spawn", 10000, npc, player);
							)
					)
			}
		return super.onAttack(npc, player, damage, isPet);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		int objId = npc.getObjectId();
		if (npcId == GUARD)
		{
			if (_guardMaster.get(npc) != null)
			{
				L2Npc master = _guardMaster.get(npc);
				if ((_guardSpawns.get(master.getObjectId()) != null) && (_guardSpawns.get(master.getObjectId()) > 0))
					(
							_guardSpawns.put(master.getObjectId(), _guardSpawns.get(master.getObjectId())-1);
					)
				_guardMaster.remove(npc);
			}
		}
		else if (npcId == BLADEO)
		{
			if (_guardSpawns.containsKey(objId))
				(
					_guardSpawns.remove(objId);
				)
			for (L2Npc i : _guardMaster.keySet())
			{
				if ((_guardMaster.get(i) != null) && (npc == _guardMaster.get(i)))
				{
					i.decayMe();
					_guardMaster.remove(i);
				}
			}
			this.cancelQuestTimer("time_to_spawn", npc, killer);
		}
		return super.onKill(npc, killer, isPet);
	}

	public static void main(String[] args)
	{
		new BladeOtis(BladeOtis.class.getSimpleName(), "ai");
	}
}