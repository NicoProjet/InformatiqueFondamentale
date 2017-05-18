package Chess;

public abstract class ChessPiece {
	protected int _x;
	protected int _y;
	
	public int getX(){return _x;}
	public int getY(){return _y;}
	
	public abstract void addToBoard(Board board);
	public abstract String toString();
	public abstract void drawTargets(Board board);
}
