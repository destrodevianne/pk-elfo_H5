package pk.elfo.gameserver;

import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Point3D;

public class GeoData
{
	private static final Logger _log = Logger.getLogger(GeoData.class.getName());
	
	protected GeoData()
	{
		//
	}
	
	/**
	 * Instantiates a new geodata.
	 * @param disabled the disabled
	 */
	protected GeoData(final boolean disabled)
	{
		if (disabled)
		{
			_log.info("Geodata Engine: desativado.");
		}
	}
	
	/**
	 * Gets the type.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the geodata block type
	 */
	public short getType(int x, int y)
	{
		return 0;
	}
	
	/**
	 * Gets the height.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the height
	 */
	public short getHeight(int x, int y, int z)
	{
		return (short) z;
	}
	
	/**
	 * Gets the spawn height.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param zmin the minimum z coordinate
	 * @param zmax the the maximum z coordinate
	 * @param spawn the spawn
	 * @return the spawn height
	 */
	public short getSpawnHeight(int x, int y, int zmin, int zmax, L2Spawn spawn)
	{
		return (short) zmin;
	}
	
	/**
	 * Geodata position.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the string
	 */
	public String geoPosition(int x, int y)
	{
		return "";
	}
	
	/**
	 * Can see target.
	 * @param cha the character
	 * @param target the target
	 * @return {@code true} if the character can see the target (LOS), {@code false} otherwise
	 */
	public boolean canSeeTarget(L2Object cha, L2Object target)
	{
		// If geodata is off do simple check :]
		// Don't allow casting on players on different dungeon levels etc
		return (Math.abs(target.getZ() - cha.getZ()) < 1000);
	}
	
	/**
	 * Can see target.
	 * @param cha the character
	 * @param worldPosition the world position
	 * @return {@code true} if the character can see the target at the given world position, {@code false} otherwise
	 */
	public boolean canSeeTarget(L2Object cha, Point3D worldPosition)
	{
		// If geodata is off do simple check :]
		// Don't allow casting on players on different dungeon levels etc
		return Math.abs(worldPosition.getZ() - cha.getZ()) < 1000;
	}
	
	/**
	 * Can see target.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param tx the target's x coordinate
	 * @param ty the target's y coordinate
	 * @param tz the target's z coordinate
	 * @return {@code true} if there is line of sight between the given coordinate sets, {@code false} otherwise
	 */
	public boolean canSeeTarget(int x, int y, int z, int tx, int ty, int tz)
	{
		// If geodata is off do simple check :]
		// Don't allow casting on players on different dungeon levels etc
		return (Math.abs(z - tz) < 1000);
	}
	
	/**
	 * Can see target debug.
	 * @param gm the Game Master
	 * @param target the target
	 * @return {@code true} if the Game Master can see the target target (LOS), {@code false} otherwise
	 */
	public boolean canSeeTargetDebug(L2PcInstance gm, L2Object target)
	{
		return true;
	}
	
	/**
	 * Gets the NSWE.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the geodata NSWE (0-15)
	 */
	public short getNSWE(int x, int y, int z)
	{
		return 15;
	}
	
	/**
	 * Gets the height and NSWE.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return the height and NSWE
	 */
	public short getHeightAndNSWE(int x, int y, int z)
	{
		return (short) ((z << 1) | 15);
	}
	
	/**
	 * Move check.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param tx the target's x coordinate
	 * @param ty the target's y coordinate
	 * @param tz the target's z coordinate
	 * @param instanceId the instance id
	 * @return the last Location (x,y,z) where player can walk - just before wall
	 */
	public Location moveCheck(int x, int y, int z, int tx, int ty, int tz, int instanceId)
	{
		return new Location(tx, ty, tz);
	}
	
	/**
	 * Can move from to target.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param tx the target's x coordinate
	 * @param ty the target's y coordinate
	 * @param tz the target's z coordinate
	 * @param instanceId the instance id
	 * @return {@code true} if the character at x,y,z can move to tx,ty,tz, {@code false} otherwise
	 */
	public boolean canMoveFromToTarget(int x, int y, int z, int tx, int ty, int tz, int instanceId)
	{
		return true;
	}
	
	/**
	 * Adds the geodata data bug.
	 * @param gm the Game Master
	 * @param comment the comment
	 */
	public void addGeoDataBug(L2PcInstance gm, String comment)
	{
		// Do Nothing
	}
	
	/**
	 * Unload geodata.
	 * @param rx the region x coordinate
	 * @param ry the region y coordinate
	 */
	public static void unloadGeodata(byte rx, byte ry)
	{
		
	}
	
	/**
	 * Load a geodata file.
	 * @param rx the region x coordinate
	 * @param ry the region y coordinate
	 * @return {@code true} if successful, {@code false} otherwise
	 */
	public static boolean loadGeodataFile(byte rx, byte ry)
	{
		return false;
	}
	
	/**
	 * Checks for geodata.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return {@code true} if there is geodata for the given coordinates, {@code false} otherwise
	 */
	public boolean hasGeo(int x, int y)
	{
		return false;
	}
	
	/**
	 * Gets the single instance of GeoData.
	 * @return single instance of GeoData
	 */
	public static GeoData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final GeoData _instance = Config.GEODATA > 0 ? GeoEngine.getInstance() : new GeoData(true);
	}
}