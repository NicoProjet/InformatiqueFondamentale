package museum;

public class Camera extends MuseumObject {
	private static final char[] orientations = {'E','S','W','N'};
	private char _value;
	
	public Camera(int x, int y, int orientation){
		_x = x;
		_y = y;
		_value = orientations[orientation];
	}
	
	public Camera(int x, int y, char value){
		_x = x;
		_y = y;
		if (value == 'E' || value =='S' || value == 'W' || value == 'N'){
			_value = value;
		}
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
