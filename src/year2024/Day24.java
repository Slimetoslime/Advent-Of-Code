package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public class Day24 {
	
	static class Program {
		private Map<String, Boolean> registers;
		private Map<String, Boolean> instructions;
		
		public Program(String[] instructions, Map<String, Boolean> registers) {
			this.instructions = new LinkedHashMap<>();
			for (String ins: instructions) {
				this.instructions.put(ins, false);
			}
			this.registers = registers;
		}
		
		public List<String> findBadGates() {
			List<String> badGates = new ArrayList<>();
			Pattern pattern = Pattern.compile("(\\w+) (\\w+) (\\w+) -> (\\w+)");
			execute();
			
			for (String ins: instructions.keySet()) {
				Matcher match = pattern.matcher(ins);
				if (!match.find()) continue;
				String register1 = match.group(1);
				String operation = match.group(2);
				String register2 = match.group(3);
				String location = match.group(4);
				
				if (location.startsWith("z") && !location.equals("z45")) {
					if (!operation.equals("XOR")) {
						badGates.add(location);
					}
				} else if (!location.startsWith("z")
						&& !(register1.startsWith("x") || register1.startsWith("y"))
						&& !(register2.startsWith("x") || register2.startsWith("y"))) {
					if (operation.equals("XOR")) badGates.add(location);
				} else if ((register1.startsWith("x") || register1.startsWith("y"))
						&& (register2.startsWith("x") || register2.startsWith("y"))) {
					if (!(register1.endsWith("00") && register2.endsWith("00"))) {
						String toCheck = "";
						if (operation.equals("XOR")) {
							toCheck = "XOR";
						} else if (operation.equals("AND")) {
							toCheck = "OR";
						} else continue;
						boolean found = false;
						for (String insA: instructions.keySet()) {
							Matcher matchA = pattern.matcher(insA);
							if (!matchA.find()) continue;
							String register1A = matchA.group(1);
							String operationA = matchA.group(2);
							String register2A = matchA.group(3);
							if (insA.equals(ins)) continue;
							if ((register1A.equals(location) || register2A.equals(location)) && operationA.equals(toCheck)) {
								found = true;
								break;
							}
						}
						if (!found) badGates.add(location);
					}
				}
				
			}
			
			return badGates;
		}
		
		public void execute() {
			Pattern pattern = Pattern.compile("(\\w+) (\\w+) (\\w+) -> (\\w+)");
			while (!this.areAllExecuted()) {
				for (String ins: instructions.keySet()) {
					if (instructions.get(ins)) continue;
					
					Matcher match = pattern.matcher(ins);
					if (!match.find()) continue;
					if (!registers.containsKey(match.group(1)) || !registers.containsKey(match.group(3))) continue;
					String register1 = match.group(1);
					String register2 = match.group(3);
					String location = match.group(4);
					
					switch (match.group(2)) {
					case "AND" -> and(register1, register2, location);
					case "OR" -> or(register1, register2, location);
					case "XOR" -> xor(register1, register2, location);
					}
					
					instructions.put(ins, true);
				}
			}
		}
		
		public void and(String reg1, String reg2, String loc) {
			registers.put(loc, registers.get(reg1) & registers.get(reg2));
		}
		
		public void or(String reg1, String reg2, String loc) {
			registers.put(loc, registers.get(reg1) | registers.get(reg2));
		}
		
		public void xor(String reg1, String reg2, String loc) {
			registers.put(loc, registers.get(reg1) ^ registers.get(reg2));
		}
		
		public boolean areAllExecuted() {
			for (Boolean b: instructions.values()) {
				if (!b) return false;
			}
			return true;
		}
	}
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day24.txt");
		List<String> puzzle = ReadFiles.readDoubleGap(puzzleFile);
		Map<String, Boolean> registers = initializeRegisters(puzzle.get(0));
		
		Part1.part(registers, puzzle.get(1));
		Part2.part(registers, puzzle.get(1));
	}
	
	private static Map<String, Boolean> initializeRegisters(String start) {
		Map<String, Boolean> registers = new HashMap<>();
		Pattern pattern = Pattern.compile("(\\w+): (\\d)");
		for (String s: start.split(System.lineSeparator())) {
			Matcher matcher = pattern.matcher(s);
			while (matcher.find()) {
				registers.put(matcher.group(1), (Integer.valueOf(matcher.group(2)) == 1) ? true : false);
			}
		}
		return registers;
	}
	
	private static long powerOfTwo(int exponent) {
		long a = 1;
		for (int i = 0; i < exponent; i++) {
			a *= 2;
		}
		return a;
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
		public static void part(Map<String, Boolean> registers, String instructions) {
			Program program = new Program(instructions.split(System.lineSeparator()), registers);
			program.execute();
			
			List<Long> res = new ArrayList<>();
			for (String register: program.registers.keySet()) {
				if (!register.startsWith("z")) continue;
				if (!program.registers.get(register)) continue;
				res.add(powerOfTwo(Integer.valueOf(register.substring(1))));
			}

			System.out.println("Part 1: " + res.stream().mapToLong(Long::valueOf).sum());
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
		public static void part(Map<String, Boolean> registers, String instructions) {
			Program program = new Program(instructions.split(System.lineSeparator()), registers);
			program.execute();
			
			List<String> badGates = program.findBadGates();
			Collections.sort(badGates);
	
			System.out.println("Part 2: " + String.join(",", badGates));
		}
	}
}


