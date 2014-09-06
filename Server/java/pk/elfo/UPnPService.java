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
            _log.log(Level.WARNING, getClass().getSimpleName() + ": error while initializing: ", e);
        }
    }

    private void load() throws Exception
    {
        if (!Config.ENABLE_UPNP)
        {
            _log.log(Level.WARNING, "UPnP Service esta desabilitado.");
            return;
        }

        _log.log(Level.INFO, "Looking for UPnP Gateway Devices...");

        final Map<InetAddress, GatewayDevice> gateways = _gatewayDiscover.discover();
        if (gateways.isEmpty())
        {
            _log.log(Level.INFO, "No UPnP gateways found");
            return;
        }

        // choose the first active gateway for the tests
        _activeGW = _gatewayDiscover.getValidGateway();
        if (_activeGW != null)
        {
            _log.log(Level.INFO, "Using UPnP gateway: " + _activeGW.getFriendlyName());
        }
        else
        {
            _log.log(Level.INFO, "No active UPnP gateway found");
            return;
        }

        _log.log(Level.INFO, "Using local address: " + _activeGW.getLocalAddress().getHostAddress() + " External address: " + _activeGW.getExternalIPAddress());

        if (Server.serverMode == Server.MODE_GAMESERVER)
        {
            addPortMapping(Config.PORT_GAME, "L2j Game Server");
        }
        else if (Server.serverMode == Server.MODE_LOGINSERVER)
        {
            addPortMapping(Config.PORT_LOGIN, "L2j Login Server");
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
            _log.log(Level.INFO, "Mapping successfull on [" + localAddress.getHostAddress() + ":" + port + "]");
        }
        else
        {
            _log.log(Level.INFO, "Mapping failed on [" + localAddress.getHostAddress() + ":" + port + "] - Already mapped?");
        }
    }

    private void deletePortMapping(int port) throws IOException, SAXException
    {
        if (_activeGW.deletePortMapping(port, PROTOCOL))
        {
            _log.log(Level.INFO, "Mapping was deleted from [" + _activeGW.getLocalAddress().getHostAddress() + ":" + port + "]");
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