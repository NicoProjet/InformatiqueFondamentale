package museum;

public class Obstacle extends MuseumObject {
	private final char _value = '*';
	
	public Obstacle(int x, int y){
		_x = x;
		_y = y;
	}

	@Override
	public void addToMuseum(Museum museum) {
		museum.addObstacle(_x,_y,this);
	}

	@Override
	public String toString() {
		return ""+_value;
	}

}
