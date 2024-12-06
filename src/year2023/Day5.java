package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day5 {

	public static void main(String[] args) {
		File puzzle = new File("input/2023/Day5.txt");
		String puzzleContent = ReadFiles.readFileRaw(puzzle);
		List<String> seeds = new ArrayList<>();
		List<List<String>> mapStrings = new ArrayList<>();

		Pattern pattern = Pattern.compile(
				"seeds: (.+)(?:\\r|\\n|\\r\\n){2}"
				+ "seed-to-soil map:(?:\\r|\\n|\\r\\n)(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "soil-to-fertilizer map:(?:\\r|\\n|\\r\\n)(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "fertilizer-to-water map:(?:\\r|\\n|\\r\\n)(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "water-to-light map:(?:\\r|\\n|\\r\\n)(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "light-to-temperature map:(?:\\r|\\n|\\r\\n)\\n(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "temperature-to-humidity map:(?:\\r|\\n|\\r\\n)(.+)(?:\\r|\\n|\\r\\n){2}"
				+ "humidity-to-location map:(?:\\r|\\n|\\r\\n)(.+)",
				Pattern.DOTALL);
		Matcher matcher = pattern.matcher(puzzleContent);
		if (matcher.find()) {
			seeds = Arrays.asList(matcher.group(1).strip().split(" "));
			for (int i = 2; i < 9; i++) {
				List<String> conversionMap = new ArrayList<>();
				conversionMap = Arrays.asList(matcher.group(i).strip().split("\\r\\n"));
				mapStrings.add(conversionMap);
			}
		}
		List<Long> intSeeds = seeds.stream().map(Long::valueOf).collect(Collectors.toList());
		
		Part1.part(intSeeds, mapStrings);
		Part2.part(intSeeds, mapStrings);
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
		public static void part(List<Long> seeds, List<List<String>> mapStrings) {
			List<List<Long>> seedHistory = new ArrayList<>();
			
			seedHistory.add(seeds);
			System.out.println(seeds);
			for (List<String> conversionMap: mapStrings) {
				Long[] newSeeds = new Long[seeds.size()];
				for (String information: conversionMap) {
					String[] tempInfo = information.split(" ");
					long destination = Long.valueOf(tempInfo[0]);
					long source = Long.valueOf(tempInfo[1]);
					long range = Long.valueOf(tempInfo[2]);
					for (int i = 0; i < newSeeds.length; i++) {
						long seed = seeds.get(i);
						if (source <= seed && seed < source+range) {
							newSeeds[i] = destination + (seed - source);
						}	
					}
				}
				for (int i = 0; i < newSeeds.length; i++) {
					if (newSeeds[i] == null) newSeeds[i] = seeds.get(i);
				}
				seeds = Arrays.asList(newSeeds);
				seedHistory.add(seeds);
			} 

			Collections.sort(seeds);
			System.out.println("Part 1: " + seeds.getFirst());
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
		 * @param puzzleLines List of Strings read from the file.
		 */
		public static void part(List<Long> seeds, List<List<String>> mapStrings) {
			List<List<List<Long>>> seedHistory = new ArrayList<>();
			List<List<Long>> seedRanges = new ArrayList<>();
			for (int i = 0; i < seeds.size(); i += 2) {
				Long seedSource = seeds.get(i);
				Long seedLength = seeds.get(i+1);
				seedRanges.add(Arrays.asList(seedSource, seedSource+seedLength));
			}
			List<List<Long>> mappedRanges = new ArrayList<>();
			
			seedHistory.add(seedRanges);
			for (List<String> conversionMap: mapStrings) {
				for (String information: conversionMap) {
					List<List<Long>> newSeeds = new ArrayList<>();
					String[] tempInfo = information.split(" ");
					long destination = Long.valueOf(tempInfo[0]);
					long source = Long.valueOf(tempInfo[1]);
					long length = Long.valueOf(tempInfo[2]);
					
					while (seedRanges.size() != 0) {
						Long seedStart = seedRanges.get(0).get(0);
						Long seedEnd = seedRanges.get(0).get(1);
						seedRanges.remove(0);
						
						Long low = Math.max(seedStart, source);
						Long high = Math.min(seedEnd, source + length);
						
						if (low >= high) {
							newSeeds.add(Arrays.asList(seedStart, seedEnd));
							continue;
						}
							
						Long offset = destination - source;
						mappedRanges.add(Arrays.asList(low + offset, high + offset));
						
						if (low > seedStart) newSeeds.add(Arrays.asList(seedStart, low));
						if (high < seedEnd) newSeeds.add(Arrays.asList(high, seedEnd));
					}
					seedRanges = newSeeds;
					
				}
				seedRanges.addAll(mappedRanges);
				mappedRanges.clear();
			} 

			List<Long> flatSeeds = seedRanges.stream().flatMap(listItem -> listItem.stream()).collect(Collectors.toList());
			Collections.sort(flatSeeds);
			System.out.println("Part 2: " + flatSeeds.getFirst());
		}
	}
}
