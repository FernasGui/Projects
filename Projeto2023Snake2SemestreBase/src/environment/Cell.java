package environment;


import game.GameElement;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.Snake;
import game.AutomaticSnake;

public class Cell{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;

	public GameElement getGameElement() {
		return gameElement;
	}


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}

	// request a cell to be occupied by Snake, If it is occupied by another Snake or Obstacle, wait.
	public  void request(Snake snake)
			throws InterruptedException {
		// TODO
	}

	public void release() {
		// TODO
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}

	@Override
	public String toString() {
		return "" + position;
	}

	public void setGameElement(GameElement obstacle) {
		// TODO

	}

	public boolean isOcupied() {
		// TODO
		return false;
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() {
		// TODO
		return null;
	}
	public void removeObstacle() {
		// TODO
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}


	public boolean isOccupiedByKiller() {
		return (gameElement!=null && gameElement instanceof Killer);
	}


	public boolean isOcupiedByObstacle() {
		return (gameElement!=null && gameElement instanceof Obstacle);
	}


	public void removeSnake(Snake snake) {
		// TODO
	}



}
