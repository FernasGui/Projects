package gui;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.Snake;
import game.AutomaticSnake;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class BoardComponent extends JComponent implements KeyListener{

	private Board board;
	private Image obstacleImage;
	private Image killerImage;

	public BoardComponent(Board board) {
		this.board = board;
		obstacleImage=new ImageIcon(getClass().getResource("/obstacle.png")).getImage();
		killerImage=new ImageIcon(getClass().getResource("/killer.png")).getImage();
		// Necessary for key listener
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final double CELL_WIDTH=SnakeGui.BOARD_WIDTH/(double)SnakeGui.NUM_COLUMNS;
		final double CELL_HEIGHT=SnakeGui.BOARD_HEIGHT/(double)SnakeGui.NUM_ROWS;
		for (int x = 0; x < LocalBoard.WIDTH; x++) {
			for (int y = 0; y < LocalBoard.HEIGHT; y++) {
				Cell cell = board.getCell(new BoardPosition(x, y));
				Image image = null;
				// if (cell.getBonus().ordinal() < Bonus.values().length - 1) {
				if(cell.getGameElement()!=null) 
					if(cell.getGameElement() instanceof Obstacle) {
						Obstacle obstacle=(Obstacle)cell.getGameElement();
						image = obstacleImage;
						g.setColor(Color.BLACK);
						g.drawImage(image, (int)Math.round(cell.getPosition().x* CELL_WIDTH),
								(int)Math.round(cell.getPosition().y* CELL_HEIGHT),
								(int)Math.round(CELL_WIDTH),(int)Math.round(CELL_WIDTH), null);
						// write number of remaining moves
						g.setColor(Color.WHITE);
						g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,(int)CELL_HEIGHT));
						g.drawString(obstacle.getRemainingMoves()+"", (int)Math.round((cell.getPosition().x+0.15)* CELL_WIDTH), 
								(int)Math.round((cell.getPosition().y+0.9) * CELL_HEIGHT));
					}
					else if(cell.getGameElement() instanceof Goal) {
						Goal goal=(Goal)cell.getGameElement() ;
						g.setColor(Color.RED);
						g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,(int)CELL_HEIGHT));
						g.drawString(goal.getValue()+"", (int)Math.round((cell.getPosition().x+0.15)* CELL_WIDTH), 
								(int)Math.round((cell.getPosition().y+0.9) * CELL_HEIGHT));
					} else if(cell.getGameElement() instanceof Killer) {
						image = killerImage;
						g.setColor(Color.BLACK);
						g.drawImage(image, (int)Math.round(cell.getPosition().x* CELL_WIDTH),
								(int)Math.round(cell.getPosition().y* CELL_WIDTH),
								(int)Math.round(CELL_WIDTH),(int)Math.round(CELL_WIDTH), null);

					} 
				// Fill cells with snake body...
				if (cell.isOcupiedBySnake()) {
					// Check if it is dead... Will be painted white
					if(!cell.getOcuppyingSnake().isAlive())
						g.setColor(Color.WHITE);
					else
						g.setColor(Color.LIGHT_GRAY);
					g.fillRect((int)Math.round(cell.getPosition().x* CELL_WIDTH), 
							(int)Math.round(cell.getPosition().y * CELL_HEIGHT),
							(int)Math.round(CELL_WIDTH), (int)Math.round(CELL_HEIGHT));
					
				}
			
				// }
			}
			g.setColor(Color.BLACK);
			g.drawLine((int)Math.round(x * CELL_WIDTH), 0, (int)Math.round(x * CELL_HEIGHT),
					getHeight());
		}
		for (int y = 1; y < LocalBoard.HEIGHT; y++) {
			g.drawLine(0, (int)Math.round(y * CELL_WIDTH), getWidth(), 
					(int)Math.round(y* CELL_HEIGHT));
		}
		for (Snake s : board.getSnakes()) {
			if (s.getCurrentLength() > 0) {
				g.setColor(new Color(s.getIdentification() * 1000));

				((Graphics2D) g).setStroke(new BasicStroke(5));
				BoardPosition prevPos=s.getPath().getFirst();
				for (BoardPosition coordinate : s.getPath()) {
					if(prevPos!=null) {
						g.drawLine((int) Math.round((prevPos.x + .5) * CELL_WIDTH),
								(int) Math.round((prevPos.y + .5) * CELL_HEIGHT),
								(int) Math.round((coordinate.x+ .5) * CELL_WIDTH),
								(int) Math.round((coordinate.y+ .5) * CELL_HEIGHT));
						// System.out.println(p.getPosition().getLatitude()
						// * SnakeGui.CELL_SIZE + ","
						// + p.getPosition().getLongitude()
						// * SnakeGui.CELL_SIZE + ","
						// + n.getPosition().getLatitude()
						// * SnakeGui.CELL_SIZE + ","
						// + n.getPosition().getLongitude()
						// * SnakeGui.CELL_SIZE);
					}
					prevPos = coordinate;
				}

				// Paint head differently
				g.drawOval((int) Math.round((prevPos.x+0.25)* CELL_WIDTH),
						(int) Math.round((prevPos.y+0.25) * CELL_HEIGHT),10,10);
				((Graphics2D) g).setStroke(new BasicStroke(1));
			}
		}
	}

	// Only for remote clients: 2. part of the project
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Got key pressed.");
		if(e.getKeyCode()!=KeyEvent.VK_LEFT && e.getKeyCode()!=KeyEvent.VK_RIGHT && 
				e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_DOWN ) 
			return; // ignore
		board.handleKeyPress(e.getKeyCode());
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()!=KeyEvent.VK_LEFT && e.getKeyCode()!=KeyEvent.VK_RIGHT && 
				e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_DOWN ) 
			return; // ignore

		System.out.println("Got key released.");
		board.handleKeyRelease();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// ignore
	}
	
}
