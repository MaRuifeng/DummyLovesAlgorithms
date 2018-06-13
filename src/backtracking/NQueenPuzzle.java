package backtracking;

/**
 * In the international chess game, the Queen is the most powerful piece 
 * who is able to move any number of squares vertically, horizontally or diagonally on the chess board. 
 * 
 * Suppose we have an N X N chess board, and N queen pieces, place all of them on the chess board 
 * in a way that no two queens can attack each other. 
 * 
 * Find all possible solutions.
 * 
 * @author ruifengm
 * @since 2018-Jun-10
 * 
 * https://www.geeksforgeeks.org/backtracking-set-3-n-queen-problem/
 *
 */
public class NQueenPuzzle {
	
	/**
	 * The problem can be attempted with the backtracking strategy.
	 * 
	 * Starting from the leftmost column, we try to put a queen piece in every row. If the queen piece
	 * can be safely put in a row, we mark the column and row indices as part of the solution and recursively
	 * move on to check if such a placement leads to a complete solution. If yes, we return true; if not, we unmark 
	 * those indices and try the next row. If all rows have been tried and no solution found, we return false and 
	 * move on with the next column. 
	 */
	private static void solveNQueenPuzzle(int N) {
		char[][] board = new char[N][N]; 
		for (int i=0; i<N; i++)
			for (int j=0; j<N; j++) board[i][j] = '_';
		System.out.println("Solutions:");
		if (queenPlacedInCol(board, 0) == false) System.out.println("No soluction exists.");
	}
	private static boolean queenPlacedInCol(char[][] board, int col) {
		int N = board.length;
		boolean placed = false; // denoting whether a queen has been placed in the column as part of a complete solution
		if (col == N) { // all queens are properly placed
			printBoard(board);
			return true; 
		}
		for (int i=0; i<N; i++) { // check all rows for current column
			if (safetyCheck(board, i, col)) {
				board[i][col] = 'Q'; // mark queen location to form a possible solution
				if (queenPlacedInCol(board, col+1) == true) {
					placed = true;       // the solution can be completed
					board[i][col] = '_'; // unmark the queen location for next possible solution
					// break;            // break to obtain one solution only
				} else { // unmark the queen location as the solution can't be completed (backtracking)
					board[i][col] = '_'; 
				}
			}
		}
		return placed;
	}
	/* Check if a queen can be placed at given location with left hand side of the board already filled */
	private static boolean safetyCheck(char[][] board, int row, int col) { 
		int N = board.length;
		// check row horizontally
		for (int i=0; i<col; i++) {
			if (board[row][i] == 'Q') return false; 
		}
		// check upper left diagonally
		for (int i=row, j=col; i>=0 && j>=0; i--, j--) {
			if (board[i][j] == 'Q') return false;
		}
		// check lower left diagonally
		for (int i=row, j=col; i<N && j>=0; i++, j--) {
			if (board[i][j] == 'Q') return false;
		}
		return true; 
	}
	private static void printBoard(char[][] board) {
		for (char[] row: board) {
			System.out.print("|");
			for (char c: row) System.out.print(c + "|");
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of N-queen puzzles! \n");
		
		for (int i=1; i<=8; i++) {
			System.out.println("Chess board dimension: " + i);
			System.out.println("Queen count: " + i);
			solveNQueenPuzzle(i);
			System.out.println("**********************************");
		}
		
		System.out.println("All rabbits gone.");
	}

}
