package Chess;

public abstract class ChessPiece {
	protected int _x;
	protected int _y;
	
	public int getX(){return _x;}
	public int getY(){return _y;}
	
	abstract void draw(Board board);
	abstract void drawTargets();
}
