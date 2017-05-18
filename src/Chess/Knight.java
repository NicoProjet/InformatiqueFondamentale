package Chess;

public class Knight extends ChessPiece {
	static private char _v = 'K';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets() {
		// TODO Auto-generated method stub
		
	}

}
