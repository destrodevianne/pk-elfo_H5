package pk.elfo.gameserver.taskmanager.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;
import pk.elfo.gameserver.taskmanager.TaskTypes;

/**
 * PkElfo
 */

public class TaskHappyHourStart extends Task
{
	String param1, param2, param3;
     
	private static final String NAME = "happy_hour_start";
     
	@Override
	public String getName() { return NAME; }

	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		OldConfig.values();
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(d);
          
		final int day = c.get(Calendar.DAY_OF_WEEK);
		String todayIs = ""; // Inscreva-se hoje
		String [] params = null; // Parametros do evento
           
		//File
		String lineRead = "";
       
		switch (day)
		{
			case 1:
				todayIs = "D";
				break;
			case 2:
				todayIs = "L";
				break;
			case 3:
				todayIs = "M";
				break;
			case 4:
				todayIs = "W";
				break;
			case 5:
				todayIs = "J";
				break;
			case 6:
				todayIs = "V";
			case 7:
				todayIs = "S";
				break;
			default: // Nunca deveria ocorrer
			{
				todayIs = "S";
				_log.info("Algo estranho aconteceu com as datas em TaskHappyHourStart: " + day);
			}
		}
           
		if (Config.DEBUG)
			_log.info("Hoje e " + todayIs);
		params = task.getParams();
		if (Config.DEBUG)
			_log.info("Parametros: " + params[0] + " " + params[1] + " " + params[2]);
           
		if (todayIs.equals(params[2].split(" ")[0]))
		{
			_log.info("Evento happyHour iniciado.");
			File f = new File("./config/Eventos/HappyHour/happyHour.properties");
			try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);)
			{
				while ((lineRead = br.readLine())!=null)
				{
					if (lineRead.split(" ")[0].contains("NAME"))
					{
						if (lineRead.split(" ")[1].equals(params[2].split(" ")[1]))
						{                                                      
							_log.info("Carregando e aplicando as configuracoes dso evento HappyHour.");
							Config.RATE_XP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_SP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_PARTY_XP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_PARTY_SP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_DROP_ITEMS = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_DROP_ITEMS_BY_RAID = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_DROP_SPOIL = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_DROP_MANOR = Integer.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_DROP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_XP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_SP = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_POTION = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_SCROLL = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_RECIPE = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.RATE_QUEST_REWARD_MATERIAL = Float.valueOf((br.readLine()).split(" ")[1]);
							Config.ENCHANT_CHANCE = Double.valueOf((br.readLine()).split(" ")[1]);
                                                   
							_log.info(" RATE_XP: " + Config.RATE_XP); _log.info(" RATE_SP: " + Config.RATE_SP);
							_log.info(" RATE_PARTY_XP: " + Config.RATE_PARTY_XP); _log.info(" RATE_PARTY_SP: " + Config.RATE_PARTY_SP);
							_log.info(" RATE_DROP_ITEMS: " + Config.RATE_DROP_ITEMS); _log.info(" RATE_DROP_ITEMS_BY_RAID: " + Config.RATE_DROP_ITEMS_BY_RAID);
							_log.info(" RATE_DROP_SPOIL: " + Config.RATE_DROP_SPOIL); _log.info(" RATE_DROP_MANOR: " + Config.RATE_DROP_MANOR);
							_log.info(" RATE_QUEST_DROP: " + Config.RATE_QUEST_DROP); _log.info(" RATE_QUEST_REWARD_XP: " + Config.RATE_QUEST_REWARD_XP);
							_log.info(" RATE_QUEST_REWARD_SP: " + Config.RATE_QUEST_REWARD_SP); _log.info(" RATE_QUEST_REWARD_POTION: " + Config.RATE_QUEST_REWARD_POTION);
							_log.info(" RATE_QUEST_REWARD_SCROLL: " + Config.RATE_QUEST_REWARD_SCROLL); _log.info(" RATE_QUEST_REWARD_RECIPE: " + Config.RATE_QUEST_REWARD_RECIPE);
							_log.info(" RATE_QUEST_REWARD_RECIPE: " + Config.RATE_QUEST_REWARD_RECIPE); _log.info(" RATE_QUEST_REWARD_MATERIAL: " + Config.RATE_QUEST_REWARD_MATERIAL);
							_log.info(" ENCHANT_CHANCE: " + Config.ENCHANT_CHANCE);
                                                   
							ExShowScreenMessage ess = new ExShowScreenMessage("Evento Happyhour iniciado!",1000);
							L2PcInstance[] PjsOnline = L2World.getInstance().getAllPlayersArray();
							for (L2PcInstance pj : PjsOnline)
								pj.sendPacket(ess);                                                   
							break;
						}
					}
				}
			}
			catch (IOException e)
			{ 
				e.printStackTrace(); 
			}
		}
	}
     
	@Override
	public void initializate()
	{
		super.initializate();
		_log.info("*** Happyhour (Start time) *** Inicializada");
		try ( Connection con = L2DatabaseFactory.getInstance().getConnection(); )
		{
			Statement st = con.createStatement();
			String query = "Select param1,param2,param3 from global_tasks where task=\"happy_hour_start\"";
			ResultSet executeQuery = st.executeQuery(query);
			while (executeQuery.next())
			{
				param1 = executeQuery.getString("param1");
				param2 = executeQuery.getString("param2");
				param3 = executeQuery.getString("param3"); // Onde esta o dia da programacao semanal
			}
			con.close(); st.close();
		}
		catch (Exception e) { e.printStackTrace(); }

		if (param1!=null && param2!=null)
			TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, param1, param2, param3, 0L);
             
		_log.info("Se ha anadido: " + NAME + " " + param1 + " " + param2 + " " + param3);
	}   
}

enum OldConfig
{
	RATE_XP(Config.RATE_XP),RATE_SP(Config.RATE_SP),RATE_PARTY_XP(Config.RATE_PARTY_XP), RATE_PARTY_SP(Config.RATE_PARTY_SP),
	RATE_DROP_ITEMS(Config.RATE_DROP_ITEMS), RATE_DROP_ITEMS_BY_RAID(Config.RATE_DROP_ITEMS_BY_RAID), RATE_DROP_SPOIL(Config.RATE_DROP_SPOIL),
	RATE_DROP_MANOR(Config.RATE_DROP_MANOR), RATE_QUEST_DROP(Config.RATE_QUEST_DROP), RATE_QUEST_REWARD_XP(Config.RATE_QUEST_REWARD_XP),
	RATE_QUEST_REWARD_SP(Config.RATE_QUEST_REWARD_SP), RATE_QUEST_REWARD_ADENA(Config.RATE_QUEST_REWARD_SP),
	RATE_QUEST_REWARD_POTION(Config.RATE_QUEST_REWARD_POTION), RATE_QUEST_REWARD_SCROLL(Config.RATE_QUEST_REWARD_SCROLL),
	RATE_QUEST_REWARD_RECIPE(Config.RATE_QUEST_REWARD_RECIPE), RATE_QUEST_REWARD_MATERIAL(Config.RATE_QUEST_REWARD_MATERIAL),
	ENCHANT_CHANCE(Config.ENCHANT_CHANCE);
     
	float rateFloat;
	int rateInt;
	double rateDouble;
     
	OldConfig(float f)
	{
		this.rateFloat = f;
	}
     
	OldConfig(int i)
	{
		this.rateInt = i;
	}
     
	OldConfig(double d)
	{
		this.rateDouble = d;
	}
     
	float getFloatRate() 
	{ 
		return rateFloat; 
	}
	int getIntegerRate() 
	{ 
		return rateInt; 
	} // Drop manor usa tipo int
	double getDoubleRate() 
	{ 
		return rateDouble; 
	}   // Enchant rate usa double
}