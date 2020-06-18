package hr.fer.oop.gol;

import javax.swing.SwingWorker;

public class SimulationWorker extends SwingWorker<Void, Void> {

	private Board board;

	public SimulationWorker(Board board) {
		this.board = board;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		while(!isCancelled()){
			board.playOneIteration();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//ignore
			}
		}
		
		return null;
	}

}
