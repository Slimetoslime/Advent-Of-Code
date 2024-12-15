package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day7 {
	
	static class Hand {
		public int[] hand;
		public int bid;
		
		public Hand(int[] hand, int bid) {
			this.hand = hand;
			this.bid = bid;
		}
		
		@Override
		public String toString() {
			return Arrays.toString(hand) + " " + this.bid;
		}
	}
	
	static class HandComparator implements Comparator<Hand> {
		@Override
		public int compare(Hand hand1, Hand hand2) {
			int typeOne = getHandType(hand1);
			int typeTwo = getHandType(hand2);
			if (typeOne > typeTwo) return 1;
			else if (typeOne < typeTwo) return -1;
			else return secondOrdering(hand1.hand, hand2.hand);
		}
		
		public int getHandType(Hand hand) {
			List<Integer> fullHouse = Arrays.asList(2, 3);
			Map<Integer, Integer> frequencyHand = new HashMap<>();
			for (int card: hand.hand) {
				frequencyHand.computeIfPresent(card, (k, v) -> v+1);
				frequencyHand.putIfAbsent(card, 1);
			}
			
			Collection<Integer> frequencyValues = frequencyHand.values();
			if (frequencyValues.contains(5)) return 6;
			else if (frequencyValues.contains(4)) return 5;
			else if (frequencyValues.containsAll(fullHouse)) return 4;
			else if (frequencyValues.contains(3)) return 3;
			else if (Collections.frequency(frequencyValues, 2) == 2) return 2;
			else if (frequencyValues.contains(2)) return 1;
			else return 0;
		}
		
		public int secondOrdering(int[] bid1, int[] bid2) {
			for (int i = 0; i < bid1.length; i++) {
				int card1 = bid1[i];
				int card2 = bid2[i];
				if (card1 > card2) return 1;
				else if (card1 < card2) return -1;
			}
			return 0;
		}
	}
	
	static class JokerComparator extends HandComparator {
		@Override
		public int getHandType(Hand hand) {
			List<Integer> fullHouse = Arrays.asList(2, 3);
			Map<Integer, Integer> frequencyHand = new HashMap<>();
			for (int card: hand.hand) {
				frequencyHand.computeIfPresent(card, (k, v) -> v+1);
				frequencyHand.putIfAbsent(card, 1);
			}
			
			if (frequencyHand.containsKey(1)) {
				int poppedValue = frequencyHand.remove(1);
				if (poppedValue >= 5) frequencyHand.put(13, poppedValue);
				else frequencyHand.compute(Collections.max(frequencyHand.entrySet(), Map.Entry.comparingByValue()).getKey(), (k, v) -> v + poppedValue);
			}
			
//			System.out.println(frequencyHand);
			Collection<Integer> frequencyValues = frequencyHand.values();
			if (frequencyValues.contains(5)) return 6;
			else if (frequencyValues.contains(4)) return 5;
			else if (frequencyValues.containsAll(fullHouse)) return 4;
			else if (frequencyValues.contains(3)) return 3;
			else if (Collections.frequency(frequencyValues, 2) == 2) return 2;
			else if (frequencyValues.contains(2)) return 1;
			else return 0;
		}
	}
	
	private static Map<String, Integer> cardValues = Map.ofEntries(
			entry("2", 2),
			entry("3", 3),
			entry("4", 4),
			entry("5", 5),
			entry("6", 6),
			entry("7", 7),
			entry("8", 8),
			entry("9", 9),
			entry("T", 10),
			entry("J", 11),
			entry("Q", 12),
			entry("K", 13),
			entry("A", 14)
		);
	
	private static Map<String, Integer> jokerValues = Map.ofEntries(
			entry("2", 2),
			entry("3", 3),
			entry("4", 4),
			entry("5", 5),
			entry("6", 6),
			entry("7", 7),
			entry("8", 8),
			entry("9", 9),
			entry("T", 10),
			entry("J", 1),
			entry("Q", 11),
			entry("K", 12),
			entry("A", 13)
		);

	public static void main(String[] args) {
		File puzzle = new File("input/2023/Day7.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		List<List<String>> handsAndBids = puzzleLines.stream().map(s -> Arrays.asList(s.split(" "))).toList();

		Part1.part(handsAndBids);
		Part2.part(handsAndBids);
	}
	
	private static List<Hand> parseToHands(List<List<String>> handsAndBids, Map<String, Integer> values) {
		List<Hand> result = new ArrayList<>();
		
		for (List<String> entry: handsAndBids) {
			String[] handStrings = entry.get(0).split("");
			int[] handValue = parseCardsToInt(handStrings, values);
			Hand handEntry = new Hand(handValue, Integer.valueOf(entry.get(1)));
			result.add(handEntry);
		}
		
		return result;
	}
	
	private static int[] parseCardsToInt(String[] handStrings, Map<String, Integer> values) {
		int[] result = new int[handStrings.length];
		for (int i = 0; i < handStrings.length; i++) {
			result[i] = values.get(handStrings[i]);
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
		public static void part(List<List<String>> handsAndBids) {
			List<Hand> listOfHands = parseToHands(handsAndBids, cardValues);
			Collections.sort(listOfHands, new HandComparator());
			
			int result = 0;
			for (int i = 0; i < listOfHands.size(); i++) {
				result += ((i+1) * listOfHands.get(i).bid);
			}

			System.out.println("Part 1: " + result);
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
		public static void part(List<List<String>> handsAndBids) {
			List<Hand> listOfHands = parseToHands(handsAndBids, jokerValues);
			Collections.sort(listOfHands, new JokerComparator());
			
			int result = 0;
			for (int i = 0; i < listOfHands.size(); i++) {
				result += ((i+1) * listOfHands.get(i).bid);
			}

			System.out.println("Part 2: " + result);
		}
	}
}
