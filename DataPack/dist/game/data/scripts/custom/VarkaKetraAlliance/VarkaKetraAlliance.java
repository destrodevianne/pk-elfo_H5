/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package custom.VarkaKetraAlliance;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.util.Util;
import ai.npc.AbstractNpcAI;

public class VarkaKetraAlliance extends AbstractNpcAI
{
        protected static final int[] KETRA_ALLY_MARK = { 7211, 7212, 7213, 7214, 7215 };        
        protected static final int[] VARKA_ALLY_MARK = { 7225, 7224, 7223, 7222, 7221 };
        protected static final int[] MOBS = 
        { 
                // Ketra Monsters
                21324, 21325, 21327, 21328, 21329, 21331, 21332, 21334, 21335, 21336, 21338, 21339, 21340, 21342, 21343, 21344, 21345, 21346, 21347, 21348, 21349,
                // Varka Monsters
                21350, 21351, 21353, 21354, 21355, 21357, 21358, 21360, 21361, 21362, 21364, 21365, 21366, 21368, 21369, 21370, 21371, 21372, 21373, 21374, 21375
        };
        
        public VarkaKetraAlliance(String name, String descr)
        {
                super(name, descr);
                registerMobs(MOBS, QuestEventType.ON_ATTACK, QuestEventType.ON_KILL);
        }       
        
        @Override
        public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
        {
                if (attacker.getAllianceWithVarkaKetra() != 0)
                {
                        if ((attacker.isAlliedWithKetra() && npc.getFactionId() == "ketra") || (attacker.isAlliedWithVarka() && npc.getFactionId() == "varka"))
                        {
                                L2Skill skill = SkillTable.getInstance().getInfo(4578, 1);
                                if (skill != null)
                                {
                                        npc.setTarget(attacker);
                                        npc.doCast(skill);
                                }
                        }
                }
                return super.onAttack(npc, attacker, damage, isPet);
        }
        
        @Override
        public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
        {
                if (killer.getParty() != null)
                {
                        for (L2PcInstance member : killer.getParty().getMembers())
                        {
                                if (Util.checkIfInRange(6000, killer, member, true))
                                        decreaseAlly(npc, member);
                        }
                }
                else
                        decreaseAlly(npc, killer);
                
                return super.onKill(npc, killer, isPet);
        }
        
        private void decreaseAlly(L2Npc npc, L2PcInstance player)
        {
                if (player.getAllianceWithVarkaKetra() == 0)
                        return;

                L2ItemInstance mark = null;
                
                if (player.isAlliedWithKetra())
                {
                        if (npc.getFactionId() == "ketra")
                        {
                                final L2ItemInstance varkasBadgeSoldier = player.getInventory().getItemByItemId(7216);
                                final L2ItemInstance varkasBadgeOfficer = player.getInventory().getItemByItemId(7217);
                                final L2ItemInstance varkasBadgeCaptain = player.getInventory().getItemByItemId(7218);
                                final L2ItemInstance valorTotem = player.getInventory().getItemByItemId(7219);
                                final L2ItemInstance wisdomTotem = player.getInventory().getItemByItemId(7220);
                                
                                final long varkasBadgeSoldierCount = varkasBadgeSoldier == null ? 0 : varkasBadgeSoldier.getCount();
                                final long varkasBadgeOfficerCount = varkasBadgeOfficer == null ? 0 : varkasBadgeOfficer.getCount();
                                final long varkasBadgeCaptainCount = varkasBadgeCaptain == null ? 0 : varkasBadgeCaptain.getCount();
                                final long valorTotemCount = valorTotem == null ? 0 : valorTotem.getCount();
                                final long wisdomTotemCount = wisdomTotem == null ? 0 : wisdomTotem.getCount();
                                
                                if (varkasBadgeSoldierCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeSoldier", 7216, varkasBadgeSoldierCount, player, true);
                                if (varkasBadgeOfficerCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeOfficer", 7217, varkasBadgeOfficerCount, player, true);
                                if (varkasBadgeCaptainCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeCaptain", 7218, varkasBadgeCaptainCount, player, true);
                                if (valorTotemCount > 0)
                                        player.destroyItemByItemId("valorTotem", 7219, valorTotemCount, player, true);
                                if (wisdomTotemCount > 0)
                                        player.destroyItemByItemId("wisdomTotem", 7220, wisdomTotemCount, player, true);
                                
                                player.destroyItemByItemId("Mark", KETRA_ALLY_MARK[player.getAllianceWithVarkaKetra() - 1], 1, player, true);
                                player.setAllianceWithVarkaKetra(player.getAllianceWithVarkaKetra() - 1);
                                if (player.getAllianceWithVarkaKetra() != 0)
                                        mark = player.getInventory().addItem("Mark", KETRA_ALLY_MARK[player.getAllianceWithVarkaKetra() - 1], 1, player, player);
                                
                                final InventoryUpdate u = new InventoryUpdate();
                                u.addNewItem(mark);
                                player.sendPacket(u);
                        }
                }                       
                else
                {
                        if (npc.getFactionId() == "varka")
                        {
                                final L2ItemInstance ketrasBadgeSoldier = player.getInventory().getItemByItemId(7226);
                                final L2ItemInstance ketrasBadgeOfficer = player.getInventory().getItemByItemId(7227);
                                final L2ItemInstance ketrasBadgeCaptain = player.getInventory().getItemByItemId(7228);
                                final L2ItemInstance featherValor = player.getInventory().getItemByItemId(7229);
                                final L2ItemInstance featherWisdom = player.getInventory().getItemByItemId(7230);
                                
                                final long ketrasBadgeSoldierCount = ketrasBadgeSoldier == null ? 0 : ketrasBadgeSoldier.getCount();
                                final long ketrasBadgeOfficerCount = ketrasBadgeOfficer == null ? 0 : ketrasBadgeOfficer.getCount();
                                final long ketrasBadgeCaptainCount = ketrasBadgeCaptain == null ? 0 : ketrasBadgeCaptain.getCount();
                                final long featherValorCount = featherValor == null ? 0 : featherValor.getCount();
                                final long featherWisdomCount = featherWisdom == null ? 0 : featherWisdom.getCount();
                                
                                if (ketrasBadgeSoldierCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeSoldier", 7226, ketrasBadgeSoldierCount, player, true);
                                if (ketrasBadgeOfficerCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeOfficer", 7227, ketrasBadgeOfficerCount, player, true);
                                if (ketrasBadgeCaptainCount > 0)
                                        player.destroyItemByItemId("ketrasBadgeCaptain", 7228, ketrasBadgeCaptainCount, player, true);
                                if (featherValorCount > 0)
                                        player.destroyItemByItemId("featherValor", 7229, featherValorCount, player, true);
                                if (featherWisdomCount > 0)
                                        player.destroyItemByItemId("featherWisdom", 7230, featherWisdomCount, player, true);
                                
                                player.destroyItemByItemId("Mark", VARKA_ALLY_MARK[player.getAllianceWithVarkaKetra() + 5], 1, player, true);
                                player.setAllianceWithVarkaKetra(player.getAllianceWithVarkaKetra() + 1);
                                if (player.getAllianceWithVarkaKetra() != 0)
                                        mark = player.getInventory().addItem("Mark", VARKA_ALLY_MARK[player.getAllianceWithVarkaKetra() + 5], 1, player, player);
                                
                                final InventoryUpdate u = new InventoryUpdate();
                                u.addNewItem(mark);
                                player.sendPacket(u);
                        }
                }
        }
        
        public static void main(String[] args)
        {
                new VarkaKetraAlliance(VarkaKetraAlliance.class.getSimpleName(), "ai");
        }
}