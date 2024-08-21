package game;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	@Override
	public void run() {
		//TODO
		// decidir o prox celula - nova funçao (ou so meter a funcionar - calcular a celula da cabeca mais perto de goal)
		//implementar move (chamar).move
		//dps ver se cobra come-se  ele proprio (dentro da prox celula)-ver celulas disponiveis
	}


	
}
