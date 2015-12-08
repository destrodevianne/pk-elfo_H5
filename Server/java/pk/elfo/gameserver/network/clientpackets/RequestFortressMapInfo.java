package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.network.serverpackets.ExShowFortressMapInfo;

/**
 * Projeto PkElfo
 */
 
public class RequestFortressMapInfo extends L2GameClientPacket
{
    private static final String _C_D0_48_REQUESTFORTRESSMAPINFO = "[C] D0:48 RequestFortressMapInfo";
    private int _fortressId;

    @Override
    protected void readImpl()
    {
        _fortressId = readD();
    }

    @Override
    protected void runImpl()
    {
        Fort fort = FortManager.getInstance().getFortById(_fortressId);
        sendPacket(new ExShowFortressMapInfo(fort));
    }

    @Override
    public String getType()
    {
        return _C_D0_48_REQUESTFORTRESSMAPINFO;
    }
}