# Dummy Loves Algorithms and Data Structures

## Description

This is a repository where I keep all interesting algorithms and data structures I came across either in work or learning. 

Lack of a well-trained background in computer science always pushed me back in taking time to catch up in algorithms and data structures. But that is indeed
the area where a programmer shines and aligns the thinking process with his beloved machine. Behind the general software engineering principles, it's the carefully designed algorithms and data structures in vaiours code snippets that make the overall product performant and wonderful. 

## Requirements

Java 8 is required so as to support the functional interface and Lambda expressions used. 

## How

Each package contains an interesting algorithmic question with solution provided. 

* Run it as a Java application in a common IDE like Eclipse (make sure Java 8 is supported). This provides flexibility to modify code. 
* Key function calls will be provided with roughly estimated running time so as to cross check with the indicated time complexity.

## List of algorithmic questions
* [Integer Array](./src/main/java/integerArray)
	* [Find the largest sub-array sum of a given integer array](./src/main/java/integerArray/maxSubsequenceSum/MaxSubseqSum.java)
	* [Find the largest absolute difference of two non-overlapping sub-arrays of a given integer array](./src/main/java/integerArray/maxSubsequenceDiff/MaxSubseqDiff.java)
	* [Find total number of reverse pairs in a given integer array](./src/main/java/integerArray/numOfReversePairs/NumOfReversePairs.java)
	* [Find the largest sum of a sub-array with size K in a given integer array](./src/main/java/integerArray/maxSubarraySumOfSizeK/MaxSubArraySumOfSizeK.java)
	* [Find the k'th smallest item from a given array of distinct integers](./src/main/java/integerArray/kthSmallestElementInArray/kthSmallestElementInArray.java)
	* [Find number pairs each of which sums up to a fixed value from an integer array](./src/main/java/integerArray/NumberPairOfFixedSum.java)
	* [**All hail Recursion!** Find required add and subtract operations to produce a fixed sum](./src/main/java/dynamicProgramming/MathOpsForFixedSum.java)
	* [Find the greatest common divisor of an array of integers](./src/main/java/integerArray/GCDOfIntegerArray.java)
	* [Print numbers along matrix diagonals](./src/main/java/integerArray/DiagonalNumberMatrix.java)
	* [Print spiral numbers](./src/main/java/integerArray/SpiralNumber.java)
	* [Bitwise operations](./src/main/java/integerArray/BitOperators.java)
	* [Merge sorted integer arrays](./src/main/java/integerArray/SortedArrayMerger.java)
	* [**All hail Recursion!** Find all subsets of an integer set](./src/main/java/integerArray/SubsetSeeker.java)
	* [Patch a given array to produce list of sums from 1 to N](./src/main/java/integerArray/IntegerArrayPatcher.java)
	* [Binary search in a sorted array rotated at an unknown pivot](./src/main/java/integerArray/BinarySearchInRotatedSortedArray.java)
	* [**All hail Recursion!** Number permutation](./src/main/java/integerArray/NumberPermutation.java)
	* [**All hail Recursion!** Number combination](./src/main/java/integerArray/NumberCombination.java)
	* [Random number with different entropies](./src/main/java/integerArray/RandNumberGenerator.java)
	* [Binary search in matrix](./src/main/java/integerArray/BinarySearchInMatrix.java)
	* [**Multiple-Condition Check** Insert intervals](./src/main/java/integerArray/Intervals.java)
	* [Reverse an integer number](./src/main/java/integerArray/ReverseInteger.java)
	* [Recover a rotated sorted array](./src/main/java/integerArray/RecoverRotatedSortedArray.java)
	* [**NP Hard** Bin packing problem](./src/main/java/integerArray/BinPacker.java)
	* [**Boyer–Moore Majority Vote Algorithm** Find majority numbers](./src/main/java/integerArray/MajorityNumber.java)
	* [Monkey crossing river](./src/main/java/integerArray/MonkeyAndRiver.java)
	* [Number of ways to sum up to a given number with consecutive positive integers](./src/main/java/integerArray/SumOfConsecutiveInt.java)
	* [Array based circular queue](./src/main/java/integerArray/CircularArrayQueue.java)
* [Linked List](./src/main/java/linkedList)
    * [Reverse a linked list](./src/main/java/linkedList/ReverseLinkedList.java)
    * [LRU cache implemented with HashMap and LinkedList](./src/main/java/linkedList/LRUCache.java)
    * [LRU cache implemented plainly](./src/main/java/linkedList/LRUCachePlain.java)
* [Dynamic Programming](./src/main/java/dynamicProgramming)
  * [Fibonacci Numbers](./src/main/java/dynamicProgramming/FibNumbers.java)
  * [Binomial Coefficients](./src/main/java/dynamicProgramming/BinomialCoefficients.java)
  * [Catalan Numbers **Find all expressions of balanced n pairs of parentheses**](./src/main/java/dynamicProgramming/CatalanNumbers.java)
  * [Coin change problems](./src/main/java/dynamicProgramming/CoinKeeper.java)
    * Number of possible combinations for given sum
    * List of possible combinations for given sum
    * Least number of coins needed for given sum
  * [Number arrangements to get a fixed sum](./src/main/java/dynamicProgramming/NumberOrganizer.java)
  * [Maximum sub-array sum](./src/main/java/integerArray/maxSubsequenceSum/MaxSubseqSum.java)
  * [Ugly Numbers](./src/main/java/dynamicProgramming/UglyNumbers.java)
  * [Maximum stock profit](./src/main/java/dynamicProgramming/StockProfit.java)
  * [Digit sequence decoder](./src/main/java/dynamicProgramming/DigitSequenceDecoder.java)
  * [Longest common subsequence](./src/main/java/dynamicProgramming/LongestCommonSebsequence.java)
  * [Longest common substring](./src/main/java/dynamicProgramming/LongestCommonSubstring.java)
  * [Longest increasing subsequence](./src/main/java/dynamicProgramming/LongestIncreasingSubsequence.java)
  * [Edit distance algorithm](./src/main/java/dynamicProgramming/MinStringEdits.java)
  * [Set partition for minimum sum difference](./src/main/java/dynamicProgramming/MinimumSetPartition.java)
  * [Distance and steps](./src/main/java/dynamicProgramming/DistanceTraveller.java)
  * [Longest path in matrix](./src/main/java/dynamicProgramming/LongestPathInMatrix.java)
  * [**NP-Complete** Subset of fixed sum](./src/main/java/dynamicProgramming/SubsetOfFixedSum.java)
  * [Optimal coin game strategy](./src/main/java/dynamicProgramming/CoinGameStrategy.java)
  * [Minimum insertions for palindrome](./src/main/java/dynamicProgramming/MinimumPalindromeInsertion.java)
  * [**NP-Complete** 0-1 Knapsack problem](./src/main/java/dynamicProgramming/KnapsackPacker.java)
  * [Wildcard pattern match check](./src/main/java/dynamicProgramming/WildcardMatching.java)
  * [**Beware of integer overflow!** Boolean parenthesization](./src/main/java/dynamicProgramming/BooleanParenthesization.java)
  * [**DP Enhancement** Find required add and subtract operations to produce a fixed sum](./src/main/java/dynamicProgramming/MathOpsForFixedSum.java)
  * [Shortest common supersequence](./src/main/java/dynamicProgramming/ShortestCommonSupersequence.java)
  * [Least arithmetic operations for matrix chain multiplication](./src/main/java/dynamicProgramming/MatrixChainMultiplication.java)
  * [**`equals` and `hashCode` method override** Rod cutting for maximum gain](./src/main/java/dynamicProgramming/RodCutter.java)
  * [Resolve a string with dictionary words](./src/main/java/dynamicProgramming/StringResolver.java)
  * [Rope cutting for maximum length product](./src/main/java/dynamicProgramming/RopeCutter.java)
  * [Dice rolling for fixed sum](./src/main/java/dynamicProgramming/DiceRoller.java)
  * [Egg dropping puzzle](./src/main/java/dynamicProgramming/EggDropper.java)
  * [K non-overlapping subarrays of maximum sum](./src/main/java/dynamicProgramming/MaxKSubArraySum.java)
  * [LEGO part assembler](./src/main/java/dynamicProgramming/LegoAssembler.java)
  * [**NP-Complete** Minimum time for fixed sum of points](./src/main/java/dynamicProgramming/MinTimeForFixedPoints.java)
* [String](./src/main/java/string)
  * [Remove duplicates in string](./src/main/java/string/removeDuplicates/DuplicatesRemover.java)
  * [Remove alternate duplicates in string](./src/main/java/string/removeDuplicates/AlternateDuplicatesRemover.java)
  * [Remove adjacent duplicates in string](./src/main/java/string/removeDuplicates/AdjacentDuplicatesRemover.java)
  * [Remove consecutive duplicates in string](./src/main/java/string/removeDuplicates/ConsecutiveDuplicatesRemover.java)
  * [Longest valid parentheses substring](./src/main/java/string/LongestValidParentheses.java)
  * [Parentheses checker](./src/main/java/string/ParenthesisChecker.java)
  * [In-place string rotator](./src/main/java/string/StringRotator.java)
  * [**Backtracking** String permutation](./src/main/java/string/StringPermutation.java)
  * [**KMP Algorithm** Search for patterns in string](./src/main/java/string/StringPatternSearch.java)
  * [**DP** Check interleaved string](./src/main/java/string/InterleavedString.java)
  * [Minimum window substring](./src/main/java/string/MinimumWindowSubstring.java)
  * [IPv4 address regex matcher](./src/main/java/string/IPv4AddressMatcher.java)
  * [**Functional Programming** Credit card number Luhn check](./src/main/java/string/CreditCardNumberValidator.java)
  * [String to int converter](./src/main/java/string/StringToIntConverter.java)
* [Binary Tree](./src/main/java/binaryTree)
  * [Plain Binary Tree implementation with linked nodes](./src/main/java/binaryTree/entities/BinaryTree.java)
    * Pre-order Traversal
    * Post-order Traversal
    * Level-order Traversal
    * Clone via pre-order traversal
    * Serialization/de-serialization
  * [Plain Binary Search Tree implementation with linked nodes](./src/main/java/binaryTree/entities/BinarySearchTree.java)
  * [Parse a BST from its pre-order traversal](./src/main/java/binaryTree/BSTParserFromPreorderTraversal.java)
  * [Parse a BST from its post-order traversal](./src/main/java/binaryTree/BSTParserFromPostorderTraversal.java)
  * [Parse a BST from its level-order traversal](./src/main/java/binaryTree/BSTParserFromLevelOrderTraversal.java)
  * [Convert a BT to a BST with tree structure maintained](./src/main/java/binaryTree/BTtoBSTConverter.java)
  * [**Divide and Conquer** Search range in BST](./src/main/java/binaryTree/SearchRangeInBST.java)
  * [Sum up greater keys with each node in BST](./src/main/java/binaryTree/SumWithGreaterKeysInBST.java)
  * [Sum up greater keys for each node in BST](./src/main/java/binaryTree/BSTtoGreaterSumTreeConverter.java)
  * [Convert integer array to BST](./src/main/java/binaryTree/ArrayToBSTConverter.java)
  * [Convert integer linked list to BST](./src/main/java/binaryTree/LinkedListToBSTConverter.java)
  * [All possible BSTs from 1 to N **Catalan Numbers**](./src/main/java/binaryTree/AllPossibleBSTsFromOneToN.java)
  * [Convert BST to min heap](./src/main/java/binaryTree/BSTtoMinHeapConverter.java)
  * [Convert BT to doubly linked list](./src/main/java/binaryTree/BTtoDoublyLinkedListConverter.java)
  * [Convert complete BST to conditional min heap where left sub-tree smaller than right sub-tree](./src/main/java/binaryTree/BSTtoConditionalMinHeapConverter.java)
* [Binary Heap](./src/main/java/binaryHeap)
  * [Plain Binary Heap implementation with array](./src/main/java/binaryHeap/MinHeap.java)
  * [Heap Sort](./src/main/java/binaryHeap/HeapSort.java)
  * [Least Frequently Used Cache implementation via PriorityQueue](./src/main/java/binaryHeap/LFUCache.java)
* [Graph](./src/main/java/graph)
  * [Plain Directed Graph implementation with adjacency list](./src/main/java/graph/GraphAdjacencyList.java)
    * Breadth First Search
    * Depth First Search
    * Linear paths from source to destination
* [Backtracking Technique](./src/main/java/backtracking)
  * [N-Queen puzzle](./src/main/java/backtracking/NQueenPuzzle.java)
  * [Knight tour problem](./src/main/java/backtracking/KnightTour.java)

## Background

This all started with a day when I accidentally picked a book my university professor passed me when I graduated. 

*Data Structures & Algorithm Analysis in Java* by *Weiss, Mark Allen* (ISBN 0-201-35754-2). It's a textbook for first-year graduate student in computer 
science so it should be a good starting point.

Well eventually it turned out that reading book is rather slow and solving problems can be more challenging and rewarding. Hence eventually this repository became a place where I keep my practice and notes. Each question comes with a detailed explanation in code comments. When I do coding practice on LintCode, LeetCode or Geeks For Geeks and encounter certain typical and interesting problems, I will add them to the list here.   

## Owners
Author: Ruifeng Ma

Email: mrfflyer@gmail.com

## Contributing

1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write your change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Submit a Pull Request using Github


