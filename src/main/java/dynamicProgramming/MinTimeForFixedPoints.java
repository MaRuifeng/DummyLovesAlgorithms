package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import utils.FunIntAlgorithm;

/**
 * Given a set of questions, each question carries a given number of points. A student gets either
 * full points or zero points depending on whether the question is solved. Each question takes the
 * student a different amount of time to solve. Find the minimum time needed by the student to
 * obtain a target total number of points. Return 0 if there is no way to reach the exact target
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
     * The solution can be pursued in two steps. Firstly we try to write an algorithm to determine
     * whether there exist a subset of questions whose points sum up to P. If positive, we iterate
     * over all such subsets and look for the one which produces the minimum total time.
     * <p>
     * We try to observe the sub-problem property.
     * <p>
     * Let Q(n) be a question array of size n, and P be the target number of points. Let Sol(Q(n), P)
     * denote a boolean value indicating whether there exists a subset whose points sum up to P.
     * <p>
     * We reduce the problem by removing one question Q[n-1] from the question array. From subarray
     * Q(n-1), to determine whether a solution exists for Q(n), it's possible that some of its
     * elements can already sum up to P, meaning Q[n-1] does not need to be included for the final
     * solution, or they can sum up to P - Q[n-1], meaning Q[n-1] needs to be included for the final
     * solution. There are no other possibilities for the parent solution to exist.
     * <p>
     * Below is the recursive pattern:
     * <p>
     * Sol(Q(n), P) = Sol(Q(n-1), P)               removed element not included
     * ||
     * Sol(Q(n-1),P-Q[n-1])         removed element included
     */
    private static boolean subsetExists( List<Question> questionList, int size, int sum ) {
        if (sum == 0) {
            return true; // sum reached
        }
        if (size == 0) {
            return false; // sum not reached and question list depleted
        }
        if (sum < 0) {
            return false; // sum surpassed
        }

        return subsetExists( questionList, size - 1, sum )
                || subsetExists( questionList, size - 1,
                sum - questionList.get( size - 1 ).points );
    }

    private static boolean subsetExistsDriver( List<Question> questionList, int sum ) {
        return subsetExists( questionList, questionList.size(), sum );
    }

    /**
     * DP top down memoization. Complexity is pseudo-polynomial.
     */
    private static boolean subsetExistsDPMemo( List<Question> questionList, int size, int sum, Boolean[][] dpTable ) {
        if (dpTable[size][sum] != null) return dpTable[size][sum];
        if (sum == 0) dpTable[size][sum] = true;
        else if (size == 0) {
            dpTable[size][sum] = false;
        } else {
            if (sum < questionList.get( size - 1 ).points)
                dpTable[size][sum] = subsetExistsDPMemo( questionList, size - 1, sum, dpTable );
            else
                dpTable[size][sum] = subsetExistsDPMemo( questionList, size - 1, sum, dpTable )
                        || subsetExistsDPMemo( questionList, size - 1, sum - questionList.get( size - 1 ).points, dpTable );
        }
        return dpTable[size][sum];
    }

    private static boolean subsetExistsDPMemoDriver( List<Question> questionList, int sum ) {
        Boolean[][] dpTable = new Boolean[questionList.size() + 1][sum + 1];
        return subsetExistsDPMemo( questionList, questionList.size(), sum, dpTable );
    }

    /**
     * DP bottom up tabulation. Complexity is pseudo-polynomial.
     */
    private static boolean subsetExistsDPTabu( List<Question> questionList, int sum ) {
        boolean[][] DPLookUp = new boolean[questionList.size() + 1][sum + 1];
        // base state
        for (int i = 0; i <= questionList.size(); i++) DPLookUp[i][0] = true;
        for (int i = 1; i <= sum; i++) DPLookUp[0][i] = false;
        // proliferation
        for (int i = 1; i <= questionList.size(); i++) {
            for (int j = 1; j <= sum; j++) {
                if (j < questionList.get( i - 1 ).points) DPLookUp[i][j] = DPLookUp[i - 1][j];
                else DPLookUp[i][j] = DPLookUp[i - 1][j] || DPLookUp[i - 1][j - questionList.get( i - 1 ).points];
            }
        }
        return DPLookUp[questionList.size()][sum];
    }

    /**
     * Get total time of all qualified subsets through DP top down memoization.
     */
    private static Boolean getTotalTimesDPMemo( List<Question> solution, List<Question> questionList, int sum, Boolean[][] table, List<Integer> totalTimes ) {
        if (table[questionList.size()][sum] != null && !table[questionList.size()][sum]) {
            // no solution, return boolean check only
            return table[questionList.size()][sum];
        } else {
            if (sum == 0) {
                // sum achieved, solution found, get total time
                System.out.println("Time: " + solution.stream().map( q -> q.time ).mapToInt( Integer::intValue ).sum());
                totalTimes.add( solution.stream().map( q -> q.time ).mapToInt( Integer::intValue ).sum() );
                // update DP Lookup table
                table[questionList.size()][sum] = true;
            } else if (questionList.size() == 0) {
                // set is depleted and no solution found, print nothing
                // update DP Lookup table
                table[questionList.size()][sum] = false;
            } else {
                List<Question> newSolution = new ArrayList<>( solution );
                newSolution.add( questionList.get( questionList.size() - 1 ) );
                List<Question> newQuestionList = new ArrayList<>( questionList );
                newQuestionList.remove( questionList.get( questionList.size() - 1 ) );
                if (sum < questionList.get( questionList.size() - 1 ).points) table[questionList.size()][sum] =
                        // exclude last element of set and recur
                        getTotalTimesDPMemo( solution, newQuestionList, sum, table, totalTimes );
                else {
                    // include last element of set and recur
                    Boolean incl = getTotalTimesDPMemo( newSolution, newQuestionList, sum - questionList.get( questionList.size() - 1 ).points, table, totalTimes );
                    // exclude last element of set and recur
                    Boolean excl = getTotalTimesDPMemo( solution, newQuestionList, sum, table, totalTimes );
                    table[questionList.size()][sum] = incl || excl;
                }
            }
            return table[questionList.size()][sum];
        }
    }

    private static int getTotalTimesDPMemoDriver( List<Question> questionList, int sum ) {
        List<Question> solution = new ArrayList<>();
        Boolean[][] DPLookUp = new Boolean[questionList.size() + 1][sum + 1];
        List<Integer> totalTimes = new ArrayList<>();
        getTotalTimesDPMemo( solution, questionList, sum, DPLookUp, totalTimes );

        OptionalInt optionalMin = totalTimes.stream().mapToInt( Integer::intValue ).min();
        return optionalMin.isPresent() ? optionalMin.getAsInt() : 0;
    }

    /**
     * Recursively look up all qualified subsets of questions by examining the DP lookup table generated through tabulation, and then consolidate
     * their total amounts of time.
     */
    private static int getMinTimeDPTabu( List<Question> questionList, int sum ) {
        boolean[][] DPLookUp = new boolean[questionList.size() + 1][sum + 1];
        // base state
        for (int i = 0; i <= questionList.size(); i++) DPLookUp[i][0] = true;
        for (int i = 1; i <= sum; i++) DPLookUp[0][i] = false;
        // proliferation
        for (int i = 1; i <= questionList.size(); i++) {
            for (int j = 1; j <= sum; j++) {
                if (j < questionList.get( i - 1 ).points) DPLookUp[i][j] = DPLookUp[i - 1][j];
                else DPLookUp[i][j] = DPLookUp[i - 1][j] || DPLookUp[i - 1][j - questionList.get( i - 1 ).points];
            }
        }
        List<Question> solution = new ArrayList<>();
        List<Integer> totalTimes = new ArrayList<>();
        recursiveLookupAndGetTotalTime( solution, questionList.size(), DPLookUp, sum, questionList, totalTimes);

        OptionalInt optionalMin = totalTimes.stream().mapToInt( Integer::intValue ).min();
        return optionalMin.isPresent() ? optionalMin.getAsInt() : 0;
    }

    private static void recursiveLookupAndGetTotalTime( List<Question> solution, int itemIdx, boolean[][] table, int sumIdx, List<Question> questionList, List<Integer> totalTimes) {
        if (sumIdx == 0) {
            // sum achieved, sub set found, get total time
            totalTimes.add(solution.stream().map(q -> q.time).mapToInt( Integer::intValue ).sum());
        } else if (itemIdx == 0) {
        } // no solution
        else {
            if (table[itemIdx][sumIdx]) {
                ArrayList<Question> newSolution = new ArrayList<>( solution );
                newSolution.add( questionList.get( itemIdx - 1 ) );
                if (sumIdx < questionList.get( itemIdx - 1 ).points)
                    // exclude last element and recur
                    recursiveLookupAndGetTotalTime( solution, itemIdx - 1, table, sumIdx, questionList, totalTimes );
                else {
                    // include last element and recur
                    recursiveLookupAndGetTotalTime( newSolution, itemIdx - 1, table, sumIdx - questionList.get( itemIdx - 1 ).points, questionList, totalTimes );
                    // exclude last element and recur
                    recursiveLookupAndGetTotalTime( solution, itemIdx - 1, table, sumIdx, questionList, totalTimes);
                }
            }
        }
    }

    @FunctionalInterface
    protected interface QuestionArrayToBooleanFunction {
        boolean apply( List<Question> qList, int sum ) throws Exception;
    }

    protected static void runFuncAndCalculateTime( String message, QuestionArrayToBooleanFunction func, List<Question> qList, int sum ) throws Exception {
        long startTime = System.nanoTime();
        System.out.printf( "%-70s%s\n", message, func.apply( qList, sum ) );
        long endTime = System.nanoTime();
        long totalTime = TimeUnit.MICROSECONDS.convert( endTime - startTime, TimeUnit.NANOSECONDS );
        DecimalFormat formatter = new DecimalFormat( "#,###" );
        System.out.printf( "%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format( totalTime ) );
    }

    @FunctionalInterface
    protected interface QuestionArrayToIntFunction {
        int apply( List<Question> qList, int sum ) throws Exception;
    }

    protected static void runFuncAndCalculateTime( String message, QuestionArrayToIntFunction func, List<Question> qList, int sum ) throws Exception {
        long startTime = System.nanoTime();
        System.out.printf( "%-70s%s\n", message, func.apply( qList, sum ));
        long endTime = System.nanoTime();
        long totalTime = TimeUnit.MICROSECONDS.convert( endTime - startTime, TimeUnit.NANOSECONDS );
        DecimalFormat formatter = new DecimalFormat( "#,###" );
        System.out.printf( "%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format( totalTime ) );
    }

    public static void main( String[] args ) {
        System.out.println( "Welcome to the rabbit hole of problem points!\n" );

        final int target = 20;

        List<Question> questionList = new ArrayList<>();
        IntStream.range( 0, 5 )
                .forEach( $ -> questionList.add( new Question( FunIntAlgorithm.genRanInt( 1, 21 ), FunIntAlgorithm.genRanInt( 1, 10 ) ) ) );

        System.out.println( "Question list:" );
        questionList.forEach( q -> System.out.println( "Points: " + q.points + " " + "Time: " + q.time ) );
        System.out.println( "Target: " + target + "\n" );

        try {
            runFuncAndCalculateTime( "[Recursion][NP-complete]          Subset exists? ", MinTimeForFixedPoints::subsetExistsDriver, questionList, target );
            runFuncAndCalculateTime( "[Recursion][DP Memo]              Subset exists? ", MinTimeForFixedPoints::subsetExistsDPMemoDriver, questionList, target );
            runFuncAndCalculateTime( "[Recursion][DP Tabu]              Subset exists? ", MinTimeForFixedPoints::subsetExistsDPTabu, questionList, target );
            runFuncAndCalculateTime( "[Recursion][DP Memo]              Minimum time: ", MinTimeForFixedPoints::getTotalTimesDPMemoDriver, questionList, target );
            runFuncAndCalculateTime( "[Recursion][DP Tabu]              Minimum time: ", MinTimeForFixedPoints::getMinTimeDPTabu, questionList, target );
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println( "\nAll rabbits gone." );
    }

}
