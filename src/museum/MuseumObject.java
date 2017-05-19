package museum;

public abstract class MuseumObject {
	protected int _x;
	protected int _y;
	
	public int getX(){return _x;}
	public int getY(){return _y;}
	
	public abstract void addToMuseum(Museum museum);
	public abstract String toString();
	public abstract char getValue();

}
