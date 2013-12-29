package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.datatables.ProductItemTable;
import pk.elfo.gameserver.model.L2ProductItem;
import pk.elfo.gameserver.model.L2ProductItemComponent;

/**
 * Author: VISTALL Company: J Develop Station Date: 2:50:11/10.04.2010
 */
public class ExBrProductInfo extends L2GameServerPacket
{
	private static final String TYPE = "[S] FE:D7 ExBrProductInfo";
	
	private final L2ProductItem _productId;
	
	public ExBrProductInfo(int id)
	{
		_productId = ProductItemTable.getInstance().getProduct(id);
	}
	
	@Override
	protected void writeImpl()
	{
		if (_productId == null)
		{
			return;
		}
		
		writeC(0xFE);
		writeH(0xD7);
		
		writeD(_productId.getProductId()); // product id
		writeD(_productId.getPoints()); // points
		writeD(_productId.getComponents().size()); // size
		
		for (L2ProductItemComponent com : _productId.getComponents())
		{
			writeD(com.getItemId()); // item id
			writeD(com.getCount()); // quality
			writeD(com.getWeight()); // weight
			writeD(com.isDropable() ? 1 : 0); // 0 - dont drop/trade
		}
	}
	
	public String getType()
	{
		return TYPE;
	}
}
