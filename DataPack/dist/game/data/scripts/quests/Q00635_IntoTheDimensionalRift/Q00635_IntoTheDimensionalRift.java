package quests.Q00635_IntoTheDimensionalRift;

import pk.elfo.gameserver.model.quest.Quest;

public class Q00635_IntoTheDimensionalRift extends Quest
{
	private Q00635_IntoTheDimensionalRift(int questId, String name, String descr)
	{
		super(questId, name, descr);
	}
	
	public static void main(String[] args)
	{
		new Q00635_IntoTheDimensionalRift(635, Q00635_IntoTheDimensionalRift.class.getSimpleName(), "Into the Dimensional Rift");
	}
}