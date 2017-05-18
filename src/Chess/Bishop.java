package Chess;

public class Bishop extends ChessPiece {
	static private char _v = 'F';
	
	public Bishop(int x, int y){
		_x = x;
		_y = y;
	}

	@Override
	public void addToBoard(Board board) {
		board.addPiece(_x,_y,this);
	}

	@Override
	public void drawTargets(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return ""+_v;
	}

}
