/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.model.skills.l2skills;

import king.server.gameserver.datatables.NpcTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2EffectPointInstance;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.targets.L2TargetType;
import king.server.gameserver.util.Point3D;

/**
 * @author Forsaiken
 */
public final class L2SkillSignet extends L2Skill
{
	public L2SkillSignet(StatsSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(L2Character caster, L2Object[] targets)
	{
		if (caster.isAlikeDead())
		{
			return;
		}
		
		L2NpcTemplate template = NpcTable.getInstance().getTemplate(getNpcId());
		L2EffectPointInstance effectPoint = new L2EffectPointInstance(IdFactory.getInstance().getNextId(), template, caster);
		effectPoint.setCurrentHp(effectPoint.getMaxHp());
		effectPoint.setCurrentMp(effectPoint.getMaxMp());
		
		int x = caster.getX();
		int y = caster.getY();
		int z = caster.getZ();
		
		if (caster.isPlayer() && (getTargetType() == L2TargetType.TARGET_GROUND))
		{
			Point3D wordPosition = caster.getActingPlayer().getCurrentSkillWorldPosition();
			
			if (wordPosition != null)
			{
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}
		getEffects(caster, effectPoint);
		
		effectPoint.setIsInvul(true);
		effectPoint.spawnMe(x, y, z);
	}
}
