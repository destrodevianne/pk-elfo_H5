package pk.elfo.gameserver;

import java.util.ArrayList;

import pk.elfo.gameserver.network.serverpackets.L2GameServerPacket;
 
/**
 * Projeto PkElfo
 */

public class Text extends L2GameServerPacket
{
	public static enum ScreenMessageAlign
	{
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT,
		MIDDLE_LEFT,
		MIDDLE_CENTER,
		MIDDLE_RIGHT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT
	}
	
	private final boolean _hide, _big_font, _effect;
	private final ScreenMessageAlign _text_align;
	private final int _time, _sysMessageId;
	private int _clientMessageId;
	private final ArrayList<String> _text = new ArrayList<>();
	
	public Text(String text, int time, ScreenMessageAlign text_align, boolean big_font, boolean type, int messageId, boolean showEffect)
	{
		_hide = type;
		_sysMessageId = messageId;
		_text.add(text);
		_time = time;
		_text_align = text_align;
		_big_font = big_font;
		_effect = showEffect;
	}
	
	public Text(int clientMsgId, int time, ScreenMessageAlign text_align, boolean big_font, boolean type, int messageId, boolean showEffect)
	{
		_hide = type;
		_sysMessageId = messageId;
		_time = time;
		_text_align = text_align;
		_big_font = big_font;
		_effect = showEffect;
		_clientMessageId = clientMsgId;
	}
	
	public Text add(String text)
	{
		_text.add(text);
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x39);
		
		writeD(_hide ? 0x00 : 0x01);
		if (_hide)
		{
			return;
		}
		
		writeD(_sysMessageId);
		writeD(_text_align.ordinal() + 1);
		writeD(0x00);
		writeD(_big_font ? 0x00 : 0x01);
		writeD(0x00);
		writeD(0x00);
		writeD(_effect ? 1 : 0);
		writeD(_time);
		writeD(0x00);
		writeD(_clientMessageId);
		for (String text : _text)
		{
			writeS(text);
		}
	}
}