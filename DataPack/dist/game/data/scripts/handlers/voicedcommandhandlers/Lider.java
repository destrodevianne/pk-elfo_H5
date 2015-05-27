package handlers.voicedcommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class Lider implements IVoicedCommandHandler
{
        private static final String[] VOICED_COMMANDS = 
        	{ 
        	"lider"
        	};
        
        @Override
        public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
        {
                
                if (Config.COMMAND_LIDER)
                {
                        
                        if (command.equalsIgnoreCase("lider") && activeChar.isVip())
                        {
                                
                                if (activeChar.getClan() == null)
                                {
                                        return false;
                                }
                                
                                L2PcInstance leader;
                                leader = (L2PcInstance) L2World.getInstance().findObject(activeChar.getClan().getLeaderId());
                                
                                if (leader == null)
                                {
                                        activeChar.sendMessage("Seu lider nao esta online.");
                                        return false;
                                }
                                else if (leader.isInJail())
                                {
                                        activeChar.sendMessage("Seu lider esta na Jaula.");
                                        return false;
                                }
                                else if (leader.isInOlympiadMode())
                                {
                                        activeChar.sendMessage("Seu lider esta nas Olympiadas.");
                                        return false;
                                }
                                else if (leader.isInDuel())
                                {
                                        activeChar.sendMessage("Seu lider esta em um Duelo.");
                                        return false;
                                }
                                else if (leader.isFestivalParticipant())
                                {
                                        activeChar.sendMessage("Seu lider esta em algum festival.");
                                        return false;
                                }
                                else if (activeChar.getKarma() > 0)
                                {
                                activeChar.sendMessage("Ha.. ha.. ha, Voce nao pode teleportar com Karma");
                                return false;
                                }
                                else if (leader.isInParty() && leader.getParty().isInDimensionalRift())
                                {
                                        activeChar.sendMessage("Seu lider esta em dimensional rift.");
                                        return false;
                                }
                                else if (leader.inObserverMode())
                                {
                                        activeChar.sendMessage("Seu lider esta em modo de Observacao.");
                                }
                                else if ((leader.getClan() != null) && (CastleManager.getInstance().getCastleByOwner(leader.getClan()) != null) && CastleManager.getInstance().getCastleByOwner(leader.getClan()).getSiege().getIsInProgress())
                                {
                                        activeChar.sendMessage("O seu lider esta em Siege, voce nao pode ir para o seu lider.");
                                        return false;
                                }
                                
                                else if (activeChar.isInJail())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando na Jaula!");
                                        return false;
                                }
                                else if (activeChar.isInOlympiadMode())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando nas Olympiadas.");
                                        return false;
                                }
                                else if (activeChar.isInDuel())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando em Duelo!");
                                        return false;
                                }
                                else if (activeChar.inObserverMode())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando em modo de Observacao.");
                                }
                                else if ((activeChar.getClan() != null) && (CastleManager.getInstance().getCastleByOwner(activeChar.getClan()) != null) && CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).getSiege().getIsInProgress())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando em Siege.");
                                        return false;
                                }
                                else if (activeChar.isFestivalParticipant())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando em festival.");
                                        return false;
                                }
                                else if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift())
                                {
                                        activeChar.sendMessage("Voce nao pode ir estando em dimensional rift.");
                                        return false;
                                }
                                else if (activeChar == leader())
                                {
                                        activeChar.sendMessage("Voce nao pode se teletransportar para si mesmo.");
                                        return false;
                                }
                                if (activeChar.getInventory().getItemByItemId(57) == null)
                                {
                                        activeChar.sendMessage("Voce precisa de 10 Milhoes de Adenas para se Teleportar ate seu lider.");
                                        return false;
                                }
                                int leaderx;
                                int leadery;
                                int leaderz;
                                
                                leaderx = leader.getX();
                                leadery = leader.getY();
                                leaderz = leader.getZ();
                                
                                activeChar.teleToLocation(leaderx, leadery, leaderz);
                                activeChar.sendMessage("Voce foi teletransportado para o seu lider!");
                                activeChar.getInventory().destroyItemByItemId("RessSystem", 57, 10000000, activeChar, activeChar.getTarget());
                                activeChar.sendMessage("10 Milhoes de Adenas foram usadas, Obrigado!");
                        }
                        
                        else
                        {
                                activeChar.sendMessage("Comando Desabilitado pelo Admin");
                        }
                        
                        return true;
                }
                return false;
        }
        
        public L2PcInstance leader()
        {
                return null;
        }
        
        @Override
        public String[] getVoicedCommandList()
        {
                return VOICED_COMMANDS;
        }
        
}