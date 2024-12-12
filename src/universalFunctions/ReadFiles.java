package universalFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReadFiles {

	/** Reads a file and returns a list of Strings from each line in the file.
	 * 
	 * @param puzzle File object of the puzzle input file path.
	 * @return List of Strings read line-by-line.
	 */
	public static List<String> readLineByLine(File puzzle) {
		List<String> lines = new ArrayList<String>();
		try {
			FileReader convertedFile = new FileReader(puzzle);
			BufferedReader reader = new BufferedReader(convertedFile);
			String readLine;
			while ((readLine = reader.readLine()) != null) {
				lines.add(readLine);
			}
			reader.close();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		return lines;
	}
	
	/** Reads a file and return a 2-dimensional ArrayList of Strings.
	 * 
	 * @param puzzle File object of the puzzle input file path.
	 * @return 2-dimensional ArrayList of Strings read from the file.
	 */
	public static List<List<String>> readIntoMatrix(File puzzle) {
		List<List<String>> matrix = new ArrayList<>();
		try {
			FileReader convertedFile = new FileReader(puzzle);
			BufferedReader reader = new BufferedReader(convertedFile);
			String readLine;
			while ((readLine = reader.readLine()) != null) {
				matrix.add(Arrays.asList(readLine.split("")));
			}
			reader.close();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		return matrix;
	}
	
	/** Prints a Matrix.
	 * 
	 * @param list 2D List of Strings.
	 */
	public static void printMatrix(List<List<String>> list) {
		for (List<String> innerList: list) {
			System.out.println(innerList);
		}
	}
	
	/** Prints a Map (or Dictionary), line-by-line.
	 * 
	 * @param <K> Object type of the key.
	 * @param <V> Object type of the value.
	 * @param map the Map/Dictionary to print.
	 */
	public static <K, V> void printMap(Map<K, V> map) {
		for (Map.Entry<K, V> entry: map.entrySet()) {
			System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
		}
	}
	
	/** Reads the file raw
	 * 
	 * @param puzzle File object of the puzzle input file path.
	 * @return A String containing the puzzle input itself.
	 */
	public static String readFileRaw(File puzzle) {
		String result = "";
		try {
			result = Files.readString(puzzle.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
