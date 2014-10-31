package pk.elfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

public class UPnPService
{
    private static final Logger _log = Logger.getLogger(UPnPService.class.getName());
    private static final String PROTOCOL = "TCP";

    private final GatewayDiscover _gatewayDiscover = new GatewayDiscover();
    private GatewayDevice _activeGW;

    protected UPnPService()
    {
        try
        {
            load();
        }
        catch (Exception e)
        {
            _log.log(Level.WARNING, getClass().getSimpleName() + ": erro durante a inicializacao: ", e);
        }
    }

    private void load() throws Exception
    {
        if (!Config.ENABLE_UPNP)
        {
            _log.log(Level.WARNING, "UPnP Service esta desabilitado.");
            return;
        }

        _log.log(Level.INFO, "Procurando por UPnP dispositivos de gateway...");

        final Map<InetAddress, GatewayDevice> gateways = _gatewayDiscover.discover();
        if (gateways.isEmpty())
        {
            _log.log(Level.INFO, "Nao gateways UPnP encontrados");
            return;
        }

        // choose the first active gateway for the tests
        _activeGW = _gatewayDiscover.getValidGateway();
        if (_activeGW != null)
        {
            _log.log(Level.INFO, "Usando o gateway UPnP: " + _activeGW.getFriendlyName());
        }
        else
        {
            _log.log(Level.INFO, "No portal UPnP ativo encontrado");
            return;
        }

        _log.log(Level.INFO, "Usando endereco local: " + _activeGW.getLocalAddress().getHostAddress() + " endereco externo: " + _activeGW.getExternalIPAddress());

        if (Server.serverMode == Server.MODE_GAMESERVER)
        {
            addPortMapping(Config.PORT_GAME, "L2PkElfo Game Server");
        }
        else if (Server.serverMode == Server.MODE_LOGINSERVER)
        {
            addPortMapping(Config.PORT_LOGIN, "L2PkElfo Login Server");
        }
    }

    public void removeAllPorts() throws Exception
    {
        if (_activeGW != null)
        {
            if (Server.serverMode == Server.MODE_GAMESERVER)
            {
                deletePortMapping(Config.PORT_GAME);
            }
            else if (Server.serverMode == Server.MODE_LOGINSERVER)
            {
                deletePortMapping(Config.PORT_LOGIN);
            }
        }
    }

    private void addPortMapping(int port, String description) throws IOException, SAXException
    {
        final PortMappingEntry portMapping = new PortMappingEntry();
        final InetAddress localAddress = _activeGW.getLocalAddress();

        // Attempt to re-map
        if (_activeGW.getSpecificPortMappingEntry(port, PROTOCOL, portMapping))
        {
            _activeGW.deletePortMapping(port, PROTOCOL);
        }

        if (_activeGW.addPortMapping(port, port, localAddress.getHostAddress(), PROTOCOL, description))
        {
            _log.log(Level.INFO, "Mapeamento bem sucedido em [" + localAddress.getHostAddress() + ":" + port + "]");
        }
        else
        {
            _log.log(Level.INFO, "Mapeamento falhou em [" + localAddress.getHostAddress() + ":" + port + "] - ja mapeados?");
        }
    }

    private void deletePortMapping(int port) throws IOException, SAXException
    {
        if (_activeGW.deletePortMapping(port, PROTOCOL))
        {
            _log.log(Level.INFO, "O mapeamento foi excluido do [" + _activeGW.getLocalAddress().getHostAddress() + ":" + port + "]");
        }
    }

    public static UPnPService getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final UPnPService _instance = new UPnPService();
    }
}