package integerArray;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Question 1: given a random integer generating function rand9() that generates any integer in the range of 
 *             [1, 9] with equal probability, how can this function be used to generate integers in the range of 
 *             [1, 7]?
 * 
 *           
 * Question 2: given a random integer generating function rand5() that generates any integer in the range of 
 *             [1, 5] with equal probability, how can this function be used to generate integers in the range of 
 *             [1, 7]?
 *             
 * @author ruifengm
 * @since 2018-Jun-4 
 * 
 * http://lifeofzjs.com/blog/2017/01/07/how-to-generate-uniform-distribution/
 * https://stackoverflow.com/questions/137783/expand-a-random-range-from-1-5-to-1-7
 */

public class RandNumberGenerator {
	
	private static int randM(int M) {
		return ThreadLocalRandom.current().nextInt(1, M+1); 
	}
	
	/**
	 * [Question 1]
	 * Often an intuitive solution is to do rand9() % 7, which is actually not correct. E.g. the probability of getting number 3 is 1/9, whereas 
	 * the probability of getting number 2 is 2/9, because both 2%7 and 9%7 return number 2. 
	 * 
	 * Since the rand9() function has a higher entropy, we need to reduce that entropy for rand7(). 
	 * 
	 * Let the random function of higher entropy be randM(), and the other be randN(), 
	 * the idea is to divide the range of randM(), [1, M],  into N equally sized buckets, with a remainder whose size is smaller
	 * than N. When we run the randM() function, if the generated number falls into a bucket, we return that the index of that 
	 * bucket; if it falls into the remainder section, we need to run randM() again and again until it falls into a bucket. 
	 * 
	 * The solution has a chance of entering an infinite loop, but statistically it's very small and converges to 0 as the loops run towards infinity. 
	 * @throws Exception 
	 */
	private static int randBasedOnHigherEntropy(int M, int N) throws Exception {
		if (M <= N) throw new Exception("Not allowed for this method! M should be larger than N.");
		int bucketSize = M/N; 
		int remainder = M%N; 
		int res; 
		do {
			res = randM(M); 
		} while (res > M - remainder); 
		return res/bucketSize;
	}
	
	/**
	 * [Question 2]
	 * In this case we need to increase the entropy so as to cover the longer target range. 
	 * Not sure there is a general solution to the random function based on lower entropy ones.
	 */
	private static int rand7BasedOnRand5() {
		int res; 
		do {
			res = 5 * (randM(5) - 1) + randM(5); // res uniformly distributed from 1 to 25
		} while (res > 21);                      // res uniformly distributed from 1 to 21
		return res%7 + 1;                        // res uniformly distributed from 1 to 7
	}
	
	/**
	 * A clearer and easier to understand but equivalent solution is as below. 
	 * A good analogy of it is to think of the 2-D array as a dart board, and we keep on throwing 
	 * darts to it until we hit targets we need. 
	 */
	private static int rand7BasedOnRand5Clever() {
	   int[][] vals = {
		                { 1, 2, 3, 4, 5 },
		                { 6, 7, 1, 2, 3 },
		                { 4, 5, 6, 7, 1 },
		                { 2, 3, 4, 5, 6 },
		                { 7, 0, 0, 0, 0 }
		              };

		    int result = 0;
		    while (result == 0)
		    {
		        int i = randM(5);
		        int j = randM(5);
		        result = vals[i-1][j-1];
		    }
		    return result;
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of egg droppers! \n"); 
		
		for (int i= 0; i<1; i++) {
			System.out.println("Generating a random integer from 0 to 7 using a rand9() function...");
			try {
				System.out.println(randBasedOnHigherEntropy(9, 7));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n**********\n");
		for (int i= 0; i<1; i++) {
			System.out.println("Generating 2 random integers from 0 to 7 using a rand5() function...");
			System.out.println(rand7BasedOnRand5()); 
			System.out.println(rand7BasedOnRand5Clever());
		}
		
		System.out.println("All rabbits gone.");
	}
}
