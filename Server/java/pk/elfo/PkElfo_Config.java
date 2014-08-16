package pk.elfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import pk.elfo.util.L2Properties;

/**
 * PkElfo
 */

public final class PkElfo_Config
{
	private static final Logger _log = Logger.getLogger(PkElfo_Config.class.getName());
	
	// ----------------------------------------------------------------------------------------------------//
	// Lineage II PkElfo arquivos de definicoes de propriedades 										   //
	// ----------------------------------------------------------------------------------------------------//
	private static final String RANDOM_FIGHT_FILE = "./config/Eventos/RandomFight.properties";

	//======================================================================================//
	//                               Random Fight Event										//
	//======================================================================================//
	public static boolean ALLOW_RANDOM_FIGHT;
	public static int EVERY_MINUTES;
	public static int RANDOM_FIGHT_REWARD_ID;
	public static int RANDOM_FIGHT_REWARD_COUNT;
	
	public static void load()
	{
		InputStream is = null;
		try
		{
			try
			{
				L2Properties rfSettings = new L2Properties();
				is = new FileInputStream(new File(RANDOM_FIGHT_FILE));
				rfSettings.load(is);
				
				// Random Fight Event
				ALLOW_RANDOM_FIGHT = Boolean.parseBoolean(rfSettings.getProperty("AllowRandomFight", "True"));
				EVERY_MINUTES = Integer.parseInt(rfSettings.getProperty("EveryMinutes", "3"));
				RANDOM_FIGHT_REWARD_ID = Integer.parseInt(rfSettings.getProperty("RewardId", "8762"));
				RANDOM_FIGHT_REWARD_COUNT = Integer.parseInt(rfSettings.getProperty("RewardCount", "5"));
			}
			catch (Exception e)
			{
				_log.warning("CustomConfig.load(): Nao foi possivel carregar as configuracoes do arquivo PkElfo. Motivo:");
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			_log.warning("CustomConfig.load(): Problemas durante a inicializacao. Motivo:");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
}