public class LinearProbeHash<K, V> extends AbstractHashTable<K, V> {

	private int ProbeStep = 1;

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		int pos = h(key);
		if (storedKeys.contains(key)) {
			// just adjust the value of it
			changeValue(pos, key, value);
			return;
		}

		// check if the position is empty or tombstone
		if (putInSlot(pos, key, value)) {
			storedKeys.add(key);
			numOfElements++;
			checkLoadingFactorAndDouble();
			return;
		}
//		System.out.println("collision");
		Tester.testCollisions++;
		for (int i = (pos + ProbeStep) % currentSize; i != pos; i = (i + ProbeStep) % currentSize) {

			if (putInSlot(i, key, value)) {
				storedKeys.add(key);
				numOfElements++;
				checkLoadingFactorAndDouble();
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
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			
			return HT[pos].getValue();
		}

		for (int i = (pos + ProbeStep) % currentSize; i != pos; i = (i + ProbeStep) % currentSize) {

			if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
				return HT[i].getValue();
			}
		}
		
		return null;
		
	}
	
	@Override
	public void delete(K key) {
		// TODO Auto-generated method stub
		if (!storedKeys.contains(key))
			return;
		
		int pos = h(key);
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			HT[pos].setTombstone(true);
			storedKeys.remove(key);
			numOfElements--;
			checkLoadingFactorAndShrink();
			return;
		}

		for (int i = (pos + ProbeStep) % currentSize; i != pos; i = (i + ProbeStep) % currentSize) {

			if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
				HT[i].setTombstone(true);
				storedKeys.remove(key);
				numOfElements--;
				checkLoadingFactorAndShrink();
				return;
			}
		}
		
		return;
	}

	private void changeValue(int pos, K key, V value) {
		// TODO Auto-generated method stub
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			HT[pos].setKey(key);
			HT[pos].setValue(value);
			return;
		}

		for (int i = (pos + ProbeStep) % currentSize; i != pos; i = (i + ProbeStep) % currentSize) {

			if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
				HT[i].setKey(key);
				HT[i].setValue(value);
				return;
			}
		}
	}

	@Override
	protected void setSizeAndRehash(int newSize) {
		// TODO Auto-generated method stub
		// make a new hash and set it's new size
		LinearProbeHash<K, V> newHash = new LinearProbeHash<K, V>();
		newHash.setSizeAtStartOfUsage(newSize);
		// rehash all elements in the new hash
		for (int i = 0; i < storedKeys.size(); i++) {
			newHash.put(storedKeys.get(i), get(storedKeys.get(i)));
		}
		// adjust our hash with the new hash lists and hash table
		HT = newHash.HT;
		currentSize = newSize;
	}
	
	
}
