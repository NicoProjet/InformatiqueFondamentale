package Chess;

public class Bishop extends ChessPiece {
	static private char _v = 'B';

	@Override
	void draw(Board board) {
		board.addPiece(_x,_y,_v);
	}

	@Override
	void drawTargets() {
		// TODO Auto-generated method stub
		
	}

}
