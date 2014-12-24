package handlers.admincommandhandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskTypes;
import javolution.text.TextBuilder;
 
/**
 * Projeto PkElfo
 */

public class AdminHappyHour implements IAdminCommandHandler
{
	private static Logger _log = Logger.getLogger(AdminHappyHour.class.getName());
	private static final String[] ADMIN_COMMANDS =   
	{
		"admin_happyhour" , "admin_happyhour_instructions" , "admin_happyhour_setXP" , "admin_happyhour_setSP", "admin_happyhour_setPartyXP",
		"admin_happyhour_setPartySP", "admin_happyhour_setIDrop", "admin_happyhour_setRDrop", "admin_happyhour_setSDrop",
		"admin_happyhour_setMDrop", "admin_happyhour_setQDrop", "admin_happyhour_setQXP", "admin_happyhour_setQSP",
		"admin_happyhour_setQAdena", "admin_happyhour_setQPotion", "admin_happyhour_setQScroll", "admin_happyhour_setQRecipe",
		"admin_happyhour_setQMaterial", "admin_happyhour_resetDefault", "admin_happyhour_saveConfig", "admin_happyhour_loadConfig",
		"admin_happyhour_schedule", "admin_happyhour_saveConfig", "admin_happyhour_nameConfig", "admin_happyhour_executeLoad",
		"admin_happyhour_setEChance", "admin_happyhour_implements"
	};
   
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{  
		if (command.equals("admin_happyhour"))  
		{
			showMenu(activeChar); // Muestra el menu principal
		}
		else if (command.equals("admin_happyhour_instructions"))
		{
			showInstructions(activeChar); // Muestra las instrucciones
		}
		else if (command.contains("admin_happyhour_setXP"))
		{
			setXP(activeChar, command); // XP
		}
		else if (command.contains("admin_happyhour_setSP"))
		{
			setSP(activeChar, command); // SP
		}
		else if (command.contains("admin_happyhour_setPartyXP"))
		{
			setPartyXP(activeChar,command); // Party XP
		}
		else if (command.contains("admin_happyhour_setPartySP"))
		{
			setPartySP(activeChar, command); // Party SP
		}
		else if (command.contains("admin_happyhour_setIDrop"))
		{
			setIDrop(activeChar, command); // Item drop
		}
		else if (command.contains("admin_happyhour_setRDrop"))
		{
			setRDrop(activeChar, command); // Raid drop
		}
		else if (command.contains("admin_happyhour_setSDrop"))
		{
			setSDrop(activeChar, command); // Spoil drop
		}
		else if (command.contains("admin_happyhour_setMDrop"))
		{
			setMDrop(activeChar, command); // Manor drop
		}
		else if (command.contains("admin_happyhour_setQDrop"))
		{
			setQDrop(activeChar, command); // Quest drop
		}
		else if (command.contains("admin_happyhour_setQXP"))
		{
			setQXP(activeChar, command); // Quest XP
		}
		else if (command.contains("admin_happyhour_setQSP"))
		{
			setQSP(activeChar, command); // Quest SP
		}
		else if (command.contains("admin_happyhour_setQAdena"))
		{
			setQAdena(activeChar, command); // Quest Adena
		}
		else if (command.contains("admin_happyhour_setQPotion"))
		{
			setQPotion(activeChar, command); // Quest Potion
		}
		else if (command.contains("admin_happyhour_setQScroll"))
		{
			setQScroll(activeChar, command); // Quest Scroll
		}
		else if (command.contains("admin_happyhour_setQRecipe"))
		{
			setQRecipe(activeChar, command); // Quest Recipe
		}
		else if (command.contains("admin_happyhour_setQMaterial"))
		{
			setQMaterial(activeChar,command); // Quest Material
		}
		else if (command.contains("admin_happyhour_setEChance"))
		{
			setEChance(activeChar, command); // Enchant Chance
		}
		else if (command.equals("admin_happyhour_resetDefault"))
		{
			doResetDefault(activeChar); // Volver a la conf. por defecto
		}
		else if (command.equals("admin_happyhour_saveConfig"))
		{
			doAskingNameConfig(activeChar); // Pregunta el nombre de esta configuracion
		}
		else if (command.contains("admin_happyhour_nameConfig"))
		{
			DoSaving(activeChar, command); // Se graba el fichero con la config
		}
		else if (command.equals("admin_happyhour_loadConfig"))
		{
			doLoadConfig(activeChar); // Muestra las configuraciones guardadas
		}
		else if (command.contains("admin_happyhour_executeLoad"))
		{
			admin_happyhour_executeLoad(activeChar,command); // Carga la configuracion elegida
		}
		else if (command.equals("admin_happyhour_schedule"))
		{
			doSchedule(activeChar); // Programa el evento para ser autoejecutado en la fecha dada
		}
		else if (command.contains("admin_happyhour_implements"))
		{
			recordSchedule(activeChar, command); // Graba en global task la inicializacion y finalizacion de HappyHour
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
   
	private void showMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Evento Happy Hour PkElfo</title><body>");
		replyMSG.append
		(
		"<center>" +
		"<center><br><button value=\"Instrucoes\" action=\"bypass -h admin_happyhour_instructions\" width=100 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>" +
		"<img src=L2UI_CH3.onscrmsg_pattern01_1 width=\"300\" height=\"32\" align=\"center\">" +
		"<table width=270>" +
            "<tr>" +
               // XP
               "<td width=135>XP x:<edit var=\"XP\" width=\"125\">(" + Config.RATE_XP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_XP.getFloatRate() + "</font>)</td>" +
               "><td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setXP $XP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr><tr><td><br></td></tr><tr>" +
               // SP
               "<td width=135>SP x:<edit var=\"SP\" width=\"125\">(" + Config.RATE_SP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_SP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setSP $SP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +            
            "</tr><tr><td><br></td></tr><tr>" +
               // Party XP
               "<td width=135>Party XP x:<edit var=\"P_XP\" width=\"125\">(" + Config.RATE_PARTY_XP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_PARTY_XP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setPartyXP $P_XP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr><tr><td><br></td></tr><tr>" +
               // Party SP
               "<td width=135>Party SP x:<edit var=\"P_SP\" width=\"125\">(" + Config.RATE_PARTY_SP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_PARTY_SP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setPartySP $P_SP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate ItemDrop
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Item Drop x:<edit var=\"ItemDrop\" width=\"125\">(" + Config.RATE_DROP_ITEMS + ")(<font color =\"FF00FF\">" + OldConfig.RATE_DROP_ITEMS.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setIDrop $ItemDrop\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate RaidDrop
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Raid Drop x:<edit var=\"RaidDrop\" width=\"125\">(" + Config.RATE_DROP_ITEMS_BY_RAID + ")(<font color =\"FF00FF\">" + OldConfig.RATE_DROP_ITEMS_BY_RAID.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setRDrop $RaidDrop\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate SpoilDrop
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Spoil Drop x:<edit var=\"SpoilDrop\" width=\"125\">(" + Config.RATE_DROP_SPOIL + ")(<font color =\"FF00FF\">" + OldConfig.RATE_DROP_SPOIL.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setSDrop $SpoilDrop\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate DropManor
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Manor Drop x:<edit var=\"ManorDrop\" width=\"125\">(" + Config.RATE_DROP_MANOR + ")(<font color =\"FF00FF\">" + OldConfig.RATE_DROP_MANOR.getIntegerRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setMDrop $ManorDrop\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate QuestDrop
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Quest Drop x:<edit var=\"QuestDrop\" width=\"125\">(" + Config.RATE_QUEST_DROP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_DROP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQDrop $QuestDrop\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
               // Rate Quest XP
            "</tr><tr><td><br></td></tr><tr>" +
               "<td width=135>Quest XP x:<edit var=\"Q_XP\" width=\"125\">(" + Config.RATE_QUEST_REWARD_XP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_XP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQXP $Q_XP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br></td><br><br>" +            
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest SP
               "<td width=135>Quest SP x:<edit var=\"Q_SP\" width=\"125\">(" + Config.RATE_QUEST_REWARD_SP + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_SP.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQSP $Q_SP\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +            
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest Adena
               "<td width=135>Quest Adena x:<edit var=\"Q_Adena\" width=\"125\">(" + Config.RATE_QUEST_REWARD_ADENA + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_ADENA.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQAdena $Q_Adena\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +            
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest Potion
               "<td width=135>Quest Potion x:<edit var=\"Q_Potion\" width=\"125\">(" + Config.RATE_QUEST_REWARD_POTION + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_POTION.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQPotion $Q_Potion\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +            
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest Scroll
               "<td width=135>Quest Scroll x:<edit var=\"Q_Scroll\" width=\"125\">(" + Config.RATE_QUEST_REWARD_SCROLL + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_SCROLL.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQScroll $Q_Scroll\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest Recipe
               "<td width=135>Quest Recipe x:<edit var=\"Q_Recipe\" width=\"125\">(" + Config.RATE_QUEST_REWARD_RECIPE + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_RECIPE.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQRecipe $Q_Recipe\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate Quest Material
               "<td width=135>Quest Material x:<edit var=\"Q_Material\" width=\"125\">(" + Config.RATE_QUEST_REWARD_MATERIAL + ")(<font color =\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_MATERIAL.getFloatRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setQMaterial $Q_Material\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr><tr><td><br></td></tr><tr>" +
               // Rate ENCHANT_CHANCE
               "<td width=135>Enchant Chance %:<edit var=\"Enchant_Chance\" width=\"125\">(" + Config.ENCHANT_CHANCE + ")(<font color =\"FF00FF\">" + OldConfig.ENCHANT_CHANCE.getDoubleRate() + "</font>)</td>" +
               "<td width=135><br><br><button value=\"Set\" action=\"bypass -h admin_happyhour_setEChance $Enchant_Chance\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br></td>" +
            "</tr>" +
         "</table></center>" +
         "<img src=L2UI_CH3.onscrmsg_pattern01_2 width=\"300\" height=\"32\" align=\"center\"><br><br>" +
         "<img src=L2UI_CH3.herotower_deco width=\"256\" height=\"32\" align=\"center\">" +
         "<center><button value=\"Resetar\" action=\"bypass -h admin_happyhour_resetDefault \" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" +
         "<center><button value=\"Guardar\" action=\"bypass -h admin_happyhour_saveConfig \" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" +
         "<center><button value=\"Carregar\" action=\"bypass -h admin_happyhour_loadConfig \" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" +
         "<center><button value=\"Activacao periodica\" action=\"bypass -h admin_happyhour_schedule \" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		);
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
 
	private void showInstructions(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>EventoHappy Hour PkElfo</title><body>");
		replyMSG.append("<center><font color=\"FF0000\">Atencion, el cambio de rates es inmediato!</font></center><br><br>La idea de este evento es la de subir los rates del server por un periodo determinado, animar a los jugadores a masificarse a ciertas horas en el servidor para hacerlo mas atractivo.<br><br>" +
         "En las opciones disponibles puedes dejar los rates como estan dejando el numero <font color=\"00FF00\">1</font>, ya que, al multiplicar los rates actuales del server por <font color=\"00FF00\">1</font>, pues da <font color=\"00FF00\">1</font>.<br><br>" +
         "Tambien puede interesarte subir rates, entonces a partir de <font color=\"00FF00\">1</font>, consigues subir los rates. Por ejemplo, un rate de <font color=\"#0FF00\">1.10</font> suben los rates un 10%, unos rates <font color=\"00FF00\">2</font> suben un 100% ( osea, si tu server es <font color=\"00FF00\">x8</font> y en la happy hour pones <font color=\"00FF00\">2</font>, tu server se convierte en un <font color=\"00FF00\">x16</font>).<br><br>" +
         "Para poder equilibrar la <font color=\"00FFFF\">Party hour</font>, puedes tambien bajar otros rates usando un numero menor al <font color=\"00FF00\">1</font>, por ejemplo, <font color=\"00FF00\">0.9</font> disminuye un 10% los rates de tu server.<br><br>" +
         "Puedes programar la <font color=\"00FFFF\">Party hour</font> semanalmente y activarlos manualmente, muy apropiado para pedir a los jugadores que voten al server, por ejemplo, decirles, <font color=\"FFFF00\">Si llegamos a 1000 votos, 1 hora de party Hour.</font>. Mi idea inicial era la de subir los rates una hora el sabado y el domingo, que es cuando mas afluencia tiene un servidor.<br><br>" +
         "<font color=\"FF0000\"> La ejecucion programada del evento hace que la configuracion por defecto recoja los valores del evento en lugar de los originales del servidor, asi que aconsejo que guardes una copia de los valores originales por si quieres restaurarlos cuando se haya iniciado el evento programado. </font><br><br>" +
         "El evento programado, por defecto dura maximo 23:59:59 horas. <br>" +
         "Si la programacion del evento inicial y final tienen la misma hora, se borrara el evento de la base de datos.<br>" +
         "Un evento programado solo podra ser autoejecutado despues de un restart del servidor.<br><br>" +
         "Si desprogramas un evento que se iba a ejecutar, el evento se ejecutara mientras no se haga un restart del servidor.<br><br><br>" +
         "<td width=270 align=\"center\"><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);      
	}
   
	private float getFloatRate(String command)
	{
		float rate = 1;
		try   { rate = Float.parseFloat(command.split(" ")[1]);}
		catch (NumberFormatException e)
		{
			_log.warning("Valor ilegal para " + rate + ". El tipo introducido no pudo ser convertido a Float en AdaminHappyHour#getFloatRate, se queda en 1.");
			rate = 1;
		}
		catch (ArrayIndexOutOfBoundsException e) { _log.warning("AdminHappyHour#getFloatRate, sin parametros: " + command); }
		catch (Exception e) { e.printStackTrace(); }
		if (rate < 0) rate = 1;
		return rate;
	}
   
	private int getIntegerRate(String command)
	{
		int rate = 1;
     
		try   { rate = Integer.parseInt(command.split(" ")[1]);}
		catch (NumberFormatException e)
		{
			_log.warning("Valor ilegal para " + rate + ". El tipo introducido no pudo ser convertido a Float en AdaminHappyHour#getFloatRate, se queda en 1.");
			rate = 1;
		}
		catch (ArrayIndexOutOfBoundsException e) { _log.warning("AdminHappyHour#getIntegerRate, sin parametros: " + command); }
		catch (Exception e) { e.printStackTrace(); }
		if (rate < 0) rate = 1;
		return rate;
	}
   
	private Double getDoubleRate(String command)
	{
		double rate = 1;
		try   { rate = Double.parseDouble(command.split(" ")[1]);}
		catch (NumberFormatException e)
		{
			_log.warning("Valor ilegal para " + rate + ". El tipo introducido no pudo ser convertido a Double en AdaminHappyHour#getDoubleRate, se queda en 1.");
			rate = 1;
		}
		catch (ArrayIndexOutOfBoundsException e) { _log.warning("AdminHappyHour#getDoubleRate, sin parametros: " + command); }
		catch (Exception e) { e.printStackTrace(); }
		if (rate < 0) rate = 1;
		return rate;
	}
   
	private void setXP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_XP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_XP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_XP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setSP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_SP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_SP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_SP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setPartyXP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_PARTY_XP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_PARTY_XP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_PARTY_XP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setPartySP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_PARTY_SP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_PARTY_SP.getFloatRate()+ "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_PARTY_SP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setIDrop(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_DROP_ITEMS *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_DROP_ITEMS.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_DROP_ITEMS + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void setRDrop(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_DROP_ITEMS_BY_RAID *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_DROP_ITEMS_BY_RAID.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_DROP_ITEMS_BY_RAID + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setSDrop(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_DROP_SPOIL *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_DROP_SPOIL.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_DROP_SPOIL + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setMDrop(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getIntegerRate(command);
		Config.RATE_DROP_MANOR *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_DROP_MANOR.getIntegerRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_DROP_MANOR + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQDrop(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_DROP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_DROP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_DROP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQXP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_XP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_XP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_XP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQSP(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_SP *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_SP.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_SP + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQAdena(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_ADENA *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_ADENA.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_ADENA + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQPotion(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_POTION *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_POTION.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_POTION + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQScroll(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_SCROLL *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_SCROLL.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_SCROLL + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQRecipe(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_RECIPE *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_RECIPE.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_RECIPE + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setQMaterial(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		float newRate = getFloatRate(command);
		Config.RATE_QUEST_REWARD_MATERIAL *= newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.RATE_QUEST_REWARD_MATERIAL.getFloatRate() + "</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.RATE_QUEST_REWARD_MATERIAL + "</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void setEChance(L2PcInstance activeChar, String command)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		Double newRate = getDoubleRate(command);
		Config.ENCHANT_CHANCE = newRate;
     
		if (Config.DEBUG)
		{
			replyMSG.append("Set " + command);
		}
     
		replyMSG.append("Antes: <font color=\"FF00FF\">" + OldConfig.ENCHANT_CHANCE.getDoubleRate() + " %</font><br>");
		replyMSG.append("Ahora: <font color=\"00FF00\">" + Config.ENCHANT_CHANCE + "%</font><br><br><br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
     
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
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
		}      // Drop manor usa tipo int
		double getDoubleRate()
		{
			return rateDouble;
		}   // Enchant rate usa double
	}
   
	private void doResetDefault(L2PcInstance activeChar)
	{
		Config.RATE_XP = OldConfig.RATE_XP.getFloatRate();
		Config.RATE_SP = OldConfig.RATE_SP.getFloatRate();
		Config.RATE_PARTY_XP = OldConfig.RATE_PARTY_XP.getFloatRate();
		Config.RATE_PARTY_SP = OldConfig.RATE_PARTY_SP.getFloatRate();
		Config.RATE_DROP_ITEMS = OldConfig.RATE_DROP_ITEMS.getFloatRate();
		Config.RATE_DROP_ITEMS_BY_RAID = OldConfig.RATE_DROP_ITEMS_BY_RAID.getFloatRate();
		Config.RATE_DROP_SPOIL = OldConfig.RATE_DROP_SPOIL.getFloatRate();
		Config.RATE_DROP_MANOR = OldConfig.RATE_DROP_MANOR.getIntegerRate();
		Config.RATE_QUEST_DROP = OldConfig.RATE_QUEST_DROP.getFloatRate();
		Config.RATE_QUEST_REWARD_XP = OldConfig.RATE_QUEST_REWARD_XP.getFloatRate();
		Config.RATE_QUEST_REWARD_SP = OldConfig.RATE_QUEST_REWARD_SP.getFloatRate();
		Config.RATE_QUEST_REWARD_POTION = OldConfig.RATE_QUEST_REWARD_POTION.getFloatRate();
		Config.RATE_QUEST_REWARD_SCROLL = OldConfig.RATE_QUEST_REWARD_SCROLL.getFloatRate();
		Config.RATE_QUEST_REWARD_RECIPE = OldConfig.RATE_QUEST_REWARD_RECIPE.getFloatRate();
		Config.RATE_QUEST_REWARD_MATERIAL = OldConfig.RATE_QUEST_REWARD_MATERIAL.getFloatRate();
		Config.ENCHANT_CHANCE = OldConfig.ENCHANT_CHANCE.getDoubleRate();
     
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		replyMSG.append("La configuracion original ha sido devuelta al servidor.<br><br>");
		replyMSG.append("<center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void doAskingNameConfig(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
		replyMSG.append("Introduce un nombre para esta configuracion (sin espacios):<br><edit var=\"nameConfig\" width=\"125\"><br><br>");
		replyMSG.append("<center><button value=\"Grabar\" action=\"bypass -h admin_happyhour_nameConfig $nameConfig\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void DoSaving(L2PcInstance activeChar, String command)
	{
		File HappyHourFile = new File("./config/HappyHour.properties");
		String datos = "", fileName = "NoName";
		String lineaDato = "";
     
		try
		{
			fileName = (command.split(" ")[1].length()>0) ? command.split(" ")[1] : "NoName";
		}
		catch (IndexOutOfBoundsException e) { fileName = "NoName"; }
     
		// HTML
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour</title><body>");
     
		//Existe la fila? Si => entonces cargar la información
		if (HappyHourFile.isFile())
		{
			try (FileReader fr = new FileReader(HappyHourFile);
				BufferedReader br = new BufferedReader(fr);)
			{
				while ((lineaDato = br.readLine()) != null)
				{
					datos += lineaDato + "\n";
				}
				replyMSG.append("Se han cargado los datos previos.<br>");
			}
			catch (Exception e) {
			_log.severe("No se pudo cargar Happyhour.properties.<br>"); e.printStackTrace();
			replyMSG.append("Error, no se ha podido leer el fichero existente.<br>");
		}
	}
     
		// Existe el fichero? No, pues creamos uno
		if (!HappyHourFile.exists())
		{
			try { HappyHourFile.createNewFile(); replyMSG.append("Se ha creado un fichero en el directorio config<br>"); }
			catch (IOException e1) { replyMSG.append("Error al crear el archivo config/HappyHour.properties<br>");_log.warning("Error de lectura, no se ha podido acceder a config/"); e1.printStackTrace(); }
		}
     
		// Montando los datos del properties
		datos += ("NAME " + fileName + "\n");
		datos += ("RATE_XP " + Config.RATE_XP + "\n");
		datos += ("RATE_SP " + Config.RATE_SP + "\n");
		datos += ("RATE_PARTY_XP " + Config.RATE_PARTY_XP + "\n");
		datos += ("RATE_PARTY_SP " + Config.RATE_PARTY_SP + "\n");
		datos += ("RATE_DROP_ITEMS " + Config.RATE_DROP_ITEMS + "\n");
		datos += ("RATE_DROP_ITEMS_BY_RAID " + Config.RATE_DROP_ITEMS_BY_RAID + "\n");
		datos += ("RATE_DROP_SPOIL " + Config.RATE_DROP_SPOIL + "\n");
		datos += ("RATE_DROP_MANOR " + Config.RATE_DROP_MANOR + "\n");
		datos += ("RATE_QUEST_DROP " + Config.RATE_QUEST_DROP + "\n");
		datos += ("RATE_QUEST_REWARD_XP " + Config.RATE_QUEST_REWARD_XP + "\n");
		datos += ("RATE_QUEST_REWARD_SP " + Config.RATE_QUEST_REWARD_SP + "\n");
		datos += ("RATE_QUEST_REWARD_POTION " + Config.RATE_QUEST_REWARD_POTION + "\n");
		datos += ("RATE_QUEST_REWARD_SCROLL " + Config.RATE_QUEST_REWARD_SCROLL + "\n");
		datos += ("RATE_QUEST_REWARD_RECIPE " + Config.RATE_QUEST_REWARD_RECIPE + "\n");
		datos += ("RATE_QUEST_REWARD_MATERIAL " + Config.RATE_QUEST_REWARD_MATERIAL + "\n");
		datos += ("ENCHANT_CHANCE " + Config.ENCHANT_CHANCE + "\n");

		// Grabar configuración
     
		try (FileWriter fw = new FileWriter(HappyHourFile);)
		{
			fw.write(datos);
			fw.flush();
			replyMSG.append("Datos salvados");
		}
		catch (IOException e)
		{
			_log.severe("No se pudo guardar HappyHour.properties<br>"); e.printStackTrace();
			replyMSG.append("Error al salvar los datos de HappyHour.properties!<br>");
		}
		replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
 
	private void doLoadConfig(L2PcInstance activeChar)
	{
		File HappyHourFile = new File("./config/HappyHour.properties");
		String lineaDato = "", datos = "", botonName = "";
		// HTML
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
     
		// Averiguar cuantos datos existen
		try (FileReader fr = new FileReader(HappyHourFile); BufferedReader bf = new BufferedReader(fr); )
		{
			while ((lineaDato = bf.readLine())!=null)
			{
				datos += lineaDato + "\n";
			}
		}
		catch (FileNotFoundException e)
		{
			replyMSG.append("No existen datos previos.<br>");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			replyMSG.append("Operación denegada. Vea la consola para mas informacion.<br>");
			e.printStackTrace();
		}

		replyMSG.append("<center>Elija una de las configuraciones disponibles:</center><br><br>");
     
		String [] datosOrganizados = datos.split("\\n");
		for (String nombreConfig : datosOrganizados)
		{
			if (nombreConfig.contains("NAME"))
			{
				botonName = nombreConfig.split(" ")[1];
				replyMSG.append("<center><button value=\"" + botonName + "\" action=\"bypass -h admin_happyhour_executeLoad " + botonName + "\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center><br>");
			}  
		}

		// Un HTML demasiado largo crashearia el cliente
		if(datos.length() > 8192)
		{
			replyMSG.clear();
			replyMSG.append("El fichero es demasiado largo y no puede ser mostrado.<br><br>");
			replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		}
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
   
	private void admin_happyhour_executeLoad(L2PcInstance activeChar, String command)
	{
		String lineaDatos = "";
		String regexRes[] = null;
     
		File happyHourFile = new File("./config/HappyHour.properties");
     
		// HTML
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
     
		try
		( 
			FileReader fr = new FileReader(happyHourFile);   BufferedReader br = new BufferedReader(fr);
		)
		{
			while ((lineaDatos = br.readLine())!=null)
			{
				if (lineaDatos.contains("NAME"))
				{
					regexRes = lineaDatos.split(" ");
					if (regexRes[1].equals(command.split(" ")[1]))
					{
						// Coincidencia, cargando configuración
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
						replyMSG.append("Configuración cargada y ejecutada. <br>");
						replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			replyMSG.append("El fichero happyHour.properties no existe.<br><br> La configuracion no ha sido cargada.<br><br>");
		}
		catch (ArrayIndexOutOfBoundsException e) { _log.warning("Outbounds: " + regexRes + "\n" + command + "\n"); e.printStackTrace(); }
		catch (IOException e)
		{
			_log.severe("Excepcion arrojada en AdminHappyHour#executeLoad");
			e.printStackTrace();
			replyMSG.append("Error desconocido.<br><br> La configuracion no ha sido cargada.<br><br>");
		}
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	private void doSchedule(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
               
		File HappyHourFile = new File("./config/HappyHour.properties");
		if (HappyHourFile.exists())
		{
			// Nombre del evento que quieres programar
			replyMSG.append("<center>Introduce el nombre del evento que quieres programar:</center><br>");
			replyMSG.append("<center><font color=\"00FF00\">(Nombre sin espacios)</font></center><br>");
			replyMSG.append("<center><edit var=\"eventName\" width=\"125\"></center><br><br>");
			// Dia de la semana
			replyMSG.append("<center>Introduce un dia para programar el evento:</center><br>");
			replyMSG.append("<center><font color=\"00FF00\">(L, M, W, J, V, S, D)</font></center><br>");
			replyMSG.append("<center><edit var=\"weekDay\" width=\"125\"></center><br><br>");
			// Hora de inicio
			replyMSG.append("<center>Introduce la hora de inicio del evento:</center><br>");
			replyMSG.append("<center><font color=\"00FF00\">(Formato 24 hrs, 18:00:00, son las 6 de la tarde)</font></center><br>");
			replyMSG.append("<center><edit var=\"startHappyHour\" width=\"125\"></center><br><br>");
			// Hora de finalizacion
			replyMSG.append("<center>Introduce la hora de finalizacion del evento:</center><br>");
			replyMSG.append("<center><font color=\"00FF00\">(Formato 24 hrs, 18:30:00, son las 6:30 de la tarde)</font></center><br>");
			replyMSG.append("<center><font color=\"00DD00\">(Si la hora de inicio es la misma que la hora final, se borrara la programacion de los eventos guardados.)</font></center><br>");
			replyMSG.append("<center><edit var=\"endHappyHour\" width=\"125\"></center><br><br><br><br>");
			// Botones
			replyMSG.append("<br><br><br><center><button value=\"Grabar\" action=\"bypass -h admin_happyhour_implements $weekDay $startHappyHour $endHappyHour $eventName\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center><br>");
			replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		}
		else
		{
			replyMSG.append("<center>No puedes programar un evento si no esta grabado.</center>");
			replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
		}
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
	}
           
	private void recordSchedule (L2PcInstance activeChar, String command)
	{
	boolean error = false;
	// File
	File f = new File ("./config/HappyHour.properties");
	String lineaDato = "";
	boolean fileFound = false;
               
	// HTML
	NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
	TextBuilder replyMSG = new TextBuilder("<html><title>Happy Hour </title><body>");
           
	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
		Statement st = con.createStatement();
		FileReader fr = new FileReader(f);
		BufferedReader bf = new BufferedReader(fr);)
		{
			final String dia = command.split(" ")[1];
			final String horaInicio = command.split(" ")[2];
			final String horaFinal = command.split(" ")[3];
			final String eventoNombre = command.split(" ")[4];
			// Existe este evento?
                     
			while ((lineaDato = bf.readLine())!=null)
			{
				if (lineaDato.contains(eventoNombre))
				{
					fileFound = true;
					break;
				}
			}
                                 
			// Solo un evento activo
			final String deleteHappyHoursStart = "Delete from global_tasks where task='happyHourStart'";
			final String deleteHappyHoursEnd = "Delete from global_tasks where task='happyHourEnd'";
                     
			if ((horaInicio.split(":").length!=3) || (horaFinal.split(":").length!=3) || (horaInicio.length()!=8) || (horaFinal.length()!=8))
			{
				_log.warning("Formato horario erroneo en recordSchedule: " + horaInicio + " " + horaFinal);
				error = true;
				replyMSG.append("Error: Formato horario erroneo.<br>");
			}
                     
			if (!dia.equals("L") && !dia.equals("M") && !dia.equals("W") && !dia.equals("J") && !dia.equals("V") && !dia.equals("S") && !dia.equals("D"))
			{
				_log.warning("Dia incorrecto");
				error = true;
				replyMSG.append("Error: Dia incorrecto.<br>");
			}
                     
			if (!fileFound)
			{
				error = true;
				_log.info("Se ha intentado cargar un evento happyHour que no existe.");
				replyMSG.append("Error: El evento seleccionado no existe.<br>");
			}
                     
			if (error == false)
			{
				st.execute(deleteHappyHoursStart);
				st.execute(deleteHappyHoursEnd);
				if (!horaInicio.equals(horaFinal))
				{
					// En el 3er parámetro añado el dia y el nombre del evento
					TaskManager.addTask("happy_hour_start", TaskTypes.TYPE_SCHEDULED_PER_DAY, "1", horaInicio, dia + " " + eventoNombre);
					TaskManager.addTask("happy_hour_end", TaskTypes.TYPE_SCHEDULED_PER_DAY, "1", horaFinal, dia);
						replyMSG.append("Se ha programado el evento.<br>");
				}
			}
			con.close(); st.close();
		}
		catch (ArrayIndexOutOfBoundsException e) { _log.warning("Numero de parametros incorrecto en HappyHour#recordSchedule"); replyMSG.append("Numero de parametros incorrectos.<br>"); }
		catch (SQLException e) { _log.warning("Error de query en HappyHour#recordSchedule"); replyMSG.append("Error en la base de datos.<br>"); }
		catch (FileNotFoundException e){ _log.warning("No se ha encontrado happyHour.properties"); replyMSG.append("No se ha encontrado el fichero happyHour.properties.<br>"); }
		catch (IOException e) {   _log.warning("No se ha podido leer happyHour.properties");   replyMSG.append("No se ha podido leer happyHour.properties.<br>"); }  
               
		replyMSG.append("<br><br><br><center><button value=\"Menu\" action=\"bypass -h admin_happyhour\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
           
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
}