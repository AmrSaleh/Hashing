public class Pair<K, V> {

	private K key;
	private V value;
	private Boolean tombstone;

	public Pair() {
		// TODO Auto-generated constructor stub
		key=null;
		value=null;
		tombstone = false;

	}
	public Pair(K theKey, V theValue) {
		// TODO Auto-generated constructor stub
		key = theKey;
		value = theValue;
		tombstone = false;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Boolean isTombstone() {
		return tombstone;
	}

	public void setTombstone(Boolean tombstone) {
		this.tombstone = tombstone;
	}
}
