package Chess;

public class Board {
	private int _n;
	private ChessPiece[][] _board;
	
	public Board(int n){
		_n = n;
		_board = new ChessPiece[_n][_n];
	}
	
	public void addPiece(ChessPiece p){
		p.addToBoard(this);
	}
	
	public void addPiece(int x, int y, ChessPiece p){
		_board[x][y] = p;
	}
	
	public String toString(){
		String s = "";
		for (int i=0; i<_n; i++){
			for (int j=0; j<_n; j++){
				if(_board[i][j] != null){
					s+=_board[i][j]+" ";
				}
				else{
					s+="* ";
				}
			}
			s += "\n";
		}
		return s;
	}
}
