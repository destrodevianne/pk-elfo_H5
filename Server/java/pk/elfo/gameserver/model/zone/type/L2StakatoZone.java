package pk.elfo.gameserver.model.zone.type;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.L2Skill;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.zone.L2ZoneType;

import java.util.Collection;

/**
 * A stakato zone
 *
 * @author  certes
 */
public class L2StakatoZone extends L2ZoneType
{
	private int QUEEN_SHYEED = 25671;
	private RaidBossSpawnManager.StatusEnum _raidStatus;
	private int TIME = 63000;
   
	public L2StakatoZone(int id)
	{
		super(id);
		setTargetType(InstanceType.L2Character);
		ThreadPoolManager.getInstance().scheduleGeneral(new ApplySkill(this), TIME);
	}
   
	@Override
	public void setParameter(String name, String value)
	{
		super.setParameter(name, value);
	}
   
	@Override
	protected void onEnter(L2Character character)
	{
	}
   
	@Override
	protected void onExit(L2Character character)
	{
	}
   
	protected Collection<L2Character> getCharacterList()
	{
		return _characterList.values();
	}

	class ApplySkill implements Runnable
	{
		private final L2StakatoZone skillZone;
		private int ID_BUFF       = 6171;
		private int ID_DEBUFF     = 6169;
		private int ID_BUFFMOB    = 6170;
      
		ApplySkill(L2StakatoZone zone)
		{
			skillZone = zone;
		}
      
		@SuppressWarnings("static-access")
		public void run()
		{
			int skillId;
			int skillId2;
			if(RaidBossSpawnManager.getInstance().getRaidBossStatusId(QUEEN_SHYEED) == _raidStatus.DEAD)
			{
				skillId = ID_BUFF;
				for (L2Character temp : L2StakatoZone.this.getCharactersInside().values())
				{
					if (temp != null && !temp.isDead())
					{
						if (temp instanceof L2Playable)
						{
							if (temp.getFirstEffect(skillId) == null)
								getSkill(skillId, 1).getEffects(temp, temp);
						}
					}
				}
			}
			else
			{
				skillId = ID_DEBUFF;
				skillId2 = ID_BUFFMOB;
				for (L2Character temp : L2StakatoZone.this.getCharactersInside().values())
				{
					if (temp != null && !temp.isDead())
					{
						if (temp instanceof L2Playable)
						{
							if (temp.getFirstEffect(skillId) == null)
								getSkill(skillId, 1).getEffects(temp, temp);
						}
						else if (temp instanceof L2Npc)
						{
							if (temp.getFirstEffect(skillId2) == null)
								getSkill(skillId2, 1).getEffects(temp, temp);
						}
					}
				}
			}
			ThreadPoolManager.getInstance().scheduleGeneral(new ApplySkill(skillZone), TIME);
		}
	}
   
	private L2Skill getSkill(int skillId, int skillLvl)
	{
		return SkillTable.getInstance().getInfo(skillId, skillLvl);
	}
   
	@Override
	public void onDieInside(L2Character character)
	{
	}
   
	@Override
	public void onReviveInside(L2Character character)
	{
	}
}