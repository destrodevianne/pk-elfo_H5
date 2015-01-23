package pk.elfo.gameserver.model;

import pk.elfo.gameserver.model.actor.L2Character;

public final class Location
{
	public final int _x;
	public final int _y;
	public final int _z;
	public int _heading;
	private int _instanceId;
	
	public Location(int x, int y, int z)
	{
		_x = x;
		_y = y;
		_z = z;
		_instanceId = -1;
	}
	
	public Location(int x, int y, int z, int heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
		_instanceId = -1;
	}
	
	public Location(int x, int y, int z, int heading, int instanceId)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
		_instanceId = instanceId;
	}
	
	public Location(L2Object obj)
	{
		_x = obj.getX();
		_y = obj.getY();
		_z = obj.getZ();
		_instanceId = obj.getInstanceId();
	}
	
	public Location(L2Character obj)
	{
		_x = obj.getX();
		_y = obj.getY();
		_z = obj.getZ();
		_heading = obj.getHeading();
		_instanceId = obj.getInstanceId();
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	public int getHeading()
	{
		return _heading;
	}
	
	public int getInstanceId()
	{
		return _instanceId;
	}
	
	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "] X: " + _x + " Y: " + _y + " Z: " + _z + " Heading: " + _heading + " InstanceId: " + _instanceId;
	}
	
	/**
	 * @param instanceId the instance Id to set
	 */
	public void setInstanceId(int instanceId)
	{
		_instanceId = instanceId;
	}
}