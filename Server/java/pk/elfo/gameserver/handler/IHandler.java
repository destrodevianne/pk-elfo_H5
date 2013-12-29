package pk.elfo.gameserver.handler;

public interface IHandler<K, V>
{
	public void registerHandler(K handler);
	
	public void removeHandler(K handler);
	
	public K getHandler(V val);
	
	public int size();
}