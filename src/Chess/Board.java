package Chess;

import java.util.ArrayList;

public class Board {
	private int _n;
	private char[][] _board;
	
	public Board(int n){
		_n = n;
		_board = new char[_n][_n];
	}
	
	public void addPiece(ChessPiece p){
		p.draw(this);
	}
	
	public void addPiece(int x, int y, char v){
		_board[x][y] = v;
	}

	public void print(){
		for (int i=0; i<_n; i++){
			for (int j=0; j<_n; j++){
				System.out.print(_board[i][j]);
			}
			System.out.println(); // ligne suivante
		}
	}
}
