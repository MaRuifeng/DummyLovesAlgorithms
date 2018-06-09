package binaryHeap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * LFU (Least Frequently Used) is a famous cache eviction algorithm.
 * For a cache with capacity k, if the cache is full and need to evict a key in it, the key with the 
 * least frequently used value will be kicked out.
 * Implement set() and get() method for LFU cache.
 * 
 * Since this is a priority problem, it can be attempted with the MinHeap data structure. 
 * 
 * @author ruifengm
 * @since 2018-Jun-9
 * 
 * https://www.lintcode.com/problem/lfu-cache/description
 *
 */

public class LFUCache {
	
	
	/**
	 * The idea is to utilize below customized data structure CacheNode that stores a cache entry information on key, 
	 * value, usage count and access sequence number. 
	 * 
	 * The access sequence number is used to break a ties when multiple cache entries have the same usage count.
	 * 
	 * A HashMap data structure is used for O(1) time complexity in get and add/set operations.
	 * A PriorityQueue(MinHeap) data structure is used to find the LFU entry to be kicked out when capacity is full. 
	 * - findMin: O(1) in time
	 * - add/remove: O(logN) in time
	 */
	private class CacheNode {
		int key, usage, value; 
		int seqNum; // used to maintain access sequence so as to apply first-in-first-out tie-breaking
		public CacheNode(int key, int usage, int value, int seqNum) {
			this.key = key; 
			this.usage = usage;
			this.value = value;
			this.seqNum = seqNum;
		}
	}
	
	private int capacity;
	private int occupancy;
	private int accessSeqNum;
	private HashMap<Integer, CacheNode> cacheData;
	private PriorityQueue<CacheNode> evictionQueue;
	
	public LFUCache(int capacity) {
		this.capacity = capacity;
		this.occupancy = 0;
		this.accessSeqNum = 0;
		this.cacheData = new HashMap<>();
		this.evictionQueue = new PriorityQueue<>(new Comparator<CacheNode>() {
			@Override
			public int compare(CacheNode a, CacheNode b) {
				int res = a.usage < b.usage ? -1 : a.usage == b.usage ? 0 : 1;
				if (res == 0) { // breaking tie based on accessing sequence
					res = a.seqNum < b.seqNum ? -1 : 1;
				}
				return res;
			}
		});
	}

	public void set(int key, int value) {
	    if (capacity <= 0) return;
		CacheNode cacheNode = this.cacheData.get(key); 
		if (cacheNode != null) { // existing entry
			this.evictionQueue.remove(cacheNode);
			cacheNode.value = value;
			cacheNode.usage++;
		} else { // new entry
			cacheNode = new CacheNode(key, 0, value, this.accessSeqNum++);
			if (this.occupancy == this.capacity) { // evict LFU data entry
				CacheNode nodeToEvict = this.evictionQueue.poll();
				this.cacheData.remove(nodeToEvict.key);
				this.occupancy--;
			}
			this.cacheData.put(key, cacheNode);
			this.occupancy++;
		}
		this.evictionQueue.add(cacheNode);
	}
	
	public int get(int key) {
		CacheNode cacheNode = this.cacheData.get(key);
		if (cacheNode == null) return -1;
		this.evictionQueue.remove(cacheNode);
		cacheNode.usage++;
		cacheNode.seqNum = this.accessSeqNum++;
		this.evictionQueue.add(cacheNode);
		return cacheNode.value;
	}
	
	public static void main(String[] args) {
		int capacity = 3;
		System.out.println("Welcome to the rabbit hole of LFU caches!\n"
				+ "The capacity of the cache is " + capacity + ".\n");
		
		try {
			LFUCache cache = new LFUCache(capacity);
			cache.set(1, 1);
			cache.set(2, 2);
			System.out.println("Set first two entries and query all in normal order ...");
			System.out.println(cache.get(1));
			System.out.println(cache.get(2));
			System.out.println("Set the third entry and query all in normal order ...");
			cache.set(3, 3);
			System.out.println(cache.get(1));
			System.out.println(cache.get(2));
			System.out.println(cache.get(3));
			System.out.println("Set the fourth entry and query all in normal order (the third entry is evicted as it's LFU) ...");
			cache.set(4, 4);
			System.out.println(cache.get(1));
			System.out.println(cache.get(2));
			System.out.println(cache.get(3));
			System.out.println(cache.get(4));
			
			System.out.println("\n############\n");
			LFUCache cache2 = new LFUCache(capacity);
			System.out.println("Set first three entries and query the first entry...");
			cache2.set(1, 1);
			cache2.set(2, 2);
			cache2.set(3, 3);
			System.out.println(cache2.get(1));
			System.out.println("Set the fourth entry (the second entry is evicted) and query all in reverse order ...");
			cache2.set(4, 4);
			System.out.println(cache2.get(4));
			System.out.println(cache2.get(3));
			System.out.println(cache2.get(2));
			System.out.println(cache2.get(1));
			cache2.set(5, 5);
			System.out.println("Set the fifth entry (the fourth entry is evicted since it was accessed first in previous query) and query all in normal order ...");
			System.out.println(cache2.get(1));
			System.out.println(cache2.get(2));
			System.out.println(cache2.get(3));
			System.out.println(cache2.get(4));
			System.out.println(cache2.get(5));
			
			System.out.println("\n############\n");
			LFUCache cache3 = new LFUCache(capacity);
			System.out.println("Set first three entries, set the first entry again and query the second entry ...");
			cache3.set(1, 1);
			cache3.set(2, 2);
			cache3.set(1, 11);
			cache3.set(3, 3);
			System.out.println(cache3.get(2));
			cache3.set(4, 4);
			System.out.println("Set the fourth entry (the third entry is evicted) and query all in normal order ...");
			System.out.println(cache3.get(1));
			System.out.println(cache3.get(2));
			System.out.println(cache3.get(3));
			System.out.println(cache3.get(4));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\nAll rabbits gone.");
	}
	
}
