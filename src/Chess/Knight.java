package Chess;

public class Knight extends ChessPiece {
	static private char _v = 'C';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets(Board board) {
		// TODO Auto-generated method stub
		
	}

}
