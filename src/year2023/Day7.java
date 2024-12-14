package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day7 {
	
	static class Hand {
		public String hand;
		public int bid;
		
		public Hand(String hand, int bid) {
			this.hand = hand;
			this.bid = bid;
		}
		
		@Override
		public String toString() {
			return this.hand + " " + this.bid;
		}
	}
	
	class HandComparator implements Comparator<Hand> {
		@Override
		public int compare(Hand hand1, Hand hand2) {
			return 0;
		}
		
		public int compareTypes(Hand hand1, Hand hand2) {
			Map<String, Long> frequencyHand1 = Arrays.asList(hand1.hand.split(""))
						.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			Map<String, Long> frequencyHand2 = Arrays.asList(hand2.hand.split(""))
						.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			
			
			return 0;
		}
	}

	public static void main(String[] args) {
		File puzzle = new File("test/2023/Day7.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		List<List<String>> handsAndBids = puzzleLines.stream().map(s -> Arrays.asList(s.split(" "))).toList();
		List<Hand> listOfHands = parseToHands(handsAndBids);
		
		System.out.println(listOfHands);
		Part1.part(listOfHands);
		Part2.part(puzzleLines);
	}
	
	public static List<Hand> parseToHands(List<List<String>> handsAndBids) {
		List<Hand> result = new ArrayList<>();
		
		for (List<String> entry: handsAndBids) {
			Hand handEntry = new Hand(entry.get(0), Integer.valueOf(entry.get(1)));
			result.add(handEntry);
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
		public static void part(List<Hand> listOfHands) {

			System.out.println("Part 1: " + listOfHands);
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
		public static void part(List<String> puzzleLines) {

			System.out.println("Part 2: ");
		}
	}
}
