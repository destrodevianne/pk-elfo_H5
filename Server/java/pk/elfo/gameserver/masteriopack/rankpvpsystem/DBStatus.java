package pk.elfo.gameserver.masteriopack.rankpvpsystem;

/**
 * This class contains all possible states for database updater.
 * PkElfo
 */
public class DBStatus 
{
	/**
	 * The NONE status mean the object is not changed in model and the update of the database is not required.
	 */
	public static final byte NONE = 0;
	/**
	 * The INSERTED status mean the object is added to model and the object data need to be insert into database.
	 */
	public static final byte INSERTED = 1;
	/**
	 * The UPDATED status mean the object is updated in model and the update of the database is required.
	 */
	public static final byte UPDATED = 2;
	/**
	 * The DELETED status mean the object is deleted from model and the object data need to be delete from database.
	 */
	//public static final byte DELETED = 3;	// UNSUSED
}
