package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.datatables.EnchantGroupsData;
import pk.elfo.gameserver.model.L2EnchantSkillLearn;
import pk.elfo.gameserver.model.L2EnchantSkillGroup.EnchantSkillHolder;
import javolution.util.FastList;

public final class ExEnchantSkillInfo extends L2GameServerPacket
{
	private final FastList<Integer> _routes; // skill lvls for each route
	
	private final int _id;
	private final int _lvl;
	private boolean _maxEnchanted = false;
	
	public ExEnchantSkillInfo(int id, int lvl)
	{
		_routes = new FastList<>();
		_id = id;
		_lvl = lvl;
		
		L2EnchantSkillLearn enchantLearn = EnchantGroupsData.getInstance().getSkillEnchantmentBySkillId(_id);
		// do we have this skill?
		if (enchantLearn != null)
		{
			// skill already enchanted?
			if (_lvl > 100)
			{
				_maxEnchanted = enchantLearn.isMaxEnchant(_lvl);
				
				// get detail for next level
				EnchantSkillHolder esd = enchantLearn.getEnchantSkillHolder(_lvl);
				
				// if it exists add it
				if (esd != null)
				{
					_routes.add(_lvl); // current enchant add firts
				}
				
				int skillLvL = (_lvl % 100);
				
				for (int route : enchantLearn.getAllRoutes())
				{
					if (((route * 100) + skillLvL) == _lvl)
					{
						continue;
					}
					// add other levels of all routes - same lvl as enchanted
					// lvl
					_routes.add((route * 100) + skillLvL);
				}
			}
			else
			// not already enchanted
			{
				for (int route : enchantLearn.getAllRoutes())
				{
					// add first level (+1) of all routes
					_routes.add((route * 100) + 1);
				}
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2a);
		writeD(_id);
		writeD(_lvl);
		writeD(_maxEnchanted ? 0 : 1);
		writeD(_lvl > 100 ? 1 : 0); // enchanted?
		writeD(_routes.size());
		
		for (int level : _routes)
		{
			writeD(level);
		}
	}
}