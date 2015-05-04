import java.util.ArrayList;

public class BucketHashing<K, V> extends AbstractHashTable<K, V> {

	private int bucketSize = 5;
	private ArrayList<Pair<K, V>> overFlowList = new ArrayList<Pair<K, V>>();

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		
		int pos = h(key);
		if (storedKeys.contains(key)) {
			// just adjust the value of it
			changeValue(pos,key,value);
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
		// Search for an empty spot or a tombstone in the same bucket
		if (putInBucket(pos, key, value,false)){
			storedKeys.add(key);
			numOfElements++;
			checkLoadingFactorAndDouble();
			return;
		}
			

		// Search the overflow list for tombstone or same key
		for (int i = 0; i < overFlowList.size(); i++) {
			if (overFlowList.get(i).isTombstone()) {
				overFlowList.get(i).setKey(key);
				overFlowList.get(i).setValue(value);
				overFlowList.get(i).setTombstone(false);
				storedKeys.add(key);
				numOfElements++;
				checkLoadingFactorAndDouble();
				return;
			}
		}

		// put new pair in the overFlowList
		overFlowList.add(new Pair<K, V>(key, value));
		storedKeys.add(key);
		numOfElements++;
		checkLoadingFactorAndDouble();

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
		// Search for key in the same bucket
		int startOfbucketPos = ((int) (pos / bucketSize)) * (bucketSize);
			for (int i = startOfbucketPos; i < HT.length && i < startOfbucketPos + bucketSize; i++) {
				if (i == pos)
					continue;
				if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
					
					return HT[i].getValue();
				}
			}

		for (int i = 0; i < overFlowList.size(); i++) {
			if (!overFlowList.get(i).isTombstone() && overFlowList.get(i).getKey().equals(key)) {
				
				return overFlowList.get(i).getValue();
			}
		}
		
		return null;
	}

	@Override
	public void delete(K key) {
		// TODO Auto-generated method stub
		System.out.println("over flow list size "+overFlowList.size());
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
		// Search for key in the same bucket
		int startOfbucketPos = ((int) (pos / bucketSize)) * (bucketSize);
			for (int i = startOfbucketPos; i < HT.length && i < startOfbucketPos + bucketSize; i++) {
				if (i == pos)
					continue;
				if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
					HT[i].setTombstone(true);
					storedKeys.remove(key);
					numOfElements--;
					checkLoadingFactorAndShrink();
					return;
				}
			}

		for (int i = 0; i < overFlowList.size(); i++) {
			if (!overFlowList.get(i).isTombstone() && overFlowList.get(i).getKey().equals(key)) {
				overFlowList.get(i).setTombstone(true);
				storedKeys.remove(key);
				numOfElements--;
				checkLoadingFactorAndShrink();
				return;
			}
		}
		
	}


	private void changeValue(int pos,K key, V value) {
		// TODO Auto-generated method stub
		if (HT[pos].getKey().equals(key) && !HT[pos].isTombstone()) {
			HT[pos].setKey(key);
			HT[pos].setValue(value);
			return;
		}
		// Search for key in the same bucket
		if (putInBucket(pos, key, value, true))
			return;

		for (int i = 0; i < overFlowList.size(); i++) {
			if (!overFlowList.get(i).isTombstone() && overFlowList.get(i).getKey().equals(key)) {
				overFlowList.get(i).setKey(key);
				overFlowList.get(i).setValue(value);
				return;
			}
		}
	}
	
	// serach for an empty spot or a tombstone in the same bucket
	private boolean putInBucket(int pos, K key, V value, boolean keyPresent) {
		// TODO Auto-generated method stub
		int startOfbucketPos = ((int) (pos / bucketSize)) * (bucketSize);
		if (keyPresent) {
			for (int i = startOfbucketPos; i < HT.length && i < startOfbucketPos + bucketSize; i++) {
				if (i == pos)
					continue;
				if (HT[i].getKey().equals(key) && !HT[i].isTombstone()) {
					HT[i].setKey(key);
					HT[i].setValue(value);
					return true;
				}
			}
		} else {

			for (int i = startOfbucketPos; i < HT.length && i < startOfbucketPos + bucketSize; i++) {
				if (i == pos)
					continue;

				if (putInSlot(i, key, value))
					return true;
			}
		}
		return false;
	}

	@Override
	protected void setSizeAndRehash(int newSize) {
		// TODO Auto-generated method stub

		// make a new hash and set it's new size
		BucketHashing<K, V> newHash = new BucketHashing<K, V>();
		newHash.setSizeAtStartOfUsage(newSize);
		// rehash all elements in the new hash
		for (int i = 0; i < storedKeys.size(); i++) {
			newHash.put(storedKeys.get(i), get(storedKeys.get(i)));
		}
		// adjust our hash with the new hash lists and hash table
		HT = newHash.HT;
		overFlowList = newHash.overFlowList;
		currentSize = newSize;
	}
}
