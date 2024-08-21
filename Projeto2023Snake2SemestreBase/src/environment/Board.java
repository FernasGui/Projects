package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import game.GameElement;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;

// Class is abstract to allow the creation of other kinds of Board, which is not necessary in this project.
public abstract class Board extends Observable {
	protected Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 100;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	protected boolean isFinished;

	public Board() {
		cells = new Cell[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}

	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}

	protected BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *HEIGHT),(int) (Math.random() * HEIGHT));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}

	public void addGameElement(GameElement gameElement) {
		//TODO
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<WIDTH-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<HEIGHT-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}

	public BoardPosition selectPositionClosestToGoal(List<BoardPosition> possibleDestinations) {
		//TODO
		return null;
	}

	protected Goal addGoal() {
		Goal goal=new Goal(this);
		addGameElement( goal);
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		//TODO
	}

	public LinkedList<Snake> getSnakes() {
		return snakes;
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public void moveObstacle(Obstacle obstacle) {
		//TODO
		
	}

	
	public void removeGoal() {
		//TODO
		
	}
	public boolean isFinished() {
		return isFinished;

	}

	public LinkedList<Obstacle> getObstacles() {
		//TODO
		// percorrer cells e acumular obstáculos numa lista
		return null;
	}


	public void addSnake(Snake snake) {
		snakes.add(snake);
	}

	
	public abstract void init(); 

	
	// Ignorar: para johador humano
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();



	protected void setCells(Cell[][] cells) {
		this.cells=cells;
	}

	public Cell[][] getCells() {
		return cells;
	}

}