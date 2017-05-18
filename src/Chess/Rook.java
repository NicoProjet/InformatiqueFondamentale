package Chess;

public class Rook extends ChessPiece {
	static private char _v = 'T';
	
	public Rook(int x, int y){
		_x = x;
		_y = y;
	}

	@Override
	public void addToBoard(Board board) {
		board.addPiece(_x,_y,this);
	}
	
	@Override
	public String toString() {
		return ""+_v;
	}

}
