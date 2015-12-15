package handlers.bypasshandlers;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.SevenSigns;
import pk.elfo.gameserver.SevenSignsFestival;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.L2Party.messageType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2FestivalGuideInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.StringUtil;

/**
 * Projeto PkElfo
 */

public class Festival implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"festival",
		"festivaldesc"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2FestivalGuideInstance))
		{
			return false;
		}
		
		final L2FestivalGuideInstance npc = (L2FestivalGuideInstance) target;
		try
		{
			final int val;
			if (command.toLowerCase().startsWith(COMMANDS[1]))
			{
				val = Integer.parseInt(command.substring(13));
				npc.showChatWindow(activeChar, val, null, true);
				return true;
			}
			
			final L2Party party;
			val = Integer.parseInt(command.substring(9, 10));
			switch (val)
			{
				case 1: // Become a Participant
					// Check if the festival period is active, if not then don't allow registration.
					if (SevenSigns.getInstance().isSealValidationPeriod())
					{
						npc.showChatWindow(activeChar, 2, "a", false);
						return true;
					}
					
					// Check if a festival is in progress, then don't allow registration yet.
					if (SevenSignsFestival.getInstance().isFestivalInitialized())
					{
						activeChar.sendMessage("Voce nao pode se inscrever enquanto um festival estiver em andamento.");
						return true;
					}
					
					// Check if the player is in a formed party already.
					if (!activeChar.isInParty())
					{
						npc.showChatWindow(activeChar, 2, "b", false);
						return true;
					}
					
					party = activeChar.getParty();
					
					// Check if the player is the party leader.
					if (!party.isLeader(activeChar))
					{
						npc.showChatWindow(activeChar, 2, "c", false);
						return true;
					}
					
					// Check to see if the party has at least 5 members.
					if (party.getMemberCount() < Config.ALT_FESTIVAL_MIN_PLAYER)
					{
						npc.showChatWindow(activeChar, 2, "b", false);
						return true;
					}
					
					// Check if all the party members are in the required level range.
					if (party.getLevel() > SevenSignsFestival.getMaxLevelForFestival(npc.getFestivalType()))
					{
						npc.showChatWindow(activeChar, 2, "d", false);
						return true;
					}
					
					// Check to see if the player has already signed up
					if (activeChar.isFestivalParticipant())
					{
						SevenSignsFestival.getInstance().setParticipants(npc.getFestivalOracle(), npc.getFestivalType(), party);
						npc.showChatWindow(activeChar, 2, "f", false);
						return true;
					}
					
					npc.showChatWindow(activeChar, 1, null, false);
					break;
				case 2: // Seal Stones
					final int stoneType = Integer.parseInt(command.substring(11));
					final int stoneCount = npc.getStoneCount(stoneType);
					if (stoneCount <= 0)
					{
						return false;
					}
					
					if (!activeChar.destroyItemByItemId("SevenSigns", stoneType, stoneCount, npc, true))
					{
						return false;
					}
					
					SevenSignsFestival.getInstance().setParticipants(npc.getFestivalOracle(), npc.getFestivalType(), activeChar.getParty());
					SevenSignsFestival.getInstance().addAccumulatedBonus(npc.getFestivalType(), stoneType, stoneCount);
					
					npc.showChatWindow(activeChar, 2, "e", false);
					break;
				case 3: // Score Registration
					// Check if the festival period is active, if not then don't register the score.
					if (SevenSigns.getInstance().isSealValidationPeriod())
					{
						npc.showChatWindow(activeChar, 3, "a", false);
						return true;
					}
					
					// Check if a festival is in progress, if it is don't register the score.
					if (SevenSignsFestival.getInstance().isFestivalInProgress())
					{
						activeChar.sendMessage("Voce nao pode registrar uma pontuacao enquanto um festival estiver em andamento.");
						return true;
					}
					
					// Check if the player is in a party.
					if (!activeChar.isInParty())
					{
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					final List<Integer> prevParticipants = SevenSignsFestival.getInstance().getPreviousParticipants(npc.getFestivalOracle(), npc.getFestivalType());
					
					// Check if there are any past participants.
					if ((prevParticipants == null) || prevParticipants.isEmpty() || !prevParticipants.contains(activeChar.getObjectId()))
					{
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					// Check if this player was the party leader in the festival.
					if (activeChar.getObjectId() != prevParticipants.get(0))
					{
						npc.showChatWindow(activeChar, 3, "b", false);
						return true;
					}
					
					final L2ItemInstance bloodOfferings = activeChar.getInventory().getItemByItemId(SevenSignsFestival.FESTIVAL_OFFERING_ID);
					
					// Check if the player collected any blood offerings during the festival.
					if (bloodOfferings == null)
					{
						activeChar.sendMessage("Voce nao tem qualquer oferta de sangue para contribuir.");
						return true;
					}
					
					final long offeringScore = bloodOfferings.getCount() * SevenSignsFestival.FESTIVAL_OFFERING_VALUE;
					if (!activeChar.destroyItem("SevenSigns", bloodOfferings, npc, false))
					{
						return true;
					}
					
					final boolean isHighestScore = SevenSignsFestival.getInstance().setFinalScore(activeChar, npc.getFestivalOracle(), npc.getFestivalType(), offeringScore);
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CONTRIB_SCORE_INCREASED_S1);
					sm.addItemNumber(offeringScore);
					activeChar.sendPacket(sm);
					
					if (isHighestScore)
					{
						npc.showChatWindow(activeChar, 3, "c", false);
					}
					else
					{
						npc.showChatWindow(activeChar, 3, "d", false);
					}
					break;
				case 4: // Current High Scores
					final StringBuilder strBuffer = StringUtil.startAppend(500, "<html><body>Guia do Festival:<br>Estes sao os melhores resultados da semana, para a ");
					
					final StatsSet dawnData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DAWN, npc.getFestivalType());
					final StatsSet duskData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DUSK, npc.getFestivalType());
					final StatsSet overallData = SevenSignsFestival.getInstance().getOverallHighestScoreData(npc.getFestivalType());
					
					final int dawnScore = dawnData.getInteger("score");
					final int duskScore = duskData.getInteger("score");
					int overallScore = 0;
					
					// If no data is returned, assume there is no record, or all scores are 0.
					if (overallData != null)
					{
						overallScore = overallData.getInteger("score");
					}
					
					StringUtil.append(strBuffer, SevenSignsFestival.getFestivalName(npc.getFestivalType()), " festival.<br>");
					
					if (dawnScore > 0)
					{
						StringUtil.append(strBuffer, "Dawn: ", calculateDate(dawnData.getString("date")), ". Score ", String.valueOf(dawnScore), "<br>", dawnData.getString("members"), "<br>");
					}
					else
					{
						strBuffer.append("Dawn: Nao existe nenhum registro. Score 0<br>");
					}
					
					if (duskScore > 0)
					{
						StringUtil.append(strBuffer, "Dusk: ", calculateDate(duskData.getString("date")), ". Score ", String.valueOf(duskScore), "<br>", duskData.getString("members"), "<br>");
					}
					else
					{
						strBuffer.append("Dusk: Nao existe nenhum registro. Score 0<br>");
					}
					
					if ((overallScore > 0) && (overallData != null))
					{
						final String cabalStr;
						if (overallData.getString("cabal").equals("dawn"))
						{
							cabalStr = "Children of Dawn";
						}
						else
						{
							cabalStr = "Children of Dusk";
						}
						
						StringUtil.append(strBuffer, "Melhores pontuacoes consecutivas: ", calculateDate(overallData.getString("date")), ". Score ", String.valueOf(overallScore), "<br>lado filiado: ", cabalStr, "<br>", overallData.getString("members"), "<br>");
					}
					else
					{
						strBuffer.append("Melhores pontuacoes consecutivas: Nao existe recorde. Score 0<br>");
					}
					
					StringUtil.append(strBuffer, "<a action=\"bypass -h npc_", String.valueOf(npc.getObjectId()), "_Chat 0\">Voltar.</a></body></html>");
					
					NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
					html.setHtml(strBuffer.toString());
					activeChar.sendPacket(html);
					break;
				case 8: // Increase the Festival Challenge
					if (!activeChar.isInParty())
					{
						return true;
					}
					
					if (!SevenSignsFestival.getInstance().isFestivalInProgress())
					{
						return true;
					}
					
					party = activeChar.getParty();
					if (!party.isLeader(activeChar))
					{
						npc.showChatWindow(activeChar, 8, "a", false);
						return true;
					}
					
					if (SevenSignsFestival.getInstance().increaseChallenge(npc.getFestivalOracle(), npc.getFestivalType()))
					{
						npc.showChatWindow(activeChar, 8, "b", false);
					}
					else
					{
						npc.showChatWindow(activeChar, 8, "c", false);
					}
					break;
				case 9: // Leave the Festival
					if (!activeChar.isInParty())
					{
						return true;
					}
					
					party = activeChar.getParty();
					if (party.isLeader(activeChar))
					{
						SevenSignsFestival.getInstance().updateParticipants(activeChar, null);
					}
					else
					{
						if (party.getMemberCount() > Config.ALT_FESTIVAL_MIN_PLAYER)
						{
							party.removePartyMember(activeChar, messageType.Expelled);
						}
						else
						{
							activeChar.sendMessage("Apenas o lider da party pode deixar o festival quando uma party tiver o numero mínimo de membros.");
						}
					}
					break;
				case 0: // Distribute Accumulated Bonus
					if (!SevenSigns.getInstance().isSealValidationPeriod())
					{
						activeChar.sendMessage("Os bonus nao podem ser pagos durante o periodo de competicao.");
						return true;
					}
					
					if (SevenSignsFestival.getInstance().distribAccumulatedBonus(activeChar) > 0)
					{
						npc.showChatWindow(activeChar, 0, "a", false);
					}
					else
					{
						npc.showChatWindow(activeChar, 0, "b", false);
					}
					break;
				default:
					npc.showChatWindow(activeChar, val, null, false);
			}
			return true;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	private final String calculateDate(String milliFromEpoch)
	{
		long numMillis = Long.valueOf(milliFromEpoch);
		Calendar calCalc = Calendar.getInstance();
		calCalc.setTimeInMillis(numMillis);
		return calCalc.get(Calendar.YEAR) + "/" + calCalc.get(Calendar.MONTH) + "/" + calCalc.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}