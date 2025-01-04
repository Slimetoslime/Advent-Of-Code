package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day22 {
	
	static class SecretNumber {
		private long number;
		private long price;
		
		public SecretNumber(long number) {
			this.number = number;
			updatePrice();
		}
		
		public void updatePrice() {
			this.price = number % 10;
		}
		
		public void evolve() {
			number = mult(64);
			number = div(32);
			number = mult(2048);
			updatePrice();
		}
		
		public long mult(long x) {
			long newSec = number * x;
			return mixAndPrune(newSec, number);
		}
		
		public long div(long x) {
			long newSec = number / x;
			return mixAndPrune(newSec, number);
		}
		
		public static long mixAndPrune(long a, long b) {
			return (a ^ b) % 16777216;
		}

		@Override
		public String toString() {
			return "SecretNumber [number=" + number + "]";
		}
	}
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day22.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		
		Part1.part(puzzle);
		Part2.part(puzzle);
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
		public static void part(List<String> puzzle) {
			List<SecretNumber> secrets = puzzle.stream().map(x -> new SecretNumber(Long.valueOf(x))).toList();
			
			for (int i = 0; i < 2000; i++) {
				secrets.forEach(x -> x.evolve());
			}
			
			System.out.println("Part 1: " + secrets.stream().mapToLong(x -> x.number).sum());
		}
	}
	
	/**
	 * =========================================================================
	 * START OF PART 2
	 * =========================================================================
	 */

	class Part2 {
		
		private record PriceTuple(int position, List<Long> delta) {}
		
		/**
		 * Solves the second part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<String> puzzle) {
			List<SecretNumber> secrets = puzzle.stream().map(x -> new SecretNumber(Long.valueOf(x))).toList();
	
			Set<PriceTuple> seen = new HashSet<>();
			Map<List<Long>, Long> prices = new HashMap<>();
			Map<Integer, List<Long>> history = new LinkedHashMap<>() {
				private static final long serialVersionUID = 1L;
				@Override
				protected boolean removeEldestEntry(Map.Entry<Integer, List<Long>> eldest) {
					return this.size() > 5;
				}
			};
			history.put(-1, secrets.stream().map(x -> x.price).toList());
			
			for (int i = 0; i < 2000; i++) {
				secrets.forEach(x -> x.evolve());
				history.put(i, secrets.stream().map(x -> x.price).toList());
				if (history.size() == 5) {
					List<Long> latest = history.get(i);
					for (int j = 0; j < puzzle.size(); j++) {
						long affectedValue = latest.get(j);
						List<Long> priceChange = new ArrayList<>();
						for (int k = i-4; k < i; k++) {
							priceChange.add(history.get(k+1).get(j) - history.get(k).get(j));
						}
						PriceTuple last = new PriceTuple(j, priceChange);
						if (seen.contains(last)) continue;
						
						prices.computeIfPresent(priceChange, (k, v) -> v + affectedValue);
						prices.putIfAbsent(priceChange, affectedValue);
						seen.add(last);
					}
				}
			}
			
			System.out.println("Part 2: " + prices.values().stream().mapToLong(Long::valueOf).max().getAsLong());
		}
	}
}


