import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Amr
 * 
 */
public class AbstractHashTable<K, V> implements HashTable<K, V> {

	protected int currentSize = 8;
	protected int numOfElements = 0;

	@SuppressWarnings("unchecked")
	protected Pair<K, V>[] HT = (Pair<K, V>[]) Array.newInstance(new Pair<K, V>().getClass(), currentSize);

	protected ArrayList<K> storedKeys = new ArrayList<K>();

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub

	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(K key) {
		// TODO Auto-generated method stub
		// System.out.println("delete not implemented in abstract class");
	}

	@Override
	public boolean contains(K key) {
		// TODO Auto-generated method stub

		if (storedKeys.contains(key)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (numOfElements == 0)
			return true;
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return numOfElements;
	}

	@Override
	public Iterable<K> keys() {
		// TODO Auto-generated method stub

		return storedKeys;
	}

	protected int h(K key) {
		return (Math.abs(key.hashCode() % (currentSize)));
	}

	@SuppressWarnings("unchecked")
	protected void setSizeAtStartOfUsage(int newSize) {
		currentSize = newSize;
		HT = (Pair<K, V>[]) Array.newInstance(new Pair<K, V>().getClass(), newSize);
	}

	protected boolean putInSlot(int pos, K key, V value) {
		// check if the position is empty or tombstone
		if (HT[pos] == null || HT[pos].isTombstone()) {
			HT[pos] = new Pair<K, V>(key, value);
			return true;
		}

		return false;
	}

	protected void checkLoadingFactorAndDouble() {
		// TODO Auto-generated method stub
		float z = (float) (numOfElements) / (currentSize);
		if (z >= 0.75) {
			// double the hash table
			System.out.println("elements now " + numOfElements);
			System.out.println("size " + currentSize + " doubled to " + currentSize * 2);
			System.out.println();
			setSizeAndRehash(currentSize * 2);
		}
	}

	protected void checkLoadingFactorAndShrink(){
		if (((float) numOfElements / currentSize) <= 0.2 && currentSize > 8) {
			// remove half the hash table
			System.out.println("elements now " + numOfElements);
			System.out.println("size " + currentSize + " shrinked to " + currentSize / 2);
			System.out.println();
			setSizeAndRehash(currentSize / 2);

		}
	}
	protected void setSizeAndRehash(int i) {
		// TODO Auto-generated method stub

	}

}
