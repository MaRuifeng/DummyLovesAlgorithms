package dynamicProgramming;

import utils.FunIntAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A LEGO workshop is going to put up a weekend show. Currently there is only a single worker who can only
 * assemble 2 parts at a time. The time it takes to assemble them equals to the total size of these two parts.
 * <p>
 * Suppose there is a model that consists of 5 parts of size 1, 2, 3, 4 and 5, a possible assembly process could be
 * <p>
 * Step 1)    1 + 2 => time taken: 3; left parts: [3, 3, 4, 5]
 * Step 2)    3 + 3 => time taken: 6; left parts: [6, 4, 5]
 * Step 3)    6 + 4 => time taken: 10; left parts: [10, 5]
 * Step 4)    10 + 5 => time taken: 15; Done
 * <p>
 * Total time taken: 3 + 6 + 10 + 15 = 34
 * <p>
 * Given a fixed number of parts of different sizes, compute the least time required by this worker to assemble
 * them into a single model.
 *
 * Amaz0n 0nL1ne C0d3 T3st
 *
 * @author Ruifeng Ma
 * @since 2019-Jun-08
 */

public class LegoAssembler extends FunIntAlgorithm {

    /**
     * <b>Divide & Conquer</b>
     * Given a list of n parts, denoted as P let Sol(P) be the minimum time it requires to assemble them, we'll have
     * below recursive pattern.
     * <p>
     * Sol(P) = MIN(P[i] + P[j] + Sol(P with i & j'th element merged)), where 0 < i < n, 0 < j < n and i != j
     * <p>
     * Since the worker can only assemble two parts at a time, we can search for the solution exhaustively
     * by checking through all pairs until assembled into a single model.
     */
    private static int recursiveMinTime(List<Integer> parts) {
        if (parts == null || parts.size() <= 1) return 0;

        int minTime = Integer.MAX_VALUE;

        for (int i = 0; i < parts.size(); i++) {
            for (int j = 0; j < i; j++) {
                List<Integer> reducedParts = new ArrayList<>(parts);
                reducedParts.remove(i);

                int size = parts.get(i) + reducedParts.get(j);
                reducedParts.set(j, size);

                minTime = Math.min(minTime, size + recursiveMinTime(reducedParts));
            }
        }
        return minTime;
    }

    /**
     * Use a DP lookup map to avoid repeated computations spawn by recursive calls.
     */
    private static int recursiveMinTimeDPMemo(List<Integer> parts, Map<String, Integer> map) {
        // since item order does not matter, e.g. list [1, 2, 3] and [2, 3, 1] yield the same result to the problem,
        // convert the list to a sorted array string as key to reduce DP lookup map size
        String key = parts.stream().sorted().map(String::valueOf).collect(Collectors.joining(","));

        if (map.get(key) != null) return map.get(key);

        if (parts.size() == 1) map.put(key, 0);
        else {
            int minTime = Integer.MAX_VALUE;

            for (int i = 0; i < parts.size(); i++) {
                for (int j = 0; j < i; j++) {
                    List<Integer> reducedParts = new ArrayList<>(parts);
                    reducedParts.remove(i);

                    int size = parts.get(i) + reducedParts.get(j);
                    reducedParts.set(j, size);

                    minTime = Math.min(minTime, size + recursiveMinTimeDPMemo(reducedParts, map));
                }
            }
            map.put(key, minTime);
        }
        return map.get(key);
    }

    private static int recursiveMinTimeDPMemo(List<Integer> parts) {
        if (parts == null || parts.size() <= 1) return 0;

        Map<String, Integer> DPLookUp = new HashMap<>();
        int result = recursiveMinTimeDPMemo(parts, DPLookUp);

        return result;
    }

    /**
     * <b>Greedy</b>
     * Always keep the list in ascending order and assemble from smallest.
     */
    private static int greedyMinTime(List<Integer> parts) {
        if (parts == null) return 0;

        int minTime = 0;

        List<Integer> actionParts = new ArrayList<>(parts);

        while (actionParts.size() > 1) {
            Collections.sort(actionParts);

            int tip = actionParts.get(0);
            actionParts.remove(0);
            int size = tip + actionParts.get(0);
            actionParts.set(0, size);

            minTime += size;
        }

        return minTime;
    }

    /**
     * Improve the greedy method with PriorityQueue (MinHeap)
     */
    private static int greedyTreeMapMinTime(List<Integer> parts) {
        if (parts == null) return 0;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        parts.forEach(minHeap::add);

        int minTime = 0;

        while (minHeap.size() > 1) {
            int size = minHeap.poll() + minHeap.poll();
            minHeap.add(size);

            minTime += size;
        }

        return minTime;

    }

    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of LEGO parts assemblers.\n");

        // List<Integer> parts = Arrays.asList(1, 1, 1, 1, 1); // result: 12

        List<Integer> parts = Arrays.stream(genRanIntArr(8, 1, 10))
                //.mapToObj(Integer::new)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("List of parts: " + parts);

        try {
            runIntListFuncAndCalculateTime("[Recursive] Least time required: ",
                    LegoAssembler::recursiveMinTime, parts);
            runIntListFuncAndCalculateTime("[Recursive][DP Memo] Least time required: ",
                    LegoAssembler::recursiveMinTimeDPMemo, parts);
            runIntListFuncAndCalculateTime("[Iterative][Greedy] Least time required: ",
                    LegoAssembler::greedyMinTime, parts);
            runIntListFuncAndCalculateTime("[Iterative][Greedy][MinHeap] Least time required: ",
                    LegoAssembler::greedyTreeMapMinTime, parts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All rabbits gone");
    }
}
