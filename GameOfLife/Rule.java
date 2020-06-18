package hr.fer.oop.gol;

public class Rule {

	public static boolean futureOf(boolean alive, int aliveNeighbours) {
		if(alive) {
			switch(aliveNeighbours) {
				case 0:
				case 1:
					return false;
				case 2:
				case 3:
					return true;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				default:
					return false;
			}
		} else {
			switch(aliveNeighbours) {
				case 0:
				case 1:
				case 2:
					return false;
				case 3:
					return true;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				default:
					return false;
			}
		}
	}
}
