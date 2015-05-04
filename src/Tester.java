import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Tester {

	/**
	 * @param args
	 */
	public static int testCollisions = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numOfElements = 10000;
		ArrayList<Integer> shuffledKeys=new ArrayList<Integer>();
		testCollisions = 0;
//		HashTable<Integer, String> testHash = new SeparateChainingHashTable<Integer, String>();
//		HashTable<Integer, String> testHash = new BucketHashing<Integer, String>();
//		HashTable<Integer, String> testHash = new LinearProbeHash<Integer, String>();
//		HashTable<Integer, String> testHash = new QuadraticProbingHash<Integer, String>();
//		HashTable<Integer, String> testHash = new PseudoRandomProbingHash<Integer, String>();
		HashTable<Integer, String> testHash = new DoubleHashing<Integer, String>();

		// isempty size keys
		System.out.println("1-putting " + numOfElements + " element in hash");
		for (int i = 0; i < numOfElements; i++) {
			shuffledKeys.add(i);
		}
		
		Collections.shuffle(shuffledKeys);
		
		for (int i = 0; i < numOfElements; i++) {
			testHash.put(shuffledKeys.get(i), "value" + shuffledKeys.get(i));
			
		}
		
		
		System.out.println("succeeded 1");
		System.out.println();
		
//		
		
		
//		testHash.put(1, "value" + 1);
//		testHash.put(9, "value" + 9);
		System.out.println("collisions happened = " + testCollisions);
		System.out.println();
//		testHash.delete(50);
		 System.out.println("2-printing all elements keys using the iterator");
		 Iterable<Integer> keys = testHash.keys();
		 Iterator<Integer> keysIterator = keys.iterator();
		 for (int i = 0; i < numOfElements; i++) {
		 System.out.println(keysIterator.next());
		 }
		 System.out.println("succeeded 2");
		 System.out.println();
		
		 System.out.println("3- checking if it contains an element expected true then false");
		 // for (int i = 0; i < 1001; i++) {
		 System.out.println(testHash.contains(numOfElements-1));
		 System.out.println(testHash.contains(numOfElements+1));
		 // }
		 System.out.println("succeeded 3");
		 System.out.println();
		
		 System.out.println("4- getting all elements using get method");
		 for (int i = 0; i < numOfElements; i++) {
		 System.out.println(testHash.get(i));
		 }
		 System.out.println("succeeded 4");
		 System.out.println();
		
		 System.out.println("5- checking isEmpty then size");
		 System.out.println(testHash.isEmpty());
		 System.out.println(testHash.size());
		 System.out.println("succeeded 5");
		 System.out.println();
		
		 System.out.println("6- deleting all elements");
		 for (int i = 0; i < numOfElements; i++) {
		 testHash.delete(i);
		 }
		 System.out.println("succeeded 6");
		 System.out.println();
		
		 System.out.println("7- checking isEmpty then size again");
		 System.out.println(testHash.isEmpty());
		 System.out.println(testHash.size());
		 System.out.println("succeeded 7");
		 System.out.println();
		
	}

}
