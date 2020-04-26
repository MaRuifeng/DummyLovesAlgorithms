package backtracking;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Backtracking technique tackles a problem by trying out different solutions in an incremental and recursive manner
 * until a working solution is found. It's mostly used in solving problems that can only be solved by trying out
 * every possible solution configuration. 
 * 
 * The knight's tour problem states that starting from a random position, a knight piece must visit each square exactly 
 * once on an empty chess board, with its moving rule followed. Find all possible tour paths. 
 * 
 * @author ruifengm
 * @since 2018-Jun-12
 * 
 * https://www.geeksforgeeks.org/backtracking-set-1-the-knights-tour-problem/
 */
public class KnightTour {
	
	/* final variables used to denote a knight piece's move; used in parallel with correspondence */
	private static final int[] horizontalMoves = {1, 1, 2, 2, -1, -1, -2, -2}; 
	private static final int[] verticalMoves   = {2, -2, 1, -1, 2, -2, 1, -1};
	/**
	 * It's worth noting that the sequence in which the eight moves are inspected drastically affects the computation time
	 * towards a possible solution. 
	 * The worst case scenario time complexity is O(N^(N^2)). 
	 * Below are two arrays denoting the quickest way to find a solution for 8X8 chess board starting from square 1. 
	 */
//	private static final int[] horizontalMoves = {2, 1, -1, -2, -2, -1, 1, 2}; 
//	private static final int[] verticalMoves   = {1, 2, 2, 1, -1, -2, -2, -1};
	
	/**
	 * According to the knight piece's moving rule, there are maximum eight possible moves it can make at any 
	 * square. Starting from the first square (top-left corner) of the chess board, we iterate through all possible moves. 
	 * At any one of such moves, we recursively check if such a move leads to a complete solution. If yes, we print out the 
	 * complete solution when it's reached; if no, we remove that move from the solution path and try the next one. If none of such moves
	 * leads to a complete solution, we go back and start from the next starting square. 
	 */
	private static void solveKnightTour(int n) {
		int[][] board = new int[n][n];
		// For a randomized starting position, there might be a possibility that no such tour path exists. 
		int row = ThreadLocalRandom.current().nextInt(0, n), col = ThreadLocalRandom.current().nextInt(0, n);
		// int row = 0, col = 0;
		board[row][col] = 1; // first move
		System.out.println("Solutions:");
		if (nextMove(board, row, col) == false) System.out.println("No soluction exists.");
	}
	private static boolean nextMove(int[][] board, int curRow, int curCol) {
		int size = board.length;
		boolean moved = false; // denoting whether the move has been made as part of a complete solution
		if (board[curRow][curCol] == size*size) {
			printBoard(board);
			return true; // found solution
		}
		for (int i=0; i<8; i++) {
			int newRow = curRow + verticalMoves[i], newCol = curCol + horizontalMoves[i];
			if (movable(board, newRow, newCol)) {
				board[newRow][newCol] = board[curRow][curCol] + 1; // make a move as part of a possible complete solution
				if (nextMove(board, newRow, newCol)) {
					moved = true;              // solution can be completed
					board[newRow][newCol] = 0; // reset for next possible solution
					//break;                  // break to print one solution only
				} else board[newRow][newCol] = 0; // solution can't be completed, reset (backtracking)
			}
		}
		return moved;
	}
	/* Check if the knight can move to the designated position. */
	private static boolean movable(int[][] board, int row, int col) {
		int size = board.length;
		if (row<0 || row>=size || col<0 || col>=size) return false; 
		else if (board[row][col] != 0) return false;
		else return true;
	}
	private static void printBoard(int[][] board) {
		for (int[] row: board) {
			System.out.print("|");
			for (int i: row) System.out.printf("%-2d|", i);
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of knight tour puzzles! \n");
		
		/**
		 * Note that for a chess board of dimension 6, there will be a lot (really a lot) of 
		 * possible tour paths. Such path count remains an interesting problem to be explored. 
		 */
		for (int i=1; i<=5; i++) {
			System.out.println("Chess board dimension: " + i);
			solveKnightTour(i);
			System.out.println("**********************************");
		}
		
		System.out.println("All rabbits gone.");
	}
	

}
