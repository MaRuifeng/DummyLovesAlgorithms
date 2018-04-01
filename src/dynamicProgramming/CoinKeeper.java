package dynamicProgramming;


/**
 * A coin keeper has three types of coins whose values are 1, 2, and 5 respectively, and he possesses infinite number of 
 * such coins (rich as the person who has infinite dollars). 
 * 
 * He is so rich that he has nothing to do. So one day he comes up with a problem: given a finite money value, find the total number of all 
 * possible combinations of his coins that can sum up to the value.
 * 
 *  
 * @author ruifengm
 * @since 2015-Mar-17
 */
public class CoinKeeper {
	
	private static final int coin_1_val = 1; 
	private static final int coin_2_val = 2; 
	private static final int coin_5_val = 5; 
	
	/**
	 * This problem can be solved by the DP technique. 
	 * Let state(n) be the number of total combinations when the money sum is n, so we have
	 * state(0) = 0
	 * state(1) = 1    {1}
	 * state(2) = 2    {1+1, 2}
	 * state(3) = 2    {1+1+1, 1+2}
	 * state(4) = 3    {1+1+1+1, 1+2+1, 2+2}
	 * state(5) = 4    {1+1+1+1+1, 1+2+1+1, 2+2+1, 5}
	 * To get state(5), since we already know above, and the value can be added is only 1, 2 and 5, we can deduce that
	 * 
	 * 
	 * 
	 * @param moneySum
	 * @return
	 */
	private static long recursiveCountCoin(int moneySum) {
		// TODO
		
	}

}
