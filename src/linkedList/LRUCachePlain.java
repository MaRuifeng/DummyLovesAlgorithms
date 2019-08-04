package linkedList;

/**
 * LRU (Least Recently Used) is a cache eviction algorithm.
 * <p>
 * A cache keeps a certain capacity of data in memory for quick access. When inserting a new value into an LRU cache and
 * the cache is full, the least recently used value will be evicted to free up a slot.
 * <p>
 * Design a data structure for LRU cache which implements below operations.
 * <p>
 * - get(key)
 * - set(key, value)
 * <p>
 * For a quick start, both key and value are of integer type. Target at time complexity O(1) for both operations.
 * <p>
 * https://www.lintcode.com/problem/lru-cache/description
 *
 * @author Ruifeng Ma
 * @since 2019-Jun-22
 */

import java.util.Arrays;

/**
 * TODO
 * 1) Add proper unit tests to the implementation
 * 2) Separate Hash Table implementation to a new topic to discuss further
 */
public class LRUCachePlain {

    /**
     * - Discussion -
     * A hash table provides O(1) key based retrieval in average, and a doubly linked list with head and tail offers
     * an eviction queue (least recently used entry at tail).
     * An LRU cache can be designed by leveraging both.
     *
     * In stead of relying upon the JDK Collection and Map interface, write your own.
     */

    private class LRUCacheNode {
        int key;
        int value;
        LRUCacheNode prev;
        LRUCacheNode next;
        LRUCacheNode hashNext; // for hash table collision resolution chain

        public LRUCacheNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.prev = this.next = this.hashNext = null;
        }

        public String toString() {
            return "{" + this.key + ": " + this.value + "}";
        }
    }

    private int capacity;
    private int occupancy;

    private LRUCacheNode[] cacheStore; // hash table with collisions resolved via chaining
    private int cacheStoreArraySize;

    private LRUCacheNode evictionQueueHead;
    private LRUCacheNode evictionQueueTail;

    public LRUCachePlain(final int capacity) throws Exception {
        if (capacity <= 0) throw new Exception("Capacity cannot be negative!");
        this.capacity = capacity;
        this.occupancy = 0;
        this.cacheStoreArraySize = roundUpToNearestPowerOfTwo(this.capacity);
        this.cacheStore = new LRUCacheNode[this.cacheStoreArraySize];
    }

    /* Learnt form what Java 8 does in HashMap implementation */
    static final int roundUpToNearestPowerOfTwo(int number) {
        int result = 1;
        while (result < number) result = result << 1; // power of 2 computation via left bit shift operator
        return result;
    }

    static final int hash(int key) {
        return key ^ (key >>> 16); // bitwise unsigned right shift to spread differences to lower bits for index calculation
    }

    static final int hashTableIndex(int key, int length) {
        int keyHash = hash(key);
        return keyHash & (length - 1); // computationally optimized modulo operation on numbers that are power of 2
    }
    /* Learnt form what Java 8 does in HashMap implementation */

    public int get(int key) throws Exception {
        LRUCacheNode target = findNodeFromHashTable(key);
        if (target == null) throw new Exception("Not found!");
        locateToEvictionQueueHead(target);
        return target.value;
    }

    public void set(int key, int value) {
        LRUCacheNode target = findNodeFromHashTable(key);
        if (target != null) { // update
            target.value = value;
            locateToEvictionQueueHead(target);
        } else { // insert
            if (this.occupancy == this.capacity) { // cache is full
                LRUCacheNode tail = this.evictionQueueTail;
                this.evictionQueueTail = this.evictionQueueTail.prev;
                removeNodeFromEvictionQueue(tail);

                int index = hashTableIndex(tail.key, this.cacheStoreArraySize);
                this.cacheStore[index] = removeNodeFromHashTableChain(this.cacheStore[index], tail);

                this.occupancy--;
            }
            LRUCacheNode node = new LRUCacheNode(key, value);
            locateToEvictionQueueHead(node);
            addNodeToHashTable(node);
            this.occupancy++;
        }
    }

    public boolean contains(int key) {
        return null != findNodeFromHashTable(key);
    }

    public void printEvictionQueue() {
        LRUCacheNode start = this.evictionQueueHead;
        while (start != null) {
            System.out.printf(start.toString() + " > ");
            start = start.next;
        }
        System.out.println();
    }

    public void printCacheStore() {
        Arrays.stream(cacheStore).forEach(node -> {
            while (node != null) {
                System.out.print(node.toString() + " ");
                node = node.hashNext;
            }
            System.out.println();
        });
    }

    /* Doubly linked list for eviction queue (start) */
    private void locateToEvictionQueueHead(LRUCacheNode node) {
        if (node == this.evictionQueueTail) this.evictionQueueTail = this.evictionQueueTail.prev;

        removeNodeFromEvictionQueue(node);

        if (this.evictionQueueHead != null) {
            this.evictionQueueHead.prev = node;
            node.next = this.evictionQueueHead;
        } else {
            this.evictionQueueTail = node;
        }

        this.evictionQueueHead = node;
    }

    private void removeNodeFromEvictionQueue(LRUCacheNode node) {
        if (node.prev != null && node.next != null) {
            // reconnect the list
            node.next.prev = node.prev;
            node.prev.next = node.next;
            // void the node to be removed
            node.prev = node.next = null;
        } else if (node.next != null) {
            // detach from head
            node.next.prev = null;
            node.next = null;
        } else if (node.prev != null) {
            // detach from tail
            node.prev.next = null;
            node.prev = null;
        } else {
            // do nothing
        }
    }
    /* Doubly linked list for eviction queue (end) */

    /* Hash table with chain based collision resolution for cache storage (start) */
    private LRUCacheNode findNodeFromHashTable(int key) {
        int index = hashTableIndex(key, this.cacheStoreArraySize);
        LRUCacheNode targetChainRoot = this.cacheStore[index];
        LRUCacheNode target = null;
        while (targetChainRoot != null) {
            if (targetChainRoot.key == key) {
                target = targetChainRoot;
                break;
            }
            targetChainRoot = targetChainRoot.hashNext;
        }
        return target;
    }

    private void addNodeToHashTable(LRUCacheNode node) {
        int index = hashTableIndex(node.key, this.cacheStoreArraySize);
        if (this.cacheStore[index] == null) this.cacheStore[index] = node;
        else { // collision handling
            LRUCacheNode root = this.cacheStore[index];
            while (root.hashNext != null) root = root.hashNext;
            root.hashNext = node;
        }
    }

    /**
     * Return root of the chain after removal
     */
    private LRUCacheNode removeNodeFromHashTableChain(LRUCacheNode root, LRUCacheNode node) {
        LRUCacheNode prev = null;
        LRUCacheNode iterator = root;

        if (iterator != null && iterator.value == node.value) return null;

        while (iterator != null) {
            if (iterator.key == node.key) {
                if (prev != null) prev.hashNext = node.hashNext;
                node.hashNext = null;
            }
            prev = iterator;
            iterator = iterator.hashNext;
        }

        return root;
    }
    /* Hash table with chain based collision resolution for cache storage (end) */


    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of LRU caches.");
        LRUCachePlain cache = null;
        try {
            cache = new LRUCachePlain(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nSet first 5 new entries ...");
        cache.set(1, 11);
        cache.set(2, 21);
        cache.set(3, 31);
        cache.set(4, 41);
        cache.set(5, 51);
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nSet existing entry ...");
        cache.set(3, 33);
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nSet a new entry to trigger eviction...");
        cache.set(6, 61);
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nGet an existing entry...");
        try {
            cache.get(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nGet a non-existing entry...");
        try {
            cache.get(9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nGet an evicted entry...");
        try {
            cache.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nCheck hash table chaining...");
        try {
            cache = new LRUCachePlain(7);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cache.set(0, 0);
        cache.set(8, 8);
        cache.set(16, 16);
        cache.set(1, 1);
        cache.set(2, 2);
        cache.set(9, 9);
        System.out.println("Eviction queue: ");
        cache.printEvictionQueue();
        System.out.println("Cache store: ");
        cache.printCacheStore();

        System.out.println("\nAll rabbits gone.");
    }
}
