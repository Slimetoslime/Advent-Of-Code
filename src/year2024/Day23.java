package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day23 {
	
	private record Node(String label) {
		@Override
		public String toString() {
			return label;
		}
	}
	
	static class Graph {
		private Map<Node, List<Node>> nNodes;

		public Graph() {
			nNodes = new HashMap<>();
		}
		
		void addNodes(String[] labels) {
			for (String s: labels) {
				nNodes.putIfAbsent(new Node(s), new ArrayList<>());
			}
		} 
		
		void addEdges(String label1, String label2) {
			Node n1 = new Node(label1);
			Node n2 = new Node(label2);
			nNodes.get(n1).add(n2);
			nNodes.get(n2).add(n1);
		}
		
		List<Node> getNeighbors(String label) {
			return nNodes.get(new Node(label));
		}
		
		List<Node> getNeighbors(Node label) {
			return nNodes.get(label);
		}
		
		Set<Set<Node>> getTriads() {
			Set<Set<Node>> triads = new HashSet<>();
			
			for (Node middle: nNodes.keySet()) {
				for (Node left: this.getNeighbors(middle)) {
					for (Node right: this.getNeighbors(middle)) {
						if (!left.equals(right) && this.getNeighbors(left).contains(right)) {
							Set<Node> triad = new HashSet<>(Arrays.asList(left, middle, right));
							triads.add(triad);
						}
					}
				}
			}
			
			return triads;
		}
		
		Set<Set<Node>> maximalCliques() {
			Set<Set<Node>> maximalCliques = new HashSet<>();
			bronKerbosch(new HashSet<>(), nNodes.keySet(), new HashSet<>(), maximalCliques);
			return maximalCliques;
		}
		
		private void bronKerbosch(Set<Node> R, Set<Node> P, Set<Node> X, Set<Set<Node>> maximalCliques) {
			if (P.isEmpty() && X.isEmpty()) {
				maximalCliques.add(R);
				return;
			}
			for (Node n: new HashSet<>(P)) {
				var newR = new HashSet<>(R);
				newR.add(n);
				var newP = new HashSet<>(P);
				newP.retainAll(this.getNeighbors(n));
				var newX = new HashSet<>(X);
				newX.retainAll(this.getNeighbors(n));
				bronKerbosch(newR, newP, newX, maximalCliques);
				
				P.remove(n);
				X.add(n);
			}
		}
		
		void printGraph() {
			ReadFiles.printMap(nNodes);
		}
	}
	
	private static Graph network;
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day23.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		network = initializeLAN(puzzle);
		
		Part1.part();
		Part2.part();
	}
	
	private static Graph initializeLAN(List<String> puzzle) {
		Graph result = new Graph();
		
		for (String connection: puzzle) {
			String[] nodes = connection.split("-");
			result.addNodes(nodes);
			result.addEdges(nodes[0], nodes[1]);
		}
		
		return result;
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
		public static void part() {
			Set<Set<Node>> containsChief = new HashSet<>();
			for (Set<Node> triad: network.getTriads()) {
				for (Node triadNode: triad) {
					if (triadNode.label.startsWith("t")) {
						containsChief.add(triad);
						break;
					}
				}
			}

			System.out.println("Part 1: " + containsChief.size());
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
			Set<Set<Node>> maximalCliques = network.maximalCliques();
			Set<Node> biggest = maximalCliques.stream().max(Comparator.comparing(Set::size)).get();
			List<String> biggestList = new ArrayList<>(new ArrayList<>(biggest).stream().map(x -> x.label).toList());
			Collections.sort(biggestList);
	
			System.out.println("Part 2: " + String.join(",", biggestList));
		}
	}
}


