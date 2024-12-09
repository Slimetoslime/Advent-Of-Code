package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day9 {
	
	static class Block {
		List<Integer> files;
		int freeSpaceRemaining;
		
		public Block(int size) {
			this.files = Arrays.asList(new Integer[size]);
		}
		
		public Block(int id, int size) {
			this.files = Arrays.asList(Collections.nCopies(size, id).toArray(new Integer[size]));
		}
		
		public Block(Block block) {
			this.files = block.files;
		}
		
		public boolean isEmpty() {
			return this.files.stream().allMatch(x -> x == null);
		}
		
		public void empties() {
			this.files.replaceAll(x -> x = null);
		}
		
		public boolean isFull() {
			return this.files.stream().allMatch(x -> x != null);
		}
		
		public boolean canHandle(Block block2) {
			return Collections.frequency(this.files, null) >= block2.files.size();
		}
		
		public void addLeftmost(int id) {
			for (int i = 0; i < this.files.size(); i++) {
				if (this.files.get(i) == null) {
					this.files.set(i, id);
					return;
				}
			}
		}
		
		public void addLeftmostBlock(Block blockToAdd) {
			int idNumber = blockToAdd.files.get(0);
			int sizeOfTarget = blockToAdd.files.size();
			for (int i = 0; i < this.files.size(); i++) {
				if (this.files.get(i) == null && sizeOfTarget > 0) {
					this.files.set(i, idNumber);
					sizeOfTarget--;
				}
			}
		}
		
		public int removeRightmost() {
			for (int i = this.files.size()-1; i > -1; i--) {
				Integer poppedElement = this.files.get(i);
				if (poppedElement != null) {
					this.files.set(i, null);
					return poppedElement;
				}
			}
			return -1;
		}
		
		public void removeRightmostBlock() {
			this.empties();
		}
		
		public static List<Integer> flattenBlocks(List<Block> diskMap) {
			List<Integer> result = new ArrayList<>();
			
			for (Block block: diskMap) {
				for (Integer file: block.files) {
					result.add(file);
				}
			}
			
			return result;
		}
		
		@Override
		public String toString() {
			return this.files.toString();
//			return Integer.toString(this.files.size());
		}
	}
	
//	private static List<Block> diskMap = new ArrayList<>();
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day9.txt");
		List<Integer> puzzle = Arrays.asList(ReadFiles.readFileRaw(puzzleFile).split("")).stream().mapToInt(Integer::valueOf).boxed().toList();
		
		Part1.part(initializeDiskMap(puzzle));
		Part2.part(initializeDiskMap(puzzle));
	}
	
	private static List<Block> initializeDiskMap(List<Integer> puzzle) {
		List<Block> result = new ArrayList<>();
		
		int n = 0;
		for (int i = 0; i < puzzle.size(); i++) {
			int size = puzzle.get(i);
			if (i % 2 == 0) {
				result.add(new Block(n, size));
				n++;
			} else result.add(new Block(size));
		}
		
		return result;
	}
	
	private static void moveElementToFront(List<Block> diskMap) {
		int leftmostFreePointer = 0;
		int rightmostFilesPointer = diskMap.size()-1;
		
		while (leftmostFreePointer < rightmostFilesPointer) {
			Block leftmostBlock = diskMap.get(leftmostFreePointer);
			if (leftmostBlock.isFull()) {
				leftmostFreePointer++;
				continue;
			}
			
			Block rightmostBlock = diskMap.get(rightmostFilesPointer);
			if (rightmostBlock.isEmpty()) {
				rightmostFilesPointer--;
				continue;
			}
			
			while (!leftmostBlock.isFull() && !rightmostBlock.isEmpty()) {
				int removedElement = rightmostBlock.removeRightmost();
				leftmostBlock.addLeftmost(removedElement);
			}
		}
	}
	
	private static void moveBlockToFront(List<Block> diskMap) {
		int leftmostFreePointer = 0;
		int rightmostFilesPointer = diskMap.size()-1;
		boolean suitablesNotFound = false;
		
		while (leftmostFreePointer < rightmostFilesPointer) {
			Block leftmostBlock = diskMap.get(leftmostFreePointer);
			if (leftmostBlock.isFull()) {
				leftmostFreePointer++;
				continue;
			}
			
			for (int i = rightmostFilesPointer; i > leftmostFreePointer; i--) {
				Block rightmostBlock = diskMap.get(i);
				if (rightmostBlock.isEmpty()) {
					continue;
				}
				if (leftmostBlock.canHandle(rightmostBlock)) {
					leftmostBlock.addLeftmostBlock(rightmostBlock);
					rightmostBlock.removeRightmostBlock();
					suitablesNotFound = true;
				}
			}
			
			if (suitablesNotFound) leftmostFreePointer++;
		}
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
		public static void part(List<Block> diskMap) {
			moveElementToFront(diskMap);
			List<Integer> sortedList = Block.flattenBlocks(diskMap);
			
			long result = 0;
			for (int i = 0; i < sortedList.size(); i++) {
				if (sortedList.get(i) != null) result += (i * sortedList.get(i));
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
		public static void part(List<Block> diskMap) {
			moveBlockToFront(diskMap);
			List<Integer> sortedList = Block.flattenBlocks(diskMap);
			
			long result = 0;
			for (int i = 0; i < sortedList.size(); i++) if (sortedList.get(i) != null) result += (i * sortedList.get(i));
			
			System.out.println("Part 2: " + result);
		}
	}
}


