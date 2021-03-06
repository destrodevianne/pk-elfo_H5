package pk.elfo.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import pk.elfo.Config;
import pk.elfo.gameserver.model.L2SkillLearn;
import pk.elfo.gameserver.model.base.AcquireSkillType;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.model.skills.L2Skill;

public class AcquireSkillInfo extends L2GameServerPacket
{
	private final AcquireSkillType _type;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final List<Req> _reqs;
	
	/**
	 * Private class containing learning skill requisites.
	 */
	private static class Req
	{
		public int itemId;
		public long count;
		public int type;
		public int unk;
		
		/**
		 * @param pType TODO identify.
		 * @param pItemId the item Id.
		 * @param itemCount the item count.
		 * @param pUnk TODO identify.
		 */
		public Req(int pType, int pItemId, long itemCount, int pUnk)
		{
			itemId = pItemId;
			type = pType;
			count = itemCount;
			unk = pUnk;
		}
	}
	
	/**
	 * Constructor for the acquire skill info object.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 */
	public AcquireSkillInfo(AcquireSkillType skillType, L2SkillLearn skillLearn)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = skillLearn.getLevelUpSp();
		_type = skillType;
		_reqs = new ArrayList<>();
		if ((skillType != AcquireSkillType.Pledge) || Config.LIFE_CRYSTAL_NEEDED)
		{
			for (ItemHolder item : skillLearn.getRequiredItems())
			{
				if (!Config.DIVINE_SP_BOOK_NEEDED && (_id == L2Skill.SKILL_DIVINE_INSPIRATION))
				{
					continue;
				}
				_reqs.add(new Req(99, item.getId(), item.getCount(), 50));
			}
		}
	}
	
	/**
	 * Special constructor for Alternate Skill Learning system.<br>
	 * Sets a custom amount of SP.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 * @param sp the custom SP amount.
	 */
	public AcquireSkillInfo(AcquireSkillType skillType, L2SkillLearn skillLearn, int sp)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = sp;
		_type = skillType;
		_reqs = new ArrayList<>();
		for (ItemHolder item : skillLearn.getRequiredItems())
		{
			_reqs.add(new Req(99, item.getId(), item.getCount(), 50));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x91);
		writeD(_id);
		writeD(_level);
		writeD(_spCost);
		writeD(_type.ordinal());
		writeD(_reqs.size());
		for (Req temp : _reqs)
		{
			writeD(temp.type);
			writeD(temp.itemId);
			writeQ(temp.count);
			writeD(temp.unk);
		}
	}
}