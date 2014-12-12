package handlers.effecthandlers;

import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.model.Location;
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

public class EnemyCharge extends L2Effect
{
	static final Logger _log = Logger.getLogger(EnemyCharge.class.getName());
	
	private int _x, _y, _z;
	
	public EnemyCharge(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isMovementDisabled())
		{
			return false;
		}
		
		// Get current position of the L2Character
		final int curX = getEffector().getX();
		final int curY = getEffector().getY();
		final int curZ = getEffector().getZ();
		
		// Calculate distance (dx,dy) between current position and destination
		double dx = getEffected().getX() - curX;
		double dy = getEffected().getY() - curY;
		double dz = getEffected().getZ() - curZ;
		double distance = Math.sqrt((dx * dx) + (dy * dy));
		if (distance > 2000)
		{
			_log.info("EffectEnemyCharge ia usar coordenadas invalidas para os personagens, getEffector: " + curX + "," + curY + " e getEffected: " + getEffected().getX() + "," + getEffected().getY());
			return false;
		}
		int offset = Math.max((int) distance - getSkill().getFlyRadius(), 30);
		
		double cos;
		double sin;
		
		// approximation for moving closer when z coordinates are different
		// TODO: handle Z axis movement better
		offset -= Math.abs(dz);
		if (offset < 5)
		{
			offset = 5;
		}
		
		// If no distance
		if ((distance < 1) || ((distance - offset) <= 0))
		{
			return false;
		}
		
		// Calculate movement angles needed
		sin = dy / distance;
		cos = dx / distance;
		
		// Calculate the new destination with offset included
		_x = curX + (int) ((distance - offset) * cos);
		_y = curY + (int) ((distance - offset) * sin);
		_z = getEffected().getZ();
		
		if (Config.GEODATA > 0)
		{
			Location destiny = GeoData.getInstance().moveCheck(getEffector().getX(), getEffector().getY(), getEffector().getZ(), _x, _y, _z, getEffector().getInstanceId());
			_x = destiny.getX();
			_y = destiny.getY();
		}
		getEffector().broadcastPacket(new FlyToLocation(getEffector(), _x, _y, _z, FlyType.CHARGE));
		
		// maybe is need force set X,Y,Z
		getEffector().setXYZ(_x, _y, _z);
		getEffector().broadcastPacket(new ValidateLocation(getEffector()));
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}