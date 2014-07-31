package pk.elfo.gameserver.masteriopack.rankpvpsystem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class contains all ranks. Ranks starts from lowest rank with ID = 1.
 * PkElfo
 */
public class RankTable 
{
	private static RankTable _instance = null;
	
	// [rankId, Rank] - store all Ranks as Rank objects by rank id.
	private static Map<Integer, Rank> _rankList = new LinkedHashMap<>();

	public static RankTable getInstance()
	{
		if(_instance == null)
			_instance = new RankTable();
		
		return _instance;
	}
	
	/**
	 * @return Map &lt;rankID, Rank&gt;
	 */
	public Map<Integer, Rank> getRankList()
	{
		return _rankList;
	}

	public void setRankList(Map<Integer, Rank> rankList)
	{
		_rankList = rankList;
	}
	
	/**
	 * Returns Rank object by rank id, if not founded returns null.
	 * @param id
	 * @return
	 */
	public Rank getRankById(int id)
	{
		return _rankList.get(id);
	}

}
