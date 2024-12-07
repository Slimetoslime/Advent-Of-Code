package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day7 {
	
	/** Node class for binary tree.
	 * 
	 */
	static class Node {
		private long value;
		private Node left;
		private Node right;
		private Node middle;
		
		/** Node constructor.
		 * 
		 * @param value Value of the node.
		 */
		Node(long value) {
			this.value = value;
			left = null;
			right = null;
			middle = null;
		}
		
		/** Returns a new node where 3 new node branches with differing values has been inserted using recursion.
		 * 
		 * @param current The current node.
		 * @param value Value for the operation.
		 * @param includeConcat Boolean value if concatenation the numbers is considered.
		 * @return An updated node with new leaf nodes.
		 */
		public static Node addRecursiveNode(Node current, long value, boolean includeConcat) {
			if (current.left == null) {
				current.left = new Node(current.value * value);
				current.right = new Node(current.value + value);
				if (includeConcat) current.middle = new Node(Long.valueOf(Long.toString(current.value) + Long.toString(value)));
			} else {
				addRecursiveNode(current.left, value, includeConcat);
				addRecursiveNode(current.right, value, includeConcat);
				if (includeConcat) addRecursiveNode(current.middle, value, includeConcat);
			}
			return current;
		}
		
		/** Returns a list of integers from leaf nodes, given a certain node as the root.
		 * 
		 * @param current Current Node.
		 * @param includeConcat Boolean value of whether or not to include concatenation as an operator.
		 * @return Returns a list of integers containing the leaf nodes values.
		 */
		public static List<Long> getRecursiveLeafNodes(Node current, boolean includeConcat) {
			List<Long> result = new ArrayList<>();
			if (current.left == null) {
				result.add(current.value);
			} else {
				result.addAll(getRecursiveLeafNodes(current.left, includeConcat));
				result.addAll(getRecursiveLeafNodes(current.right, includeConcat));
				if (includeConcat) result.addAll(getRecursiveLeafNodes(current.middle, includeConcat));
			}
			return result;
		}
		
		/** Printer code for a Node and its children.
		 * 
		 */
		public String toString() {
	        StringBuilder buffer = new StringBuilder(50);
	        print(buffer, "", "");
	        return buffer.toString();
	    }

	    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
	        buffer.append(prefix);
	        buffer.append(value);
	        buffer.append('\n');
	        List<Node> childrenNodes = new ArrayList<>(Arrays.asList(this.left, this.middle, this.right));
	        childrenNodes.removeAll(Collections.singleton(null));
	        for (Iterator<Node> it = childrenNodes.iterator(); it.hasNext();) {
	        	Node next = it.next();
	            if (it.hasNext()) {
	                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
	            } else {
	                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
	            }
	        }
	    }
	}
	
	/** Binary tree implementation.
	 * 
	 */
	public static class BinaryTree {
		
		private static Node root;
		
		/** Constructor for a binary tree to solve this problem.
		 * 
		 * @param val Value of the root node.
		 */
		public BinaryTree(long val) {
			root = new Node(val);
		}
		
		/** Updates the root node to include new leaf nodes from operations with the value.
		 * 
		 * @param value Value to operate on.
		 * @param includeConcat Boolean value to include concatenation as an operator.
		 */
		public void add(Long value, boolean includeConcat) {
			root = Node.addRecursiveNode(root, value, includeConcat);
		}
		
		/** Returns the leaf nodes of every 
		 * 
		 * @param includeConcat
		 * @return
		 */
		public List<Long> getLeafNodes(boolean includeConcat) {
			return Node.getRecursiveLeafNodes(root, includeConcat);
		}
		
		@Override
		public String toString() {
			return root.toString();
		}
	}
	
	/** Hash map to format the input. Key is the target number and the value is a list of available numbers.
	 *  Second hash map to store the pairs that were not valid in Part 1.
	 */
	private static Map<Long, List<Long>> countdownState = new HashMap<>();
	private static Map<Long, List<Long>> failedPartOne = new HashMap<>();
	
	/** Runner code.
	 * 
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day7.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		
		for (String round: puzzle) {
			String[] info = round.split(": ");
			countdownState.put(Long.valueOf(info[0]), Arrays.asList(info[1].split(" ")).stream().mapToLong(Long::valueOf).boxed().toList());
		}
		
		Part2.part();
	}
	
	/**
 	* =========================================================================
 	* START OF PART 1
 	* =========================================================================
 	*/

	class Part1 {

		/**
		 * Solves the first part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static long part() {
			List<Long> validTrees = new ArrayList<>(); 
			
			for (Map.Entry<Long, List<Long>> entry: countdownState.entrySet()) {
				long target = entry.getKey();
				List<Long> availableNums = entry.getValue();
				BinaryTree tree = new BinaryTree(availableNums.get(0));
				
				for (int i = 1; i < availableNums.size(); i++) {
					tree.add(availableNums.get(i), false);
				}
				
				if (tree.getLeafNodes(false).contains(target)) {
					validTrees.add(target);
				} else {
					failedPartOne.put(target, availableNums);
				}
			}
			
			long sum = validTrees.stream().mapToLong(Long::valueOf).sum();
			System.out.println("Part 1: " + sum);
			return sum;
		}
	}
	
	/**
	 * =========================================================================
	 * START OF PART 2
	 * =========================================================================
	 */

	class Part2 {

		/**
		 * Solves the second part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part() {
			List<Long> validTrees = new ArrayList<>();
			long partOneResult = Part1.part();
			
			for (Map.Entry<Long, List<Long>> entry: failedPartOne.entrySet()) {
				long target = entry.getKey();
				List<Long> availableNums = entry.getValue();
				BinaryTree tree = new BinaryTree(availableNums.get(0));
				
				for (int i = 1; i < availableNums.size(); i++) {
					tree.add(availableNums.get(i), true);
				}
				
				if (tree.getLeafNodes(true).contains(target)) {
					validTrees.add(target);
				}
			}
			
			// Solution is still a bit slow. I just improved it a bit by reducing the number of trees it has to check.
			long result = partOneResult + validTrees.stream().mapToLong(Long::valueOf).sum();
			System.out.println("Part 2: " + result);
		}
	}
}


