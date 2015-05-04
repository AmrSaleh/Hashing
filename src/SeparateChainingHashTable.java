import java.lang.reflect.Array;
import java.util.LinkedList;

public class SeparateChainingHashTable<K, V> extends AbstractHashTable<K, V> {

	@SuppressWarnings("unchecked")
	private LinkedList<Pair<K, V>>[] chains = (LinkedList<Pair<K, V>>[]) Array.newInstance(LinkedList.class, currentSize);

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		int pos = h(key);
		if (storedKeys.contains(key)) {
			//just adjust the value of element
			changeValue(pos,key,value);
			return;
		}
		// check if the position is empty or contains tombstone
		if (HT[pos] == null || HT[pos].isTombstone()) {
			HT[pos] = new Pair<K, V>(key, value);
			storedKeys.add(key);
			numOfElements++;
			checkLoadingFactorAndDouble();
			return;
		}
//		System.out.println("collision");
		Tester.testCollisions++;
		// No chain ... create one and put it in
		if (chains[pos] == null) {
			chains[pos] = new LinkedList<Pair<K, V>>();
			chains[pos].add(new Pair<K, V>(key, value));
			storedKeys.add(key);
			numOfElements++;
			checkLoadingFactorAndDouble();
			return;
		}
		// loop on in chain and put it in a tombstone if found
		for (int i = 0; i < chains[pos].size(); i++) {
			if (chains[pos].get(i).isTombstone()) {
				chains[pos].get(i).setKey(key);
				chains[pos].get(i).setValue(value);
				chains[pos].get(i).setTombstone(false);
				storedKeys.add(key);
				numOfElements++;
				checkLoadingFactorAndDouble();
				return;

			}
		}
		// add new pair to the chain
		chains[pos].add(new Pair<K, V>(key, value));
		storedKeys.add(key);
		numOfElements++;
		checkLoadingFactorAndDouble();
		return;

	}

	private void changeValue(int pos, K key, V value) {
		// TODO Auto-generated method stub
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			HT[pos].setKey(key);
			HT[pos].setValue(value);
			return;
		}
		for (int i = 0; i < chains[pos].size(); i++) {
			if (chains[pos].get(i).getKey().equals(key) && !HT[pos].isTombstone()) {
				chains[pos].get(i).setKey(key);
				chains[pos].get(i).setValue(value);
				return;
			}
		}
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		if (!storedKeys.contains(key))
			return null;

		int pos = h(key);
//		// check if the main position is empty
//		if (HT[pos] == null)
//			return null;
		// if main pos contains key return it
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone())
			return HT[pos].getValue();
		// loop through chain and if found return it

		for (int i = 0; i < chains[pos].size(); i++) {
			if (!chains[pos].get(i).isTombstone() && chains[pos].get(i).getKey().equals(key))
				return chains[pos].get(i).getValue();
		}
		// not found return null
		return null;
	}

	@Override
	public void delete(K key) {
		// TODO Auto-generated method stub
		// check if key present in the hash table
		if (!storedKeys.contains(key))
			return;

		int pos = h(key);
		// check the pos if it contains the key then tombstone it
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			// HT[pos].setKey(null);
			// HT[pos].setValue(null);
			HT[pos].setTombstone(true);
			storedKeys.remove(key);
			numOfElements--;
			checkLoadingFactorAndShrink();
			return;
		}
		// check through the chain and when found tombstone it
		for (int i = 0; i < chains[pos].size(); i++) {
			if (chains[pos].get(i).getKey().equals(key) && !chains[pos].get(i).isTombstone()) {
				// chains[pos].get(i).setKey(null);
				// chains[pos].get(i).setValue(null);
				chains[pos].get(i).setTombstone(true);
				storedKeys.remove(key);
				numOfElements--;
				checkLoadingFactorAndShrink();
				return;
			}
		}
	}

	// checks the loading factor and resizes the hash table accordingly
	@Override
	protected void checkLoadingFactorAndDouble() {
		// TODO Auto-generated method stub
		if (((float) numOfElements / currentSize) >= 3) {
			// double the hash table
			System.out.println("elements now " + numOfElements);
			System.out.println("size " + currentSize + " doubled to " + currentSize * 2);
			System.out.println();
			setSizeAndRehash(currentSize * 2);

		}
	}
	@Override
	protected void checkLoadingFactorAndShrink() {
		// TODO Auto-generated method stub
		if (((float) numOfElements / currentSize) <= 0.2 && currentSize > 8) {
			// remove half the hash table
			System.out.println("elements now " + numOfElements);
			System.out.println("size " + currentSize + " shrinked to " + currentSize / 2);
			System.out.println();
			setSizeAndRehash(currentSize / 2);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setSizeAtStartOfUsage(int newSize) {
		// TODO Auto-generated method stub
		super.setSizeAtStartOfUsage(newSize);
		chains = (LinkedList<Pair<K, V>>[]) Array.newInstance(LinkedList.class, newSize);

	}

	@Override
	protected void setSizeAndRehash(int newSize) {
		// TODO Auto-generated method stub
		// make a new hash and set it's new size
		SeparateChainingHashTable<K, V> newHash = new SeparateChainingHashTable<K, V>();
		newHash.setSizeAtStartOfUsage(newSize);
		// rehash all elements in the new hash
		
//		int temp =Tester.testCollisions;
		for (int i = 0; i < storedKeys.size(); i++) {
			newHash.put(storedKeys.get(i), get(storedKeys.get(i)));
		}
//		Tester.testCollisions=temp;
		// adjust our hash with the new hash lists and hash table
		HT = newHash.HT;
		chains = newHash.chains;
		currentSize = newSize;

	}

}
