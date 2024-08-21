package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import environment.Board;
import environment.LocalBoard;

public class SnakeGui implements Observer {
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 600;
	public static final int NUM_COLUMNS = 40;
	public static final int NUM_ROWS = 30;
	private JFrame frame;
	private BoardComponent boardGui;
	private Board board;

	public SnakeGui(Board board, int x,int y) {
		super();
		this.board=board;
		frame= new JFrame("The Snake Game: "+(board instanceof LocalBoard?"Local":"Remote"));
		frame.setLocation(x, y);
		buildGui();
	}

	private void buildGui() {
		frame.setLayout(new BorderLayout());
		
		boardGui = new BoardComponent(board);
		frame.add(boardGui,BorderLayout.CENTER);
		
		frame.setSize((int)(Math.round(LocalBoard.WIDTH*SnakeGui.BOARD_WIDTH/(double)SnakeGui.NUM_COLUMNS)),
		(int)(Math.round((LocalBoard.HEIGHT) +2.7)* SnakeGui.BOARD_HEIGHT/(double)SnakeGui.NUM_ROWS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
		board.addObserver(this);
		board.init();
	}

	@Override
	public void update(Observable o, Object arg) {
//		System.err.println("Updating GUI!");
		boardGui.repaint();
	}

	public boolean isFinished() {
		return board.isFinished();
	}	
}
