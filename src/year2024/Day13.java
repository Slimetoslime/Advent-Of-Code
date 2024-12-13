package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day13 {
	
	// Class for a 2D Augmented Matrix.
	static class TwoMatrix {
		
		private double[][] matrix;
		
		/** Constructor for a 2D augmented matrix. The parameters follows these equations:
		 * 
		 *  x1 * x + y1 * y = c1, and
		 *  x2 * x + y2 * y = c2
		 * 
		 * @param x1 Refer to the equations above.
		 * @param y1 Refer to the equations above.
		 * @param c1 Refer to the equations above.
		 * @param x2 Refer to the equations above.
		 * @param y2 Refer to the equations above.
		 * @param c2 Refer to the equations above.
		 */
		public TwoMatrix(double x1, double y1, double c1, double x2, double y2, double c2) {
			matrix = new double[][] {{x1, y1, c1}, {x2, y2, c2}};
		}
		
		/** Solves the 2D Augmented Matrix / 2 Dimensional System of Linear Equations with Cramer's Rule.
		 *  Returns null if the solution is not an integer.
		 * @return A Long array with solutions for x and y, or null if either one of the solution is not an integer.
		 */
		private Long[] solveInteger() {
			Long[] solutions = new Long[2];
			
			double denominator = this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
			double xNumerator = this.matrix[0][2] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][2];
			double x = xNumerator / denominator;
			if (x % 1 != 0) return null;
			solutions[0] = (long) x;
			
			double yNumerator = this.matrix[0][0] * this.matrix[1][2] - this.matrix[0][2] * this.matrix[1][0];
			double y = yNumerator / denominator;
			if (y % 1 != 0) return null;
			solutions[1] = (long) y;
			
			return solutions;
		}
		
		// Formatting purposes.
		@Override
		public String toString() {
			return String.format("\r\n%f + %f = %f\r\n%f + %f = %f\r\n", matrix[0][0], matrix[0][1], matrix[0][2], matrix[1][0], matrix[1][1], matrix[1][2]);
		}
	}
	
	// Runner code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day13.txt");
		List<String> puzzle = ReadFiles.readCustomGap(puzzleFile, System.lineSeparator() + System.lineSeparator());
		List<TwoMatrix> systems = parseToMatrices(puzzle, 0);
		List<TwoMatrix> newSystems = parseToMatrices(puzzle, 10000000000000.0);
		
		Part1.part(systems, true);
		Part2.part(newSystems);
	}
	
	/** Parses the input file to the 2D augmented matrix class above.
	 * 
	 * @param inputs inputs.
	 * @param offset an offset meter for the target solutions.
	 * @return a List of 2D Augmented Matrices.
	 */
	public static List<TwoMatrix> parseToMatrices(List<String> inputs, double offset) {
		List<TwoMatrix> matrices = new ArrayList<>();
		String regex = "Button A: X\\+(\\d+), Y\\+(\\d+)\r\n"
					+ "Button B: X\\+(\\d+), Y\\+(\\d+)\r\n"
					+ "Prize: X=(\\d+), Y=(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		
		for (String input: inputs) {
			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				TwoMatrix m = new TwoMatrix(Double.valueOf(matcher.group(1)), 
										Double.valueOf(matcher.group(3)), 
										offset + Double.valueOf(matcher.group(5)), 
										Double.valueOf(matcher.group(2)), 
										Double.valueOf(matcher.group(4)), 
										offset + Double.valueOf(matcher.group(6)));
				matrices.add(m);
			}
		}
		
		return matrices;
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
		public static long part(List<TwoMatrix> systems, boolean solvePartOne) {
			List<List<Long>> finalResults = new ArrayList<>();
			for (TwoMatrix system: systems) {
				Long[] solutions = system.solveInteger();
				if (solutions == null) continue;
				else finalResults.add(Arrays.asList(solutions));
			}

			long finalSum = finalResults.stream().map(l -> 3 * l.get(0) + l.get(1)).mapToLong(Long::valueOf).sum();
			if (solvePartOne) System.out.println("Part 1: " + finalSum);
			return finalSum;
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
		public static void part(List<TwoMatrix> systems) {
			System.out.println("Part 2: " + Part1.part(systems, false));
		}
	}
}


