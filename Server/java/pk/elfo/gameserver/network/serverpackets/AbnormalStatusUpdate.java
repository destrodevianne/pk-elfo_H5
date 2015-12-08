package pk.elfo.gameserver.network.serverpackets;

import java.util.List;

import javolution.util.FastList;

public class AbnormalStatusUpdate extends L2GameServerPacket
{
	private final List<Effect> _effects;
	
	private static class Effect
	{
		protected int _skillId;
		protected int _level;
		protected int _duration;
		
		public Effect(int pSkillId, int pLevel, int pDuration)
		{
			_skillId = pSkillId;
			_level = pLevel;
			_duration = pDuration;
		}
	}
	
	public AbnormalStatusUpdate()
	{
		_effects = new FastList<>();
	}
	
	/**
	 * @param skillId
	 * @param level
	 * @param duration
	 */
	public void addEffect(int skillId, int level, int duration)
	{
		if ((skillId == 2031) || (skillId == 2032) || (skillId == 2037) || (skillId == 26025) || (skillId == 26026))
		{
			return;
		}
		_effects.add(new Effect(skillId, level, duration));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x85);
		
		writeH(_effects.size());
		
		for (Effect temp : _effects)
		{
			writeD(temp._skillId);
			writeH(temp._level);
			
			if (temp._duration == -1)
			{
				writeD(-1);
			}
			else
			{
				writeD(temp._duration / 1000);
			}
		}
	}
}
