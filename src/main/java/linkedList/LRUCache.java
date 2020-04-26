package linkedList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LRU (Least Recently Used) is a cache eviction algorithm.
 *
 * A cache keeps a certain capacity of data in memory for quick access. When inserting a new value into an LRU cache and
 * the cache is full, the least recently used value will be evicted to free up a slot.
 *
 * Design a data structure for LRU cache which implements below operations.
 *
 * - get(key)
 * - set(key, value)
 *
 * For a quick start, both key and value are of integer type. Target at time complexity O(1) for both operations.
 *
 * https://www.lintcode.com/problem/lru-cache/description
 *
 * @author Ruifeng Ma
 * @since 2019-Jun-22
 */

public class LRUCache {

    /**
     * - Discussion -
     * A hash table provides O(1) key based retrieval in average, and a doubly linked list with head and tail offers
     * an eviction queue (least recently used entry at tail).
     * An LRU cache can be designed by leveraging both.
     */

    private class LRUCacheNode {
        int key;
        int value;

        public LRUCacheNode(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return "{" + this.key + ": " + this.value + "}";
        }
    }

    private Map<Integer, LRUCacheNode> cacheStore;
    private LinkedList<LRUCacheNode> evictionQueue;
    private int capacity;
    private int occupancy;


    public LRUCache(final int capacity) {
        this.capacity = capacity;
        this.occupancy = 0;
        this.cacheStore = new HashMap<>((int) Math.ceil(this.capacity / 0.75)); // to avoid rehash
        this.evictionQueue = new LinkedList<>();
    }

    public int get(int key) throws Exception {
        LRUCacheNode target = this.cacheStore.get(key);
        if (target == null) throw new Exception("Not found!");
        setMostRecentlyUsed(target);

        return target.value;
    }

    public void set(int key, int value) {
        LRUCacheNode target = this.cacheStore.get(key);
        if (target != null) { // update
            target.value = value;
            setMostRecentlyUsed(target);
        } else { // insert
            if (this.occupancy == this.capacity) { // cache full
                this.cacheStore.remove(this.evictionQueue.pollLast().key);
                this.occupancy--;
            }
            target = new LRUCacheNode(key, value);
            this.cacheStore.put(key, target);
            this.evictionQueue.addFirst(target);
            this.occupancy++;
        }
    }

    public void print() {
        System.out.println(this.evictionQueue.stream().map(LRUCacheNode::toString).collect(Collectors.joining(" > ")));
    }

    private void setMostRecentlyUsed(LRUCacheNode node) {
        this.evictionQueue.remove(node);
        this.evictionQueue.addFirst(node);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of LRU caches.");
        LRUCache cache = new LRUCache(5);
        System.out.println("Set first 5 new entries ...");
        cache.set(1, 11);
        cache.set(2, 21);
        cache.set(3, 31);
        cache.set(4, 41);
        cache.set(5, 51);
        cache.print();

        System.out.println("Set existing entry ...");
        cache.set(3, 33);
        cache.print();

        System.out.println("Set a new entry to trigger eviction...");
        cache.set(6, 61);
        cache.print();

        System.out.println("Get an existing entry...");
        try {
            cache.get(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.print();

        System.out.println("Get a non-existing entry...");
        try {
            cache.get(9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.print();

        System.out.println("Get an evicted entry...");
        try {
            cache.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.print();
        System.out.println("\nAll rabbits gone.");

    }
}
