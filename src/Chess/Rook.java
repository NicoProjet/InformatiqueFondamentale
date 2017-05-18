package Chess;

public class Rook extends ChessPiece {
	static private char _v = 'R';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets() {
		// TODO Auto-generated method stub
		
	}

}
