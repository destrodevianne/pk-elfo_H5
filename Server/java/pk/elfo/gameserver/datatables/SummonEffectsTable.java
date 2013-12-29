package pk.elfo.gameserver.datatables;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;

public class SummonEffectsTable
{
	/** Servitors **/
	// Map tree
	// -> key: charObjectId, value: classIndex Map
	// --> key: classIndex, value: servitors Map
	// ---> key: servitorSkillId, value: Effects list
	private final TIntObjectHashMap<TIntObjectHashMap<TIntObjectHashMap<List<SummonEffect>>>> _servitorEffects = new TIntObjectHashMap<>();
	
	public TIntObjectHashMap<TIntObjectHashMap<TIntObjectHashMap<List<SummonEffect>>>> getServitorEffectsOwner()
	{
		return _servitorEffects;
	}
	
	public TIntObjectHashMap<List<SummonEffect>> getServitorEffects(L2PcInstance owner)
	{
		final TIntObjectHashMap<TIntObjectHashMap<List<SummonEffect>>> servitorMap = _servitorEffects.get(owner.getObjectId());
		if (servitorMap == null)
		{
			return null;
		}
		return servitorMap.get(owner.getClassIndex());
	}
	
	/** Pets **/
	private final TIntObjectHashMap<List<SummonEffect>> _petEffects = new TIntObjectHashMap<>(); // key: petItemObjectId, value: Effects list
	
	public TIntObjectHashMap<List<SummonEffect>> getPetEffects()
	{
		return _petEffects;
	}
	
	public class SummonEffect
	{
		L2Skill _skill;
		int _effectCount;
		int _effectCurTime;
		
		public SummonEffect(L2Skill skill, int effectCount, int effectCurTime)
		{
			_skill = skill;
			_effectCount = effectCount;
			_effectCurTime = effectCurTime;
		}
		
		public L2Skill getSkill()
		{
			return _skill;
		}
		
		public int getEffectCount()
		{
			return _effectCount;
		}
		
		public int getEffectCurTime()
		{
			return _effectCurTime;
		}
	}
	
	/**
	 * @return
	 */
	public static SummonEffectsTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final SummonEffectsTable _instance = new SummonEffectsTable();
	}
}