package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day17 {
	
	private static int instructionPointer = 0;
	private static Map<String, Long> registers = new HashMap<>();
	private static List<Integer> commands = new ArrayList<>();
	private static boolean cont = true;
	
	// Class to contain all the Operations.
	static class OpCodes {
		
		/** Returns the value of the combo operand.
		 * 
		 * @param operand Operand.
		 * @return Combo Operand.
		 */
		private static Long getComboOperand(int operand) {
			if (0 <= operand && operand <= 3) return (long) operand;
			if (operand == 4) return registers.get("A");
			if (operand == 5) return registers.get("B");
			if (operand == 6) return registers.get("C");
			return null;
		}
		
		/** Performs division as specified in the problem.
		 * 
		 * @param operand Operand.
		 * @return Truncated division as specified.
		 */
		private static long division(int operand) {
			 long numerator = registers.get("A");
			 long denominator = (long) Math.pow(2L, getComboOperand(operand));
			 return numerator / denominator;
		}
		
		/** Division to store in register A.
		 * 
		 * @param operand Operand.
		 */
		public static void adv(int operand) {
			registers.put("A", division(operand));
		}
		
		/** Division to store in register B.
		 * 
		 * @param operand Operand.
		 */
		public static void bdv(int operand) {
			registers.put("B", division(operand));
		}
		
		/** Division to store in register C.
		 * 
		 * @param operand Operand.
		 */
		public static void cdv(int operand) {
			registers.put("C", division(operand));
		}
		
		/** Bitwise XOR Operation of register B and a literal to store in register B.
		 * 
		 * @param operand Operand.
		 */
		public static void bxl(int operand) {
			registers.compute("B", (k, v) -> v ^ operand);
		}
		
		/** Sets register B to a combo operand modulo 8.
		 * 
		 * @param operand Operand.
		 */
		public static void bst(int operand) {
			registers.put("B", getComboOperand(operand) % 8);
		}
		
		/** Sets (jumps) the instruction pointer according to the operand.
		 * 
		 * @param operand Operand.
		 */
		public static void jnz(int operand) {
			if (registers.get("A") == 0) return;
			instructionPointer = operand;
			cont = false;
		}
		
		/** Bitwise XOR Operation of register B and register C to store in register B.
		 * 
		 */
		public static void bxc() {
			registers.compute("B", (k, v) -> v ^ registers.get("C"));
		}
		
		/** Returns the Combo Operand modulo 8.
		 * 
		 * @param operand Operand.
		 * @return a Long value of the Combo Operand.
		 */
		public static long out(int operand) {
			return getComboOperand(operand) % 8;
		}
	}
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day17.txt");
		List<String> puzzle = ReadFiles.readCustomGap(puzzleFile, System.lineSeparator() + System.lineSeparator());
		
		parse(puzzle);
		
		Part1.part();
		Part2.part();
	}
	
	/** Initializes the program.
	 * 
	 * @param computer List of Strings read from the file.
	 */
	private static void parse(List<String> computer) {
		Pattern pattern = Pattern.compile("Register (\\w+): (\\d+)");
		
		for (String reg: computer.get(0).split(System.lineSeparator())) {
			Matcher matchRegistry = pattern.matcher(reg);
			if (matchRegistry.find()) {
				registers.put(matchRegistry.group(1), Long.valueOf(matchRegistry.group(2)));
			}
		}
		
		commands.addAll(Arrays.asList(computer.get(1).split(": ")[1].split(",")).stream().map(Integer::valueOf).toList());
	}
	
	/** Runs the program itself.
	 * 
	 * @return a List of Long numbers according to what the "Out" operation returned.
	 */
	private static List<Long> program() {
		instructionPointer = 0;
		cont = true;
		int maxCommands = commands.size();
		List<Long> output = new ArrayList<>();
		
		while (true) {
			if (instructionPointer >= maxCommands) break;
			int opcode = commands.get(instructionPointer);
			int operand = commands.get(instructionPointer + 1);
			switch (opcode) {
			case 0 -> OpCodes.adv(operand);
			case 1 -> OpCodes.bxl(operand);
			case 2 -> OpCodes.bst(operand);
			case 3 -> OpCodes.jnz(operand);
			case 4 -> OpCodes.bxc();
			case 5 -> output.add(OpCodes.out(operand));
			case 6 -> OpCodes.bdv(operand);
			case 7 -> OpCodes.cdv(operand);
			}
			if (cont) instructionPointer += 2;
			else cont = true;
		}
		
		return output;
	}
	
	/** Returns a List of Long numbers, indicating that if the register A is set to one of the numbers,
	 * 	it will return the program code itself.
	 * 
	 *  Credit to u/4HbQ for the clever algorithm.
	 * 
	 * @return a List of Longs as described above.
	 */
	private static List<Long> sameProgram() {
		List<Long> results = new ArrayList<>();
		List<Long> longCommands = commands.stream().mapToLong(Long::valueOf).boxed().toList();
		int lengthOfCommands = longCommands.size();
		LinkedList<long[]> left = new LinkedList<>();
		left.add(new long[] {longCommands.size()-1, 0});
		
		while (!left.isEmpty()) {
			long[] popped = left.poll();
			long max = popped[0];
			List<Long> listToCompare = longCommands.subList((int) max, lengthOfCommands);
			long length = popped[1];
			long a;
			for (a = 8 * length; a < 8 * (length + 1); a++) {
				registers.put("A", a);
				registers.put("B", 0L);
				registers.put("C", 0L);
				List<Long> result = program();
				if (result.equals(listToCompare)) {
					if (max == 0) results.add(a);
					else left.add(new long[] {max-1, a});
				}
			}
		}
		return results;
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
			List<Long> output = program();

			System.out.println("Part 1: " + output.stream().map(x -> x.toString()).collect(Collectors.joining(",")));
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
	
			System.out.println("Part 2: " + sameProgram().stream().mapToLong(Long::valueOf).min().getAsLong());
		}
	}
}


