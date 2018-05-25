# Dummy Loves Algorithms and Data Structures

## Description

This is a repository where I keep all interesting algorithms and data structures I came across either in work or learning. 

Lack of a well-trained background in computer science always pushed me back in taking time to catch up in algorithms and data structures. But that is indeed
the area where a programmer trains his brain and align with his beloved machine. 

## Requirements

Java 8 is required so as to support the functional interface and Lambda expressions used. 

## How

Each package contains an interesting algorithmic question with solution provided. 

* Run it as a Java application in a common IDE like Eclipse (make sure Java 8 is supported). This provides flexibility to modify code. 
* Key function calls will be provided with roughly estimated running time so as to cross check with the indicated time complexity.

## List of algorithmic questions
* [Find the largest sub-array sum of a given integer array](./src/integerArray/maxSubsequenceSum/MaxSubseqSum.java)
* [Find the largest absolute difference of two non-overlapping sub-arrays of a given integer array](./src/integerArray/maxSubsequenceDiff/MaxSubseqDiff.java)
* [Find total number of reverse pairs in a given integer array](./src/integerArray/numOfReversePairs/NumOfReversePairs.java)
* [Find the largest sum of a sub-array with size K in a given integer array](./src/integerArray/maxSubarraySumOfSizeK/MaxSubArraySumOfSizeK.java)
* [Find the k'th smallest item from a given array of distinct integers](./src/integerArray/kthSmallestElementInArray/kthSmallestElementInArray.java)
* [Find number pairs each of which sums up to a fixed value from an integer array](./src/integerArray/NumberPairOfFixedSum.java)
* [**All hail Recursion!** Find required add and subtract operations to produce a fixed sum](./src/integerArray/MathOpsForFixedSum.java)
* [Find the greatest common divisor of an array of integers](./src/integerArray/GCDOfIntegerArray.java)
* [Print numbers along matrix diagonals](./src/integerArray/DiagonalNumberMatrix.java)
* [Print spiral numbers](./src/integerArray/SpiralNumber.java)
* [Bitwise operations](./src/integerArray/BitOperators.java)
* [Merge sorted integer arrays](./src/integerArray/SortedArrayMerger.java)
* [**All hail Recursion!** Find all subsets of an integer set](./src/integerArray/SubsetSeeker.java)
* [Patch a given array to produce list of sums from 1 to N](./src/integerArray/IntegerArrayPatcher.java)
* [Binary search in a sorted array rotated at an unknown pivot](./src/integerArray/BinarySearchInRotatedSortedArray.java)
* [Dynamic Programming](./src/dynamicProgramming)
  * [Fibonacci Numbers](./src/dynamicProgramming/FibNumbers.java)
  * [Binomial Coefficients](./src/dynamicProgramming/BinomialCoefficients.java)
  * [Catalan Numbers **Find all expressions of balanced n pairs of parentheses**](./src/dynamicProgramming/CatalanNumbers.java)
  * [Coin change problems](./src/dynamicProgramming/CoinKeeper.java)
    * Number of possible combinations for given sum
    * List of possible combinations for given sum
    * Least number of coins needed for given sum
  * [Number arrangements to get a fixed sum](./src/dynamicProgramming/NumberOrganizer.java)
  * [Maximum sub-array sum](./src/integerArray/maxSubsequenceSum/MaxSubseqSum.java)
  * [Ugly Numbers](./src/dynamicProgramming/UglyNumbers.java)
  * [Maximum stock profit](./src/dynamicProgramming/StockProfit.java)
  * [Digit sequence decoder](./src/dynamicProgramming/DigitSequenceDecoder.java)
  * [Longest common subsequence](./src/dynamicProgramming/LongestCommonSebsequence.java)
  * [Longest common substring](./src/dynamicProgramming/LongestCommonSubstring.java)
  * [Longest increasing subsequence](./src/dynamicProgramming/LongestIncreasingSubsequence.java)
  * [Edit distance algorithm](./src/dynamicProgramming/MinStringEdits.java)
  * [Set partition for minimum sum difference](./src/dynamicProgramming/MinimumSetPartition.java)
  * [Distance and steps](./src/dynamicProgramming/DistanceTraveller.java)
  * [Longest path in matrix](./src/dynamicProgramming/LongestPathInMatrix.java)
  * [**NP-Complete** Subset of fixed sum](./src/dynamicProgramming/SubsetOfFixedSum.java)
  * [Optimal coin game strategy](./src/dynamicProgramming/CoinGameStrategy.java)
  * [Minimum insertions for palindrome](./src/dynamicProgramming/MinimumPalindromeInsertion.java)
  * [**NP-Complete** 0-1 Knapsack problem](./src/dynamicProgramming/KnapsackPacker.java)
  * [Wildcard pattern match check](./src/dynamicProgramming/WildcardMatching.java)
  * [**Beware of integer overflow!** Boolean parenthesization](./src/dynamicProgramming/BooleanParenthesization.java)
  * [**DP Enhancement** Find required add and subtract operations to produce a fixed sum](./src/integerArray/MathOpsForFixedSum.java)
  * [Shortest common supersequence](./src/dynamicProgramming/ShortestCommonSupersequence.java)
  * [Least arithmetic operations for matrix chain multiplication](./src/dynamicProgramming/MatrixChainMultiplication.java)
* [Remove duplicates in string](./src/string/removeDuplicates/DuplicatesRemover.java)
* [Remove alternate duplicates in string](./src/string/removeDuplicates/AlternateDuplicatesRemover.java)
* [Remove adjacent duplicates in string](./src/string/removeDuplicates/AdjacentDuplicatesRemover.java)
* [Remove consecutive duplicates in string](./src/string/removeDuplicates/ConsecutiveDuplicatesRemover.java)
* [Longest valid parentheses substring](./src/string/LongestValidParentheses.java)
* [Parentheses checker](./src/string/ParenthesisChecker.java)
* [In-place string rotator](./src/string/StringRotator.java)
* [Binary Tree](./src/binaryTree)
  * [Plain Binary Tree implementation with linked nodes](./src/binaryTree/entities/BinaryTree.java)
    * Pre-order Traversal
    * Post-order Traversal
    * Level-order Traversal
    * Clone via pre-order traversal
    * Serialization/de-serialization
  * [Plain Binary Search Tree implementation with linked nodes](./src/binaryTree/entities/BinarySearchTree.java)
  * [Parse a BST from its pre-order traversal](./src/binaryTree/BSTParserFromPreorderTraversal.java)
  * [Parse a BST from its post-order traversal](./src/binaryTree/BSTParserFromPostorderTraversal.java)
  * [Parse a BST from its level-order traversal](./src/binaryTree/BSTParserFromLevelOrderTraversal.java)
  * [Convert a BT to a BST with tree structure maintained](./src/binaryTree/BTtoBSTConverter.java)
  * [**Divide and Conquer** Search range in BST](./src/binaryTree/SearchRangeInBST.java)
  * [Sum up greater keys with each node in BST](./src/binaryTree/SumWithGreaterKeysInBST.java)
  * [Sum up greater keys for each node in BST](./src/binaryTree/BSTtoGreaterSumTreeConverter.java)
  * [Convert integer array to BST](./src/binaryTree/ArrayToBSTConverter.java)
  * [Convert integer linked list to BST](./src/binaryTree/LinkedListToBSTConverter.java)
  * [All possible BSTs from 1 to N **Catalan Numbers**](./src/binaryTree/AllPossibleBSTsFromOneToN.java)
  * [Convert BST to min heap](./src/binaryTree/BSTtoMinHeapConverter.java)
  * [Convert BT to doubly linked list](./src/binaryTree/BTtoDoublyLinkedListConverter.java)
  * [Convert complete BST to conditional min heap where left sub-tree smaller than right sub-tree](./src/binaryTree/BSTtoConditionalMinHeapConverter.java)
* [Plain Binary Heap implementation with array](./src/binaryHeap/MinHeap.java)

## Background

This all started with a day when I accidentally picked a book my university professor passed me when I graduated. 

*Data Structures & Algorithm Analysis in Java* by *Weiss, Mark Allen* (ISBN 0-201-35754-2). It's a textbook for first-year graduate student in computer 
science so it should be a good starting point.

A lot of examples in this repository will be from this book.  

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


