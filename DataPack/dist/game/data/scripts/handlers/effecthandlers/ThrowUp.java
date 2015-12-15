package handlers.effecthandlers;

import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.serverpackets.FlyToLocation;
import pk.elfo.gameserver.network.serverpackets.ValidateLocation;
import pk.elfo.gameserver.network.serverpackets.FlyToLocation.FlyType;

/**
 * Projeto PkElfo
 */
 
public class ThrowUp extends L2Effect
{
	private static final Logger _log = Logger.getLogger(ThrowUp.class.getName());
	
	private int _x, _y, _z;
	
	public ThrowUp(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.THROW_UP;
	}
	
	@Override
	public boolean onStart()
	{
		// Get current position of the L2Character
		final int curX = getEffected().getX();
		final int curY = getEffected().getY();
		final int curZ = getEffected().getZ();
		
		// Calculate distance between effector and effected current position
		double dx = getEffector().getX() - curX;
		double dy = getEffector().getY() - curY;
		double dz = getEffector().getZ() - curZ;
		double distance = Math.sqrt((dx * dx) + (dy * dy));
		if (distance > 2000)
		{
			_log.info("EffectThrow ia usar coordenadas invalidas para os personagens, getEffected: " + curX + "," + curY + " e getEffector: " + getEffector().getX() + "," + getEffector().getY());
			return false;
		}
		int offset = Math.min((int) distance + getSkill().getFlyRadius(), 1400);
		
		double cos;
		double sin;
		
		// approximation for moving futher when z coordinates are different
		// TODO: handle Z axis movement better
		offset += Math.abs(dz);
		if (offset < 5)
		{
			offset = 5;
		}
		
		// If no distance
		if (distance < 1)
		{
			return false;
		}
		
		// Calculate movement angles needed
		sin = dy / distance;
		cos = dx / distance;
		
		// Calculate the new destination with offset included
		_x = getEffector().getX() - (int) (offset * cos);
		_y = getEffector().getY() - (int) (offset * sin);
		_z = getEffected().getZ();
		
		if (Config.GEODATA > 0)
		{
			Location destiny = GeoData.getInstance().moveCheck(getEffected().getX(), getEffected().getY(), getEffected().getZ(), _x, _y, _z, getEffected().getInstanceId());
			_x = destiny.getX();
			_y = destiny.getY();
		}
		getEffected().startStunning();
		getEffected().broadcastPacket(new FlyToLocation(getEffected(), _x, _y, _z, FlyType.THROW_UP));
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopStunning(false);
		getEffected().setXYZ(_x, _y, _z);
		getEffected().broadcastPacket(new ValidateLocation(getEffected()));
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.STUNNED.getMask();
	}
}