package pk.elfo.gameserver.model.actor.instance;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.ai.L2CharacterAI;
import pk.elfo.gameserver.ai.L2ControllableMobAI;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;

/**
 * Projeto PkEfo
 */
 
public class L2ControllableMobInstance extends L2MonsterInstance
{
	private boolean _isInvul;
	private L2ControllableMobAI _aiBackup; // to save ai, avoiding beeing detached
	
	protected class ControllableAIAcessor extends AIAccessor
	{
		@Override
		public void detachAI()
		{
			// do nothing, AI of controllable mobs can't be detached automatically
		}
	}
	
	@Override
	public boolean isAggressive()
	{
		return true;
	}
	
	@Override
	public int getAggroRange()
	{
		// force mobs to be aggro
		return 500;
	}
	
	public L2ControllableMobInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2ControllableMobInstance);
	}
	
	@Override
	public L2CharacterAI getAI()
	{
		L2CharacterAI ai = _ai; // copy handle
		if (ai == null)
		{
			synchronized (this)
			{
				if ((_ai == null) && (_aiBackup == null))
				{
					_ai = new L2ControllableMobAI(new ControllableAIAcessor());
					_aiBackup = (L2ControllableMobAI) _ai;
				}
				else
				{
					_ai = _aiBackup;
				}
				return _ai;
			}
		}
		return ai;
	}
	
	@Override
	public boolean isInvul()
	{
		return _isInvul;
	}
	
	public void setInvul(boolean isInvul)
	{
		_isInvul = isInvul;
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		removeAI();
		return true;
	}
	
	@Override
	public void deleteMe()
	{
		removeAI();
		super.deleteMe();
	}
	
	/**
	 * Definitively remove AI
	 */
	protected void removeAI()
	{
		synchronized (this)
		{
			if (_aiBackup != null)
			{
				_aiBackup.setIntention(CtrlIntention.AI_INTENTION_IDLE);
				_aiBackup = null;
				_ai = null;
			}
		}
	}
}