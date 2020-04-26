package integerArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implement an array base circular queue with below operation time complexities.
 * <p>
 * head value - O(1)
 * tail value - O(1)
 * enqueue - O(1)
 * dequeue - O(1)
 *
 * @author Ruifeng Ma
 * @since 2019-Jun-20
 */

public class CircularArrayQueue {
    private int size;
    private int head, tail; // indices pointing to front and end of the queue
    private int[] array;

    public CircularArrayQueue(final int size) {
        this.size = size + 1; // one extra empty slot needed, see enqueue discussion
        this.head = this.tail = 0;
        array = new int[size + 1]; // claim continuous space from memory
    }

    public int getHeadValue() {
        return array[head];
    }

    public int getTailValue() {
        return array[getTailIndex()];
    }

    public int getTail() {
        return tail;
    }

    public int getHead() {
        return head;
    }

    public int getTailIndex() { // index pointing to queue end
        return (tail - 1 + size) % size;
    }

    /**
     * Note how the modulo operation is used to gracefully go across boundaries.
     * It's common to be used to avoid indexOutOfBoundary exception.
     * <p>
     * Since this is a circular structure, if tail==head condition is used to denote an empty queue,
     * it can't be used to denote a full queue. Then we are left with tail immediately after head, i.e.
     * (tail + 1) % size == head, which inevitably leads to an always empty slot pointed by tail.
     */
    public boolean enqueue(final int value) {
        int newTail = (tail + 1) % size;
        if (newTail == head) return false; // tail index right after head index, queue is full
        array[tail] = value;
        tail = newTail;
        return true;
    }

    public boolean dequeue() {
        if (tail == head) return false; // queue is empty
        head = (head + 1) % size;
        return true;
    }

    public void print() {
        int i = head;
        List<Integer> intList = new ArrayList<>();
        while (i != tail) {
            intList.add(array[i]);
            i = (i + 1) % size;
        }

        System.out.println(intList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" < ")));
    }

    private static void peephole(CircularArrayQueue queue) {
        System.out.println("Current queue: ");
        queue.print();
        System.out.println("Current head at " + queue.getHead() + ", queue head value: " + queue.getHeadValue());
        System.out.println("Current tail at " + queue.getTail() + ", queue end value: " + queue.getTailValue());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of circular array queues!");

        CircularArrayQueue queue = new CircularArrayQueue(4);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);

        peephole(queue);

        System.out.println("\nDequeue two entries ...");
        queue.dequeue();
        queue.dequeue();

        peephole(queue);

        System.out.println("\nDequeue two more entries till empty...");
        queue.dequeue();
        queue.dequeue();

        peephole(queue);

        System.out.println("\nAdd 4 entries ...");
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);
        queue.enqueue(8);

        peephole(queue);

        System.out.println("\nDequeue 1 entry ...");
        queue.dequeue();

        peephole(queue);

        System.out.println("\nAll rabbits gone.");
    }
}
