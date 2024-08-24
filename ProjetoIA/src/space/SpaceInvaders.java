package space;

import java.awt.EventQueue;

import javax.swing.JFrame;

import controllers.GameController;
import nn.NeuralNetwork;

public class SpaceInvaders extends JFrame {

	private Board board;
	private NeuralNetwork nn;
	private boolean t;
	
	public SpaceInvaders(NeuralNetwork nn, boolean t) {
		this.nn=nn;
		this.t=t;
		initUI(nn);
	}

	public Board getBoard() {
		return board;
	}

	private void initUI(NeuralNetwork nn) {
		board = new Board(nn,t);
		add(board);

		setTitle("Space Invaders");
		setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
	}


	public static void showControllerPlaying(NeuralNetwork nn, long seed) {
		EventQueue.invokeLater(() -> {

			var ex = new SpaceInvaders(nn,true);
			ex.setController(nn);
			ex.setSeed(seed);
			ex.setVisible(true);
		});
	}
	
	public void setController(NeuralNetwork nn) {
		board.setController(nn);
	}

	public void setSeed(long seed) {
		board.setSeed(seed);

	}
}
