package pk.elfo.gameserver.model.entity;

import java.util.Calendar;
import java.util.List;

import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.L2SiegeClan;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public interface Siegable
{
	public void startSiege();
	
	public void endSiege();
	
	public L2SiegeClan getAttackerClan(int clanId);
	
	public L2SiegeClan getAttackerClan(L2Clan clan);
	
	public List<L2SiegeClan> getAttackerClans();
	
	public List<L2PcInstance> getAttackersInZone();
	
	public boolean checkIsAttacker(L2Clan clan);
	
	public L2SiegeClan getDefenderClan(int clanId);
	
	public L2SiegeClan getDefenderClan(L2Clan clan);
	
	public List<L2SiegeClan> getDefenderClans();
	
	public boolean checkIsDefender(L2Clan clan);
	
	public List<L2Npc> getFlag(L2Clan clan);
	
	public Calendar getSiegeDate();
	
	public boolean giveFame();
	
	public int getFameFrequency();
	
	public int getFameAmount();
	
	public void updateSiege();
}