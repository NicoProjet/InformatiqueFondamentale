package Chess;

public class Rook extends ChessPiece {
	static private char _v = 'T';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets(Board board) {
		// TODO Auto-generated method stub
		
	}

}
