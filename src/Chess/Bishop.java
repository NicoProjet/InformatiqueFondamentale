package Chess;

public class Bishop extends ChessPiece {
	static private char _v = 'F';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets(Board board) {
		// TODO Auto-generated method stub
		
	}

}
