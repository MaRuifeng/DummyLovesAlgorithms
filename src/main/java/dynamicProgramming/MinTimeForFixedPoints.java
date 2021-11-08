package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import utils.FunIntAlgorithm;

/**
 * Given a set of questions, each question carries a given number of points. A student gets either
 * full points or zero points depending on whether the question is solved. Each question takes the
 * student a different amount of time to solve. Find the minimum time needed by the student to
 * obtain a target total number of points. Return Integer.MAX_VALUE if there is no way to reach the exact target
 * number of points.
 * <p>
 * Refer to the {@link SubsetOfFixedSum} problem.
 *
 * @author ruifengm
 * @since 2021-Nov-07
 */

public class MinTimeForFixedPoints {

    private static class Question {

        int points;
        int time;

        public Question( int points, int time ) {
            this.points = points;
            this.time = time;
        }
    }

    /**
     * This problem can be reduced to the Knapsack problem hence it's NP-complete with an exponential
     * time complexity.
     * <p>
     * We try to observe the sub-problem property.
     * <p>
     * Let Q(n) be a question array of size n, and P be the target number of points. Let Sol(Q(n), P)
     * denote the minimum total time required to score a target points of P.
     * <p>
     * We reduce the problem by removing one question Q[n-1] from the question array. From subarray
     * Q(n-1), to determine the solution for Q(n), it's possible that some of its
     * elements can already sum up to P, meaning Q[n-1] does not need to be included for the final
     * solution, or they can sum up to P - Q[n-1], meaning Q[n-1] needs to be included for the final
     * solution. There are no other possibilities for the parent solution to exist.
     * <p>
     * Below is the recursive pattern:
     * <p>
     * Sol(Q(n), P) = Min(Sol(Q(n-1), P),                             removed element not included
     *                    Sol(Q(n-1), P-Q[n-1]) + Q[n-1].time)        removed element included
     */
    private static int minTime( List<Question> questionList, int size, int sum ) {
        if (sum == 0) {
            return 0; // sum reached
        }
        if (size == 0) {
            return Integer.MAX_VALUE; // sum not reached and question list depleted
        }
        if (sum < 0) {
            return Integer.MAX_VALUE; // sum surpassed
        }

        int excl = minTime(questionList, size - 1, sum); // last element not to be included
        int incl = minTime(questionList, size - 1, sum - questionList.get(size - 1).points); // last element to be included

        return Math.min(excl, incl == Integer.MAX_VALUE ? Integer.MAX_VALUE : incl + questionList.get(size - 1).time);
    }

    private static int minTimeDriver( List<Question> questionList, int sum ) {
        return minTime(questionList, questionList.size(), sum);
    }

    /**
     * DP top down memoization. Complexity is pseudo-polynomial at O(n*sum).
     */
    private static int minTimeDPMemo( List<Question> questionList, int size, int sum, int[][] dpTable ) {
        if (dpTable[size][sum] != -1) return dpTable[size][sum];
        if (sum == 0) dpTable[size][sum] = 0;
        else if (size == 0) {
            dpTable[size][sum] = Integer.MAX_VALUE;
        } else {
            if (sum < questionList.get(size - 1).points)
                dpTable[size][sum] = minTimeDPMemo(questionList, size - 1, sum, dpTable);
            else {
                int excl = minTimeDPMemo(questionList, size - 1, sum, dpTable);
                int incl = minTimeDPMemo(questionList, size - 1, sum - questionList.get(size - 1).points, dpTable);
                dpTable[size][sum] = Math.min(excl, incl == Integer.MAX_VALUE ? Integer.MAX_VALUE : incl + questionList.get(size - 1).time);
            }

        }
        return dpTable[size][sum];
    }

    private static int minTimeDPMemoDriver( List<Question> questionList, int sum ) {
        int[][] dpTable = new int[questionList.size() + 1][sum + 1];
        for (int[] row : dpTable) {
            Arrays.fill(row, -1);
        }
        return minTimeDPMemo(questionList, questionList.size(), sum, dpTable);
    }

    /**
     * DP bottom up tabulation. Complexity is pseudo-polynomial at O(n*sum).
     */
    private static int minTimeDPTabu( List<Question> questionList, int sum ) {
        int[][] DPLookUp = new int[questionList.size() + 1][sum + 1];
        // base state
        for (int i = 0; i <= questionList.size(); i++) DPLookUp[i][0] = 0;
        for (int i = 1; i <= sum; i++) DPLookUp[0][i] = Integer.MAX_VALUE;
        // proliferation
        for (int i = 1; i <= questionList.size(); i++) {
            for (int j = 1; j <= sum; j++) {
                if (j < questionList.get(i - 1).points) DPLookUp[i][j] = DPLookUp[i - 1][j];
                else {
                    int excl = DPLookUp[i - 1][j];
                    int incl = DPLookUp[i - 1][j - questionList.get(i - 1).points];
                    DPLookUp[i][j] = Math.min(excl, incl == Integer.MAX_VALUE ? Integer.MAX_VALUE : incl + questionList.get(i - 1).time);
                }
            }
        }
        return DPLookUp[questionList.size()][sum];
    }


    /**
     * Recursively look up all qualified subsets of questions by examining the DP lookup table generated through tabulation, and then consolidate
     * their total amounts of time.
     * <p>
     * Note that although this solution is able to find the subset that produces the optimal solution,
     * its complexity is exponential without being pseudo-polynomial, which might be further improved with another DP lookup table.
     */
    private static int recursiveMinTimeDPTabu( List<Question> questionList, int sum ) {
        boolean[][] DPLookUp = new boolean[questionList.size() + 1][sum + 1];
        // base state
        for (int i = 0; i <= questionList.size(); i++) DPLookUp[i][0] = true;
        for (int i = 1; i <= sum; i++) DPLookUp[0][i] = false;
        // proliferation
        for (int i = 1; i <= questionList.size(); i++) {
            for (int j = 1; j <= sum; j++) {
                if (j < questionList.get(i - 1).points) DPLookUp[i][j] = DPLookUp[i - 1][j];
                else DPLookUp[i][j] = DPLookUp[i - 1][j] || DPLookUp[i - 1][j - questionList.get(i - 1).points];
            }
        }
        List<Question> solution = new ArrayList<>();
        List<Integer> totalTimes = new ArrayList<>();
        recursiveLookupAndGetTotalTime(solution, questionList.size(), DPLookUp, sum, questionList, totalTimes);

        OptionalInt optionalMin = totalTimes.stream().mapToInt(Integer::intValue).min();
        return optionalMin.isPresent() ? optionalMin.getAsInt() : Integer.MAX_VALUE;
    }

    private static void recursiveLookupAndGetTotalTime( List<Question> solution, int itemIdx, boolean[][] table, int sumIdx, List<Question> questionList, List<Integer> totalTimes ) {
        if (sumIdx == 0) {
            // sum achieved, sub set found, get total time
            totalTimes.add(solution.stream().map(q -> q.time).mapToInt(Integer::intValue).sum());
        } else if (itemIdx == 0) {
        } // no solution
        else {
            if (table[itemIdx][sumIdx]) {
                ArrayList<Question> newSolution = new ArrayList<>(solution);
                newSolution.add(questionList.get(itemIdx - 1));
                if (sumIdx < questionList.get(itemIdx - 1).points)
                    // exclude last element and recur
                    recursiveLookupAndGetTotalTime(solution, itemIdx - 1, table, sumIdx, questionList, totalTimes);
                else {
                    // include last element and recur
                    recursiveLookupAndGetTotalTime(newSolution, itemIdx - 1, table, sumIdx - questionList.get(itemIdx - 1).points, questionList, totalTimes);
                    // exclude last element and recur
                    recursiveLookupAndGetTotalTime(solution, itemIdx - 1, table, sumIdx, questionList, totalTimes);
                }
            }
        }
    }

    @FunctionalInterface
    protected interface QuestionArrayToIntFunction {
        int apply( List<Question> qList, int sum ) throws Exception;
    }

    protected static void runFuncAndCalculateTime( String message, QuestionArrayToIntFunction func, List<Question> qList, int sum ) throws Exception {
        long startTime = System.nanoTime();
        System.out.printf("%-70s%s\n", message, func.apply(qList, sum));
        long endTime = System.nanoTime();
        long totalTime = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }

    public static void main( String[] args ) {
        System.out.println("Welcome to the rabbit hole of problem points!\n");

        final int target = 210;

        List<Question> questionList = new ArrayList<>();
        IntStream.range(0, 20)
                .forEach($ -> questionList.add(new Question(FunIntAlgorithm.genRanInt(1, 21), FunIntAlgorithm.genRanInt(1, 10))));

        System.out.println("Question list:");
        questionList.forEach(q -> System.out.println("Points: " + q.points + " " + "Time: " + q.time));
        System.out.println("Target: " + target + "\n");

        try {
            runFuncAndCalculateTime("[Recursion][NP-complete]    Minimum time: ", MinTimeForFixedPoints::minTimeDriver, questionList, target);
            runFuncAndCalculateTime("[Recursion][DP Memo]        Minimum time: ", MinTimeForFixedPoints::minTimeDPMemoDriver, questionList, target);
            runFuncAndCalculateTime("[Iteration][DP Tabu]        Minimum time: ", MinTimeForFixedPoints::minTimeDPTabu, questionList, target);
            runFuncAndCalculateTime("[Recursion][NP-complete]    Minimum time: ", MinTimeForFixedPoints::recursiveMinTimeDPTabu, questionList, target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nAll rabbits gone.");
    }

}
