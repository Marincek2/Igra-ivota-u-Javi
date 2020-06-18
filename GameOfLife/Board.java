package hr.fer.oop.gol;

import java.util.LinkedList;
import java.util.List;

public class Board {
	private int width;
	private int height;
	private boolean cells[][];
	private boolean nextCells[][];
	private List<BoardListener> listeners;

	public Board(int width, int height) {
		if(width <= 0)
			throw new IllegalArgumentException("Width must be >0.");
		if(height <= 0)
			throw new IllegalArgumentException("Height must be >0.");
		
		this.width = width;
		this.height = height;
		cells = new boolean[width][height];
		nextCells = new boolean[width][height];
		listeners = new LinkedList<>();
	}

	public Board(int width, int height, String values) {
		this(width, height);
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				if(values.charAt(x+y*width) == 'o')
					setCell(x, y, true);
				else
					setCell(x, y, false);
			}
		}
	}

	public boolean isCellAlive(int x, int y) {
		int normalizedX = normalizeX(x);
		int normalizedY = normalizeY(y);

		return cells[normalizedX][normalizedY];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setCell(int x, int y, boolean alive) {
		int normalizedX = normalizeX(x);
		int normalizedY = normalizeY(y);
		cells[normalizedX][normalizedY] = alive;
	}

	private int normalizeX(int x) {
		int result = x;
		while(result < 0)
			result += width;
		
		return result % width;
	}
	private int normalizeY(int y) {
		int result = y;
		while(result < 0)
			result += height;
					
		return result % height;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int y=0; y < height; y++) {
			for(int x=0; x < width; x++) {
				if(cells[x][y])
					sb.append("o");
				else
					sb.append("-");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public int countAliveNeighbors(int x, int y) {
		int result = 0;
		
		if(isCellAlive(x-1, y-1)) result++;
		if(isCellAlive(x  , y-1)) result++;
		if(isCellAlive(x+1, y-1)) result++;

		if(isCellAlive(x-1, y  )) result++;
		if(isCellAlive(x+1, y  )) result++;

		if(isCellAlive(x-1, y+1)) result++;
		if(isCellAlive(x  , y+1)) result++;
		if(isCellAlive(x+1, y+1)) result++;
			
		return result;
	}

	public void playOneIteration() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				nextCells[x][y] = Rule.futureOf(isCellAlive(x, y), countAliveNeighbors(x, y));
			}
		}
		
		boolean tempCells[][] = nextCells;
		nextCells = cells;
		cells = tempCells;
		
		listeners.forEach(l -> l.boardChanged(this));
	}

	public void addListener(BoardListener listener) {
		listeners.add(listener);
	}

	public void removeListener(BoardListener listener) {
		listeners.remove(listener);
	}
	
}
