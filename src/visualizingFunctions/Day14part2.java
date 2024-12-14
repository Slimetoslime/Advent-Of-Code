package visualizingFunctions;

import java.io.File;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;
import universalFunctions.ReadFiles;
import year2024.Day14.Robot;
import year2024.Day14;

public class Day14part2 extends PApplet {

	int videoScale = 8;
	int cols = 101;
	int rows = 103;
		
	File puzzleFile = new File("input/2024/Day14.txt");
	List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
	List<Robot> robots = Day14.parseToRobots(puzzle);

	public static void main(String[] args) {
		PApplet.main("visualizingFunctions.Day14part2");
	}

	public void settings() {
		size(cols * videoScale, rows * videoScale);
	}

	public void setup() {
		drawGrid(Robot.getSetOfPositions(Day14.steppedRobots(robots, 6355, cols, rows)));
	}

	public void draw() {

	}
	
	private void drawGrid(Set<List<Integer>> positions) {
		// Begin loop for columns
		for (int i = 0; i < cols; i++) {
			// Begin loop for rows
			for (int j = 0; j < rows; j++) {
				List<Integer> position = List.of(i, j);
				// Scaling up to draw a rectangle at (x,y)
				int x = i * videoScale;
				int y = j * videoScale;
				
				if (positions.contains(position)) fill(255);
				else fill(0);
				
				stroke(0);
				// For every column and row, a rectangle is drawn at an (x,y) location scaled
				// and sized by videoScale.
				rect(x, y, videoScale, videoScale);
			}
		}
	}
}
