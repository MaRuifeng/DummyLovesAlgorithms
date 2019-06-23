package linkedList;

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

    public LRUCachePlain(final int capacity) {
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
        LRUCacheNode target = findNodeFromCache(key);
        if (target == null) throw new Exception("Not found!");
        locateToEvictionQueueHead(target);
        return target.value;
    }

    public void set(int key, int value) {
        LRUCacheNode target = findNodeFromCache(key);
        if (target != null) { // update
            target.value = value;
            locateToEvictionQueueHead(target);
        } else { // insert
            if (this.occupancy == this.capacity) { // cache is full
                removeEvictionQueueTailFromCache();
                this.occupancy--;
            }
            LRUCacheNode node = new LRUCacheNode(key, value);
            addNodeToCache(node);
            this.occupancy++;
        }
    }

    public void print() {
        LRUCacheNode start = this.evictionQueueHead;
        while (start != null) {
            System.out.printf(start.toString() + " > ");
            start = start.next;
        }
        System.out.println();
    }

    private void removeEvictionQueueTailFromCache() {
        LRUCacheNode tail = this.evictionQueueTail;
        this.evictionQueueTail = this.evictionQueueTail.prev;
        removeNodeFromEvictionQueue(tail);

        int index = hashTableIndex(tail.key, this.cacheStoreArraySize);
        this.cacheStore[index] = removeNodeFromHashTableChain(this.cacheStore[index], tail);
    }

    private void addNodeToCache(LRUCacheNode node) {
        locateToEvictionQueueHead(node);
        addNodeToHashTable(node);
    }

    private LRUCacheNode findNodeFromCache(int key) {
        int index = hashTableIndex(key, this.cacheStoreArraySize);
        LRUCacheNode targetChainRoot = this.cacheStore[index];
        LRUCacheNode target = null;
        while (targetChainRoot != null) {
            if (targetChainRoot.key == key) {
                target = targetChainRoot;
                break;
            }
            targetChainRoot = targetChainRoot.next;
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

    private void locateToEvictionQueueHead(LRUCacheNode node) {
        removeNodeFromEvictionQueue(node);

        if (this.evictionQueueHead != null) {
            this.evictionQueueHead.prev = node;
            node.next = this.evictionQueueHead;
        } else {
            this.evictionQueueTail = node;
        }

        this.evictionQueueHead = node;
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

    private void removeNodeFromEvictionQueue(LRUCacheNode node) {
        if (node.prev != null && node.next != null) {
            // reconnect the list
            node.next.prev = node.prev;
            node.prev.next = node.next;
            // void the node to be removed
            node.prev = node.next = null;
        }
        else if (node.next != null) {
            // detach from head
            node.next.prev = null;
            node.next = null;
        }
        else if (node.prev != null) {
            // detach from tail
            node.prev.next = null;
            node.prev = null;
        } else {
            // do nothing
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of LRU caches.");
        LRUCachePlain cache = new LRUCachePlain(4);
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
