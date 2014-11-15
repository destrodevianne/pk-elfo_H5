package pk.elfo.gameserver.network.serverpackets;

import java.util.Collection;

import pk.elfo.gameserver.datatables.ProductItemTable;
import pk.elfo.gameserver.model.L2ProductItem;

public class ExBrProductList extends L2GameServerPacket
{
	private static final String TYPE = "[S] FE:D6 ExBrProductList";
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xD6);
		Collection<L2ProductItem> items = ProductItemTable.getInstance().getAllItems();
		writeD(items.size());
		
		for (L2ProductItem template : items)
		{
			writeD(template.getProductId());
			writeH(template.getCategory());
			writeH(template.getPoints());
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0);
			writeH(0);
			writeH(0);
			writeH(0);
			writeC(0);
			writeH(0);
		}
	}
	
	public String getType()
	{
		return TYPE;
	}
}