package pk.elfo.gameserver.model.actor.instance;

import pk.elfo.Config;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.instancemanager.RaidBossPointsManager;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager.StatusEnum;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.entity.Hero;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.util.MinionList;
import pk.elfo.util.Rnd;

public class L2RaidBossInstance extends L2MonsterInstance
{
  private static final int RAIDBOSS_MAINTENANCE_INTERVAL = 30000;
  private RaidBossSpawnManager.StatusEnum _raidStatus;
  private boolean _useRaidCurse = true;

  public L2RaidBossInstance(int objectId, L2NpcTemplate template)
  {
    super(objectId, template);
    setInstanceType(InstanceType.L2RaidBossInstance);
    setIsRaid(true);
    setLethalable(false);
  }

  public void onSpawn()
  {
    setIsNoRndWalk(true);
    super.onSpawn();
  }

  protected int getMaintenanceInterval()
  {
    return 30000;
  }

  public boolean doDie(L2Character killer)
  {
    if (!super.doDie(killer))
    {
      return false;
    }

    L2PcInstance player = null;
    if ((killer instanceof L2PcInstance))
    {
      player = (L2PcInstance)killer;
    }
    else if ((killer instanceof L2Summon))
    {
      player = ((L2Summon)killer).getOwner();
    }

    if (player != null)
    {
      broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.RAID_WAS_SUCCESSFUL));
      if (player.getParty() != null)
      {
        for (L2PcInstance member : player.getParty().getMembers())
        {
          RaidBossPointsManager.getInstance().addPoints(member, getNpcId(), getLevel() / 2 + Rnd.get(-5, 5));
          if (member.isNoble())
          {
            Hero.getInstance().setRBkilled(member.getObjectId(), getNpcId());
          }
        }
      }
      else
      {
        RaidBossPointsManager.getInstance().addPoints(player, getNpcId(), getLevel() / 2 + Rnd.get(-5, 5));
        if (player.isNoble())
        {
          Hero.getInstance().setRBkilled(player.getObjectId(), getNpcId());
        }
      }
    }

    RaidBossSpawnManager.getInstance().updateStatus(this, true);
    return true;
  }

  protected void startMaintenanceTask()
  {
    if (getTemplate().getMinionData() != null)
    {
      getMinionList().spawnMinions();
    }

    this._maintenanceTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
    {
      public void run()
      {
        L2RaidBossInstance.this.checkAndReturnToSpawn();
      }
    }
    , 60000L, getMaintenanceInterval() + Rnd.get(5000));
  }

  protected void checkAndReturnToSpawn()
  {
    if ((isDead()) || (isMovementDisabled()) || (!canReturnToSpawnPoint()))
    {
      return;
    }

    L2Spawn spawn = getSpawn();
    if (spawn == null)
    {
      return;
    }

    int spawnX = spawn.getLocx();
    int spawnY = spawn.getLocy();
    int spawnZ = spawn.getLocz();

    if ((!isInCombat()) && (!isMovementDisabled()))
    {
      if (!isInsideRadius(spawnX, spawnY, spawnZ, Math.max(Config.MAX_DRIFT_RANGE, 200), true, false))
      {
        teleToLocation(spawnX, spawnY, spawnZ, false);
      }
    }
  }

  public void setRaidStatus(RaidBossSpawnManager.StatusEnum status)
  {
    this._raidStatus = status;
  }

  public RaidBossSpawnManager.StatusEnum getRaidStatus()
  {
    return this._raidStatus;
  }

  public float getVitalityPoints(int damage)
  {
    return -super.getVitalityPoints(damage) / 100.0F;
  }

  public boolean useVitalityRate()
  {
    return false;
  }

  public void setUseRaidCurse(boolean val)
  {
    this._useRaidCurse = val;
  }

  public boolean giveRaidCurse()
  {
    return this._useRaidCurse;
  }
}