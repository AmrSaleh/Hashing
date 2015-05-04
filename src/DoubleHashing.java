public class DoubleHashing<K, V> extends AbstractHashTable<K, V> {

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
		// System.out.println("collision");
		Tester.testCollisions++;
		int newPos;
		for (int i = 1; (pos + probe(key, i)) % currentSize != pos; i++) {
			newPos = (pos + probe(key, i)) % currentSize;
			if (putInSlot(newPos, key, value)) {
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

		int newPos;
		for (int i = 1; (pos + probe(key, i)) % currentSize != pos; i++) {
			newPos = (pos + probe(key, i)) % currentSize;
			if (HT[newPos].getKey().equals(key) && !HT[newPos].isTombstone()) {
				return HT[newPos].getValue();
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

		int newPos;
		for (int i = 1; (pos + probe(key, i)) % currentSize != pos; i++) {
			newPos = (pos + probe(key, i)) % currentSize;
			if (HT[newPos].getKey().equals(key) && !HT[newPos].isTombstone()) {
				HT[newPos].setTombstone(true);
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

		int newPos;
		for (int i = 1; (pos + probe(key, i)) % currentSize != pos; i++) {
			newPos = (pos + probe(key, i)) % currentSize;
			if (HT[newPos].getKey().equals(key) && !HT[newPos].isTombstone()) {
				HT[newPos].setKey(key);
				HT[newPos].setValue(value);
				return;
			}
		}

	}

	@Override
	protected void setSizeAndRehash(int newSize) {
		// TODO Auto-generated method stub
		// make a new hash and set it's new size
		DoubleHashing<K, V> newHash = new DoubleHashing<K, V>();
		newHash.setSizeAtStartOfUsage(newSize);
		// rehash all elements in the new hash
		for (int i = 0; i < storedKeys.size(); i++) {
			newHash.put(storedKeys.get(i), get(storedKeys.get(i)));
		}
		// adjust our hash with the new hash lists and hash table
		HT = newHash.HT;
		currentSize = newSize;

	}

	private int probe(K key, int i) {
		return i * h2(key);

	}

	private int h2(K key) {
		int intKey = key.hashCode();
		return (((intKey / currentSize) % (currentSize / 2)) * 2) + 1;
	}
}
