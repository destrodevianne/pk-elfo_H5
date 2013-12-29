import sys
from java.util import Iterator
from pk.elfo import L2DatabaseFactory
from pk.elfo.gameserver.model.quest import State
from pk.elfo.gameserver.model.quest import QuestState
from pk.elfo.gameserver.model.quest.jython import QuestJython as JQuest
 
qn = "50300_aio"
 
NPC         = 80001
QuestId     = 50300
MIN_LEVEL   = 0
MAX_LEVEL   = 86
QuestName   = "aio"
QuestDesc   = "custom"
InitialHtml = "1.htm"
DonateMaster  = "This ain't no free service nubblet! Now get out of my hare... Get it, 'hare'? - That gets me every time!"
 
 
class Quest (JQuest) :
 
	def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)
 
	def onEvent(self,event,st):
		htmltext = event
		level = st.getPlayer().getLevel()
		levelup = 86 - level
		if level < MIN_LEVEL :
			return"<html><head><body>No quicky for you! - Your to young shorty!</body></html>"
		if level > MAX_LEVEL :
		    return"<html><head><body>No quicky for you! - Your to old fatty!</body></html>"
 
		else:
            #AIO INFO
			if event == "01":
			 con = L2DatabaseFactory.getInstance().getConnection()
			 total_asesinados = 0
			 htmltext_ini = "<html><head><title>Lineage II PkElfo - AIOx</title></head><body><table width=300><tr><td><font color =\"FFFF00\">Rank</td><td><center><font color =\"FFFF00\">Nome do player</color></center></td><td><center> AIOx</center></td></tr><td><center>Fim do Status AIOx</center></td></tr>"
			 htmltext_info =""
			 color = 1
			 pos = 0
			 aio = con.prepareStatement("SELECT char_name,aio, aio_end FROM characters WHERE aio>0 and accesslevel=0 order by aio desc limit 200")
			 rs = aio.executeQuery()
			 while (rs.next()) :
			   char_name = rs.getString("char_name")
			   char_aio = rs.getString("aio")
			   char_aio_end = rs.getString("aio_end")
			   pos = pos + 1
			   posstr = str(pos)
			   if color == 1:
			      color_text = "<font color =\"FFFFFF\">"
			      color = 2
			      htmltext_info = htmltext_info + "<tr><td><center><font color =\"FFFFFF\">" + posstr + "</td><td><center>" + color_text + char_name +"</center></td><td><center>" + char_aio + "</center></td></tr><td><center>" + char_aio_end + "</center></td></tr>"
			   elif color == 2:
			      color_text = "<font color =\"FFFFFF\">"
			      color = 1
			      htmltext_info = htmltext_info + "<tr><td><center><font color =\"FFFFFF\">" + posstr + "</td><td><center>" + color_text + char_name +"</center></td><td><center>" + char_aio + "</center></td></tr><td><center>" + char_aio_end + "</center></td></tr>"
			 htmltext_end = "</table><center><font color=\"FFFF00\">" + "Lineage II PkElfo""</center></body></html>"
			 htmltext_aio = htmltext_ini + htmltext_info + htmltext_end
			 L2DatabaseFactory.close(con)
			 return htmltext_aio

			if htmltext != event:
 
				st.exitQuest(1)
 
		return htmltext
  
	def onFirstTalk (self,npc,player):
 
	   st = player.getQuestState(qn)
 
	   if not st : st = self.newQuestState(player)

	   return InitialHtml

QUEST = Quest(QuestId,str(QuestId) + "_" + QuestName,QuestDesc)
QUEST.addStartNpc(NPC)
QUEST.addFirstTalkId(NPC)
QUEST.addTalkId(NPC)