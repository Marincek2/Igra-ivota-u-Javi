package hr.fer.oop.gol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class BoardFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final int BOARD_HEIGHT = 30;
	private static final int BOARD_WIDTH = 30;
	private JButton btnPlay, btnStop, btnTick;
	private JToggleButton buttons[][];
	private Board board;
	private SimulationWorker simulationWorker;
	private JRadioButton alive, dead;
	private SwingWorker<Void, Void> w;
	
	public BoardFrame() {
		JPanel buttonPanel = new JPanel();
		
		btnPlay = new JButton("Pokreni");
		buttonPanel.add(btnPlay);
		btnPlay.addActionListener((e) -> {
			//simulationWorker = new SimulationWorker(board);
			btnPlay.setEnabled(false);
			btnStop.setEnabled(true);
			btnTick.setEnabled(false);
			w = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					while(!isCancelled()){
						board.playOneIteration();
						for (int y = 0; y < BOARD_HEIGHT; y++) {
							for (int x = 0; x < BOARD_WIDTH; x++) {
								if(alive.isSelected()) {
									if(buttons[x][y].isSelected()) {
										buttons[x][y].setBackground(Color.RED);
										//buttons[x][y].setBackground(new Color(buttons[x][y].getBackground().getRGB() - 15));
										//buttons[x][y].setBackground(buttons[x][y].getBackground().darker());

									}
								}else {
									if(!buttons[x][y].isSelected()) {
										//buttons[x][y].setForeground(new Color(buttons[x][y].getForeground().getRGB() - 15));
										buttons[x][y].setForeground(buttons[x][y].getForeground().darker());
									}
								}
							}
						}
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							//ignore
						}
					}
					return null;
				}
				
			};
			//simulationWorker.execute();
			w.execute();
		});
		
		btnStop = new JButton("Zaustavi");
		btnStop.setEnabled(false);
		buttonPanel.add(btnStop);
		btnStop.addActionListener((e) -> {
			//simulationWorker.cancel(true);
			w.cancel(true);
			btnPlay.setEnabled(true);
			btnStop.setEnabled(false);
			btnTick.setEnabled(true);
		});
		
		btnTick = new JButton("Jedna iteracija");
		buttonPanel.add(btnTick);
		btnTick.addActionListener((e) -> {
			board.playOneIteration();
		});
		
		add(buttonPanel, BorderLayout.NORTH);
		
		initializeButonsAndBoard();
		
		alive = new JRadioButton();
		dead = new JRadioButton();
		ButtonGroup b = new ButtonGroup();
		b.add(alive);
		b.add(dead);
		JLabel a,d;
		a = new JLabel("Alive");
		d = new JLabel("Dead");
		JPanel radiopanel = new JPanel();
		radiopanel.setLayout(new FlowLayout());
		radiopanel.add(a);
		radiopanel.add(alive);
		radiopanel.add(d);
		radiopanel.add(dead);
		add(radiopanel, BorderLayout.SOUTH);

	}

	private void initializeButonsAndBoard() {
		board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
		buttons = new JToggleButton[BOARD_WIDTH][BOARD_HEIGHT];
		JPanel boardPanel = new JPanel();
		add(boardPanel, BorderLayout.CENTER);
		boardPanel.setLayout(new GridLayout(BOARD_WIDTH, BOARD_HEIGHT, 1, 1));
		
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				JToggleButton toggleButton = new JToggleButton();
				toggleButton.setPreferredSize(new Dimension(20, 20));
				buttons[x][y] = toggleButton;
				Point point = new Point(x, y);
				toggleButton.addActionListener((e) -> {
					if(((JToggleButton)e.getSource()).isSelected()) {
						board.setCell((int)point.getX(), (int)point.getY(), true);
					} else {
						board.setCell((int)point.getX(), (int)point.getY(), false);
					}
				});
				boardPanel.add(toggleButton);
			}
		}
		
		board.addListener((b) -> {
			boolean cells[][] = new boolean[BOARD_WIDTH][BOARD_HEIGHT];
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				for (int x = 0; x < BOARD_WIDTH; x++) {
					cells[x][y] = b.isCellAlive(x, y);
				}
			}
			
			SwingUtilities.invokeLater(() -> {
				for (int y = 0; y < BOARD_HEIGHT; y++) {
					for (int x = 0; x < BOARD_WIDTH; x++) {
						buttons[x][y].setSelected(cells[x][y]);
					}
				}
			});
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			BoardFrame frame = new BoardFrame();
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		});
	}

}
