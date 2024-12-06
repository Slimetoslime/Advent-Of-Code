package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day4 {

	public static void main(String[] args) {
		File puzzle = new File("input/2023/Day4.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		Part1.part(puzzleLines);
		Part2.part(puzzleLines);
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
		public static void part(List<String> puzzleLines) {
			int totalScore = 0;

			for (String card : puzzleLines) {
				int pointsAchieved = getPointsFromCard(card);
				totalScore += pointsAchieved;
			}

			System.out.println("Part 1: " + totalScore);
		}

		/**
		 * Returns the Integer value of one card.
		 * 
		 * @param cardInfo String card information.
		 * @return Integer value of a card.
		 */
		public static int getPointsFromCard(String cardInfo) {
			Pattern pattern = Pattern.compile("Card .+: (.+) \\| (.+)");
			Matcher matcher = pattern.matcher(cardInfo);
			int results = 0;

			if (matcher.find()) {
				int foundWinnings = -1;
				List<String> winningNumbers = new ArrayList<>(Arrays.asList(matcher.group(1).strip().split(" ")));
				winningNumbers.removeAll(Arrays.asList(""));
				List<String> gottenNumbers = new ArrayList<>(Arrays.asList(matcher.group(2).strip().split(" ")));
				gottenNumbers.removeAll(Arrays.asList(""));

				for (String number : gottenNumbers) {
					if (winningNumbers.contains(number))
						foundWinnings++;
				}
				results += Math.floor(Math.pow(2, foundWinnings));
			}

			return results;
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
			Map<Integer, Integer> numberOfCards = new HashMap<>();
			for (int i = 0; i < puzzleLines.size(); i++) {
				numberOfCards.put(i + 1, 1);
			}

			int index = 1;
			for (String card : puzzleLines) {
				final int currentNumberOfCards = numberOfCards.get(index);
				int numberOfWinningNumbers = getNumberOfWinnings(card);
				for (int i = 0; i < numberOfWinningNumbers; i++) {
					numberOfCards.computeIfPresent(index + (i + 1), (k, v) -> v + currentNumberOfCards);
				}
				index++;
			}

			System.out.println("Part 2: " + numberOfCards.values().stream().mapToInt(Integer::valueOf).sum());
		}

		/**
		 * Returns the number of winning numbers from a String card.
		 * 
		 * @param cardInfo String information of a card.
		 * @return Number of winnings from the card.
		 */
		public static int getNumberOfWinnings(String cardInfo) {
			Pattern pattern = Pattern.compile("Card .+: (.+) \\| (.+)");
			Matcher matcher = pattern.matcher(cardInfo);
			int results = 0;

			if (matcher.find()) {
				List<String> winningNumbers = new ArrayList<>(Arrays.asList(matcher.group(1).strip().split(" ")));
				winningNumbers.removeAll(Arrays.asList(""));
				List<String> gottenNumbers = new ArrayList<>(Arrays.asList(matcher.group(2).strip().split(" ")));
				gottenNumbers.removeAll(Arrays.asList(""));

				for (String number : gottenNumbers) {
					if (winningNumbers.contains(number))
						results++;
				}
			}

			return results;
		}
	}

}
