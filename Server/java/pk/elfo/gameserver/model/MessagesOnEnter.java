package pk.elfo.gameserver.model;

import pk.elfo.Config;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;
import pk.elfo.gameserver.ThreadPoolManager;

public class MessagesOnEnter implements Runnable
{
	public static L2PcInstance activeChar;
	int _text;
	
	public MessagesOnEnter(L2PcInstance player, int text)
	{
		activeChar = player;
		_text = text;
	}

	@Override
	public void run()
	{
		if(_text == 1){
			if(Config.ON_ENTER_TEXT1_TYPE.equals("PARTYROOM_COMMANDER")){
				activeChar.sendPacket(new CreatureSay(15, Say2.PARTYROOM_COMMANDER,activeChar.getName(),Config.ON_ENTER_TEXT1));
			}
			else if(Config.ON_ENTER_TEXT1_TYPE.equals("EXSHOW")){
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT1, 6*1000));
			}
			else if(Config.ON_ENTER_TEXT1_TYPE.equals("CRITICAL_ANNOUNCE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CRITICAL_ANNOUNCE,activeChar.getName(),Config.ON_ENTER_TEXT1));
			}
			else if(Config.ON_ENTER_TEXT1_TYPE.equals("CLAN")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CLAN,activeChar.getName(),Config.ON_ENTER_TEXT1));
			}
			else if(Config.ON_ENTER_TEXT1_TYPE.equals("HERO_VOICE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.HERO_VOICE,activeChar.getName(),Config.ON_ENTER_TEXT1));
			}
			else{
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT1, 6*1000));
			}
		}
		else if(_text == 2){
			if(Config.ON_ENTER_TEXT2_TYPE.equals("PARTYROOM_COMMANDER")){
				activeChar.sendPacket(new CreatureSay(15, Say2.PARTYROOM_COMMANDER,activeChar.getName(),Config.ON_ENTER_TEXT2));
			}
			else if(Config.ON_ENTER_TEXT2_TYPE.equals("EXSHOW")){
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT2, 6*1000));
			}
			else if(Config.ON_ENTER_TEXT2_TYPE.equals("CRITICAL_ANNOUNCE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CRITICAL_ANNOUNCE,activeChar.getName(),Config.ON_ENTER_TEXT2));
			}
			else if(Config.ON_ENTER_TEXT2_TYPE.equals("CLAN")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CLAN,activeChar.getName(),Config.ON_ENTER_TEXT2));
			}
			else if(Config.ON_ENTER_TEXT2_TYPE.equals("HERO_VOICE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.HERO_VOICE,activeChar.getName(),Config.ON_ENTER_TEXT2));
			}
			else{
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT2, 6*1000));
			}
		}
		else if(_text == 3){
			if(Config.ON_ENTER_TEXT3_TYPE.equals("PARTYROOM_COMMANDER")){
				activeChar.sendPacket(new CreatureSay(15, Say2.PARTYROOM_COMMANDER,activeChar.getName(),Config.ON_ENTER_TEXT3));
			}
			else if(Config.ON_ENTER_TEXT3_TYPE.equals("EXSHOW")){
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT3, 6*1000));
			}
			else if(Config.ON_ENTER_TEXT3_TYPE.equals("CRITICAL_ANNOUNCE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CRITICAL_ANNOUNCE,activeChar.getName(),Config.ON_ENTER_TEXT3));
			}
			else if(Config.ON_ENTER_TEXT3_TYPE.equals("CLAN")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CLAN,activeChar.getName(),Config.ON_ENTER_TEXT3));
			}
			else if(Config.ON_ENTER_TEXT3_TYPE.equals("HERO_VOICE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.HERO_VOICE,activeChar.getName(),Config.ON_ENTER_TEXT3));
			}
			else{
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT3, 6*1000));
			}
		}
		else if(_text == 4){
			if(Config.ON_ENTER_TEXT4_TYPE.equals("PARTYROOM_COMMANDER")){
				activeChar.sendPacket(new CreatureSay(15, Say2.PARTYROOM_COMMANDER,activeChar.getName(),Config.ON_ENTER_TEXT4));
			}
			else if(Config.ON_ENTER_TEXT4_TYPE.equals("EXSHOW")){
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT4, 6*1000));
			}
			else if(Config.ON_ENTER_TEXT4_TYPE.equals("CRITICAL_ANNOUNCE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CRITICAL_ANNOUNCE,activeChar.getName(),Config.ON_ENTER_TEXT4));
			}
			else if(Config.ON_ENTER_TEXT4_TYPE.equals("CLAN")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CLAN,activeChar.getName(),Config.ON_ENTER_TEXT4));
			}
			else if(Config.ON_ENTER_TEXT4_TYPE.equals("HERO_VOICE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.HERO_VOICE,activeChar.getName(),Config.ON_ENTER_TEXT4));
			}
			else{
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT4, 6*1000));
			}
		}
		else if(_text == 5){
			if(Config.ON_ENTER_TEXT5_TYPE.equals("PARTYROOM_COMMANDER")){
				activeChar.sendPacket(new CreatureSay(15, Say2.PARTYROOM_COMMANDER,activeChar.getName(),Config.ON_ENTER_TEXT5));
			}
			else if(Config.ON_ENTER_TEXT5_TYPE.equals("EXSHOW")){
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT5, 6*1000));
			}
			else if(Config.ON_ENTER_TEXT5_TYPE.equals("CRITICAL_ANNOUNCE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CRITICAL_ANNOUNCE,activeChar.getName(),Config.ON_ENTER_TEXT5));
			}
			else if(Config.ON_ENTER_TEXT5_TYPE.equals("CLAN")){
				activeChar.sendPacket(new CreatureSay(15, Say2.CLAN,activeChar.getName(),Config.ON_ENTER_TEXT5));
			}
			else if(Config.ON_ENTER_TEXT5_TYPE.equals("HERO_VOICE")){
				activeChar.sendPacket(new CreatureSay(15, Say2.HERO_VOICE,activeChar.getName(),Config.ON_ENTER_TEXT5));
			}
			else{
				activeChar.sendPacket(new ExShowScreenMessage(Config.ON_ENTER_TEXT5, 6*1000));
			}
		}
		else{
			if(Config.ON_ENTER_TEXT1_ENABLE && Config.ON_ENTER_TEXT1_DELAY != 0 && Config.ON_ENTER_TEXT1 != null){
				ThreadPoolManager.getInstance().scheduleGeneral(new MessagesOnEnter(activeChar,1), Config.ON_ENTER_TEXT1_DELAY * 1000);//IN SECONDS
			}
			if(Config.ON_ENTER_TEXT2_ENABLE && Config.ON_ENTER_TEXT2_DELAY != 0 && Config.ON_ENTER_TEXT2 != null){
				ThreadPoolManager.getInstance().scheduleGeneral(new MessagesOnEnter(activeChar,2), Config.ON_ENTER_TEXT2_DELAY * 1000);
			}
			if(Config.ON_ENTER_TEXT3_ENABLE && Config.ON_ENTER_TEXT3_DELAY != 0 && Config.ON_ENTER_TEXT3 != null){
				ThreadPoolManager.getInstance().scheduleGeneral(new MessagesOnEnter(activeChar,3), Config.ON_ENTER_TEXT3_DELAY * 1000);
			}
			if(Config.ON_ENTER_TEXT4_ENABLE && Config.ON_ENTER_TEXT4_DELAY != 0 && Config.ON_ENTER_TEXT4 != null){
				ThreadPoolManager.getInstance().scheduleGeneral(new MessagesOnEnter(activeChar,4), Config.ON_ENTER_TEXT4_DELAY * 1000);
			}
			if(Config.ON_ENTER_TEXT5_ENABLE && Config.ON_ENTER_TEXT5_DELAY != 0 && Config.ON_ENTER_TEXT5 != null){
				ThreadPoolManager.getInstance().scheduleGeneral(new MessagesOnEnter(activeChar,5), Config.ON_ENTER_TEXT5_DELAY * 1000);
			}			
		}
	}
}